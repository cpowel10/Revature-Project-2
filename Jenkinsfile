node {
    def mvnHome = tool 'MyMaven'
    def dockerImageTag = "cpowell99/project2"
    stage('clone repo'){
        git 'https://github.com/cpowel10/Revature-Project-2.git'
        mvnHome = tool 'MyMaven'
    }
    stage('Build Revature-Project-2 project'){
        //sh on linux
        bat "C:\\Users\\chris\\Desktop\\apache-maven-3.8.5-bin\\apache-maven-3.8.5\\bin\\mvn clean install -DskipTests=true"
        //jar file will be generated
    }
    stage('Build docker image'){
        dockerImage = docker.build("cpowell99/project2:project2")
    }
    stage('Build docker deploy'){
        echo "Docker Image Tag Name : ${dockerImageTag}"
        //docker-hub-credentials - we have to create in jenkins credentials
        docker.withRegistry('https://registry.hub.docker.com','docker-hub-credentials'){
            //dockerImage.push("${env.BUILD_NUMBER}")
            dockerImage.push("latest")
        }
    }
}
