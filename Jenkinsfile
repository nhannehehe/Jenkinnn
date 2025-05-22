pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Backend') {
            steps {
                dir('QuanLiWebShopQuanAoNamNu_BE') {
                    withEnv(["PATH+MAVEN=${tool 'Maven3'}/bin"]) {
                        bat 'mvn clean package -DskipTests'
                    }
                }
            }
        }
        stage('Test Backend') {
            steps {
                echo 'Running tests...'
            }
        }
        stage('Deploy Backend') {
            steps {
                echo 'Deploying...'
            }
        }
    }
    post {
        always {
            cleanWs()
            echo 'Build failed!'
        }
    }
}