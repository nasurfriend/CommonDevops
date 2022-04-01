#!/usr/bin/env groovy

def call(Map config) {
    
	  pipeline {
		agent any
		
		stages {
		  
			  stage("Print Map Variable") {
				echo config.variable1
				echo config.varaible2
				echo config.varaible3
			  }
			  stage("Clean Workspace") {
				rm -rf "*"
			  }
		  
		  stage("Pull from repository"){
			  withCredentials([usernamePassword(credentialsId: "nexus", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
							statusCode=sh(script: "curl --write-out '%{http_code}' -s -o /dev/null -u $Username:$Password -v -X GET '${REPO_URL}'", returnStdout: true).trim().tokenize("\n")
							echo "HTTP response status code: $statusCode"
						}
						if(statusCode==['200']) {
								withCredentials([usernamePassword(credentialsId: "nexus", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
									sh "mkdir ${WORKDIR} && cd ${WORKDIR} && curl --write-out '%{http_code}' -s -L -u $USERNAME:$PASSWORD -v -X GET '${REPO_URL}' --output ${FILE_NAME}"
								}
								
							withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
								unzip dir: "${WORKDIR}/${BUILD_FOLDER}", glob: "", zipFile: "${WORKDIR}/${FILE_NAME}"
							}
							deployKey(ec2_cred,ec2_ip)
						} else {
							currentBuild.result = 'ABORTED'
							error('Apps Not Found')
						}
		  }
		  
		}
		
	  }
}
