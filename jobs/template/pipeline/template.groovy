@Library('jenkins-commons')
import org.hcm.libjenkins.DockerSupport

DockerSupport dockerSupport = new DockerSupport(this)

dockerSupport.insideContainer('alpine:3.5') {
  sh 'echo HELLO WORLD FROM DOCKER'
}

echo "DockerImageName = $DockerImageName"
