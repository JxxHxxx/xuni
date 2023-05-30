pipeline {
    agent any

    environment {
            API_SERVER_PEM_KEY = credentials('EC2-ACCESS')
            API_REMOTE_SERVER_IP = credentials('apiServerIP')
            TEST_PROPERTIES = credentials('testProperties')
            DEV_PROPERTIES = credentials('devProperties')
        }

    tools {
        gradle '7.6.1'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning Repository'

                git url: 'https://github.com/JxxHxxx/xuni.git',
                    branch: 'main',
                    credentialsId: 'abbfa91f-b62a-4b27-bed8-3300e7cd4e27'
            }
        }

        stage('Set Env') {
            steps {
                echo 'Set Env'
                dir('/var/lib/jenkins/workspace/xuni-deploy') {
                    sh 'mkdir -p ./src/test/resources'
                    sh "touch ./src/test/resources/application.properties"
                    sh "cat $TEST_PROPERTIES > ./src/test/resources/application.properties"

                    sh 'mkdir -p ./src/main/resources'
                    sh "touch ./src/main/resources/application-dev.properties"
                    sh "cat $DEV_PROPERTIES > ./src/main/resources/application-dev.properties"
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Build'
                dir('/var/lib/jenkins/workspace/xuni-deploy') {
                    sh 'gradle build'
                }
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'EC2-ACCESS', keyFileVariable: 'PEM_KEY')]) {
                    dir('/var/lib/jenkins/workspace/xuni-deploy/build/libs') {
                        sh "scp -o StrictHostKeyChecking=no -i $PEM_KEY xuni-0.0.1-SNAPSHOT.jar ubuntu@$API_REMOTE_SERVER_IP:/home/ubuntu"
                        sh "ssh -o StrictHostKeyChecking=no -i $PEM_KEY ubuntu@$API_REMOTE_SERVER_IP pkill -f xuni-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &"
                        sh "sleep 30s"
                        sh "ssh -o StrictHostKeyChecking=no -i $PEM_KEY ubuntu@$API_REMOTE_SERVER_IP nohup java -jar -Duser.timezone=Asia/Seoul /home/ubuntu/xuni-0.0.1-SNAPSHOT.jar --spring.config.name=application-dev &"
                    }
                }
            }
        }
    }
}
