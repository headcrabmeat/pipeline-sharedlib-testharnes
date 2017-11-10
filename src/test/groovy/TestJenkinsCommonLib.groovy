
import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.LocalSource.localSource

import org.junit.Before
import org.junit.Test


import com.lesfurets.jenkins.unit.BasePipelineTest
import com.lesfurets.jenkins.unit.MethodCall

import static org.junit.Assert.assertEquals
import static org.assertj.core.api.Assertions.assertThat

class TestJenkinsCommonLib extends BasePipelineTest {

  String sharedLibs = ''

  @Override
  @Before
  void setUp() throws Exception {
      scriptRoots += 'jobs'
      super.setUp()
      def library = library().name('jenkins-commons')
                       .defaultVersion("master")
                       .allowOverride(true)
                       .implicit(true)
                       .targetPath('build/libs')
                       .retriever(localSource('build/libs'))
                       .build()
      helper.registerSharedLibrary(library)


  }

  @Test
  void verify_insideContainer() throws Exception {
    helper.registerAllowedMethod("sh", [Map.class], {m-> return m.toString()})
    binding.setVariable("buildConfiguration","Sample Build Configuration")
    binding.setVariable("docker",[image:{String imageName->
      // Populate any global variables if you use them later in the pipeline
      binding.setVariable("DockerImageName",imageName)
      return [inside: {Closure c ->
        c()
      }]
    }])

    def script = runScript("template/pipeline/template.groovy")
    printCallStack()
    assertThat(helper.callStack.stream()
                    .filter { c -> c.methodName == "sh" }
                    .map(MethodCall.&callArgsToString)
                    .findAll { s -> s.contains("HELLO") })
                    .hasSize(1)
  }

}
