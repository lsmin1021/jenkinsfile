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
                    sh './gradlew booJar'
                }
            }
            stage('After Build'){
                steps{
                    sh 'rm -f *.jar'
                    sh '''cd build/libs/
                    mv *.jar ~/workspace/sample-pipeline/'''
                
                    
                }
            }
            stage('Deploy'){
                steps {
                    step([$class: 'AWSCodeDeployPublisher', applicationName: 'cicd-app', awsAccessKey: 'AKIAVLDMQLKWEVJ4YTHY', awsSecretKey: '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', credentials: 'awsAccessKey', deploymentConfig: 'cicd-test', deploymentGroupAppspec: false, deploymentGroupName: 'cicd-group2', excludes: '', iamRoleArn: '', includes: '*.jar, appspec.yml, Scripts/*', proxyHost: '', proxyPort: 0, region: 'us-east-2', s3bucket: 'cicd-buc', s3prefix: '', subdirectory: '', versionFileName: '', waitForCompletion: false])
                }
            }

        }

}
