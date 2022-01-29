def call(){
    pipeline {
        agent any
        environment {
            NEXUS_USER = credentials('user-nexus')
            NEXUS_PASS = credentials('password-nexus')
            TOKEN_SLACK = credentials('token_slack')
        }
        parameters {
            choice(
                name:'compileTool',
                choices: ['Maven', 'Gradle'],
                description: 'Seleccione herramienta de compilacion'
            )
        }
        stages {
            stage("Pipeline"){
                steps {
                    script{
                    switch(params.compileTool)
                        {
                            case 'Maven':
                                def ejecucion = load 'maven.groovy'
                                ejecucion.call()
                            break;
                            case 'Gradle':
                                def ejecucion = load 'gradle.groovy'
                                ejecucion.call()
                            break;
                        }
                    }
                }
                post{
                    
                    success{
                        slackSend color: 'good', message: "[Priscila Vergara] [JOB: ${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token_slack'
                    }
                    failure{
                        slackSend color: 'danger', message: "[Priscila Vergara] [JOB: ${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token_slack'
                    }
                }




            }
        }
    }
}
return this;