pipeline {
    agent any

    environment {
            API_SERVER_PEM_KEY = credentials('mt-dp-pem')
            API_REMOTE_SERVER_IP = credentials('apiServerIP')
            TEST_PROPERTIES = credentials('testProperties')
            DEV_PROPERTIES = credentials('v2-devProperties')
            DEPLOY_SCRIPT = credentials('DEPLOY_SCRIPT')
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
                script {
                    def addresses = ['13.124.210.71', '13.125.241.19']

                        dir('/var/lib/jenkins/workspace/xuni-deploy/build/libs') {

                            for (int i=0; i < addresses.size(); i++) {
                                echo "start ${addresses[i]} deploy"
                                sh "scp -o StrictHostKeyChecking=no -i ${API_SERVER_PEM_KEY} xuni-0.0.1-SNAPSHOT.jar ubuntu@${addresses[i]}:/home/ubuntu"
                                sh "ssh -o StrictHostKeyChecking=no -i ${API_SERVER_PEM_KEY} ubuntu@${addresses[i]} sudo rm -f /home/ubuntu/deploy.sh"
                                sh "scp -o StrictHostKeyChecking=no -i ${API_SERVER_PEM_KEY} ${DEPLOY_SCRIPT} ubuntu@${addresses[i]}:/home/ubuntu"
                                sh "ssh -o StrictHostKeyChecking=no -i ${API_SERVER_PEM_KEY} ubuntu@${addresses[i]} chmod +x /home/ubuntu/deploy.sh"
                                sh "ssh -o StrictHostKeyChecking=no -i ${API_SERVER_PEM_KEY} ubuntu@${addresses[i]} /home/ubuntu/deploy.sh &"
                            }
                        }
                    }
                }
            }
        }
    }
}
