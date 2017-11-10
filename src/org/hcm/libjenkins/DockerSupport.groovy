package org.hcm.libjenkins

// Class declaration
class DockerSupport implements Serializable {
    def script;

    DockerSupport(script) {
        this.script=script
    }

    def insideContainer(String imageName, Closure body) {
        println " buildConfiguration = $script.buildConfiguration"
        body.delegate = script
        body.resolveStrategy = Closure.DELEGATE_FIRST
        script.echo('Doing some work now')
        script.sh(returnStdout: true, script: 'docker ps').trim()
        script.node {
            script.docker.image(imageName).inside {
              body()
            }
        }
    }
}
