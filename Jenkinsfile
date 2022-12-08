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
                    gv = load "script.groovy"
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
        stage("test") {
            steps {
                script {
                   
                 gv.test()
                }
            }
        }
    }
}
