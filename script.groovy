
def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t sofienechihi/my-repo:spring-app-1.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push sofienechihi/my-repo:spring-app-1.0'
    }
}

def cloneCode() {
   //cloning code 
// The below will clone your repo and will be checked out to master branch by default.
           git credentialsId: 'github', url: 'https://github.com/Leith-mh/SpringBoot-API-CICD.git'
           // Do a ls -lart to view all the files are cloned. It will be clonned. This is just for you to be sure about it.
           sh "ls -lart ./*" 
           // List all branches in your repo. 
           sh "git branch -a"
           // Checkout to a specific branch in your repo.
           sh "git checkout master"
} 
def buildJar() {
    echo 'building the application...'
    sh 'mvn -f ./pom.xml clean package'
    sh 'mvn -version'
} 

def test() {
    echo 'Testing the application...'
    sh 'mvn test'
} 


def pushToNexus() {
    echo "pushing the jar file to Nexus maven-snapshots repo..."
    sh 'mvn clean deploy -Dmaven.test.skip=true'
}

def sonarScan(String serverIp, String serverUser) {
    echo "Running sonarQube scan..."
    def runSonar = '"export MYSQLDB_ROOT_PASSWORD=sofiene MYSQLDB_DATABASE=pet_store MYSQLDB_LOCAL_PORT=3306 MYSQLDB_DOCKER_PORT=3306 && bash runSonarQube.sh"'
    sshagent (credentials: ['sonar-server']) {
        sh "ssh -o StrictHostKeyChecking=no ${serverUser}@${serverIp} ${runSonar}"
    }}

def deployApp(String serverIp, String serverUser) {
    echo 'deploying the application...'
    def composeRun = '"export MYSQLDB_USER=root MYSQLDB_ROOT_PASSWORD=sofiene MYSQLDB_DATABASE=pet_store MYSQLDB_LOCAL_PORT=3306 MYSQLDB_DOCKER_PORT=3306 SPRING_LOCAL_PORT=8080 SPRING_DOCKER_PORT=8080 && docker-compose up -d"'
    sshagent (credentials: ['deployment-server']) {
        sh "ssh -o StrictHostKeyChecking=no ${serverUser}@${serverIp} ${composeRun}"
    }
}

def cleanUntaggedImages(String serverIp, String serverUser){
    def cleanImages = 'docker image prune --force --filter "dangling=true"'
    sshagent (credentials: ['jenkins-server']) {
        sh "ssh -o StrictHostKeyChecking=no ${serverUser}@${serverIp} ${cleanImages}"
    }
}

return this
