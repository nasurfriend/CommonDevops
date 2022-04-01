#!/usr/bin/env groovy

def call(Map config) {
    
	  pipeline {
		agent any
		
		stages {
		  
			  stage("Print Map Variable") {
				  steps{
				  	echo config.variable1
					echo config.varaible2
					echo config.varaible3
				  }
				
			  }
			  stage("Clean Workspace") {
				  steps{
					sh "rm -rf *"
				  }
			  }
		  
		  stage("Pull from repository"){
			  steps{
				echo "Pulling from repository:"
			  }
		  }
		  
		}
		
	  }
}
