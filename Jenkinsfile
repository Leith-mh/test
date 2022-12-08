def gv
pipeline {
    agent any
    tools {
     maven  'maven'
    }
     
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "functions.groovy"
                }
            }
        }
         
        
        stage("get code") {
            steps {
                script {
                   
                 gv.cloneCode()
                }
            }
        }
        
         stage("Build artifact") {
            steps {
                script {
                   
                 gv.buildJar()
                }
            }
        }
    }
}
