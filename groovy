pipeline{
    agent {
        node {
            label 'master'
        }
    }
        stages{
            stage ('Git Progress') {
                steps {
                    git 'https://github.com/lsmin1021/cicd-test.git'
                }

            }
            stage('Gradle Build'){
                steps{
                    sh './gradlew build'
                }
                post{
                    success{
                        slackSend channel:'#cicd', color:'good',message:"The pipeline ${currentBuild.fullDisplayName} stage Build JAR successfully."
                    }
                    failure {
                        slackSend channel: '#cicd', color: 'danger', message: "The pipeline ${currentBuild.fullDisplayName} stage Build JAR failed."
                    }
                }
            }
            stage('After Build'){
                steps{
                    sh 'rm -f *.jar'
                    sh '''cd build/libs/
mv *.jar ~/workspace/cicd-test2/'''
                }
            }
            stage('Deploy'){
                steps {
                    step([$class: 'AWSCodeDeployPublisher', applicationName: 'cicd-app', awsAccessKey: 'AKIAVLDMQLKWEVJ4YTHY', awsSecretKey: '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', credentials: 'awsAccessKey', deploymentConfig: 'cicd-test', deploymentGroupAppspec: false, deploymentGroupName: 'cicd-group2', excludes: '', iamRoleArn: '', includes: '*.jar, appspec.yml, Scripts/*', proxyHost: '', proxyPort: 0, region: 'us-east-2', s3bucket: 'cicd-buc', s3prefix: '', subdirectory: '', versionFileName: '', waitForCompletion: false])
                }
                post {
                    success {
                        slackSend channel: '#cicd', color: 'good', message: "The pipeline ${currentBuild.fullDisplayName} stage Deploy successfully."
                    }
                    failure {
                        slackSend channel: '#cicd', color: 'danger', message: "The pipeline ${currentBuild.fullDisplayName} stage Deploy failed."
                    }
                }
            }


        }

}
