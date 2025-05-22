pipeline {
    agent any
    
    tools {
        maven 'Maven3'
        jdk 'JDK'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build Backend') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }
        
        stage('Test Backend') {
            steps {
                bat 'mvn test'
            }
        }
        
        stage('Deploy Backend') {
            steps {
                echo 'Deploying Backend...'
                // Add deployment steps
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Build successful!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}