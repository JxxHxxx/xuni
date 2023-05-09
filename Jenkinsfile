pipeline {
    agent any

    environment {
            API_SERVER_PEM_KEY = credentials('EC2-ACCESS')
            API_REMOTE_SERVER_IP = credentials('apiServerIP')
            DEV_PROPERTIES = credentials('devProperties')
            TEST_PROPERTIES = credentials('testProperties')
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
                dir('/var/lib/jenkins/workspace/xuni_ci_cd') {
                    sh 'mkdir -p ./src/test/resources'
                    sh 'touch ./src/test/resources/application.properties'
                    sh "echo $TEST_PROPERTIES >> ./src/test/resources/application.properties"

                    sh 'mkdir -p ./src/main/resources'
                    sh 'touch ./src/main/resources/application-dev.properties'
                    sh "echo $DEV_PROPERTIES >> ./src/main/resources/application-dev.properties"
                }
            }
        }
    }
}


