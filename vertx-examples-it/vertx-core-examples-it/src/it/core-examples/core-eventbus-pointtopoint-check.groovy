helper.ensureSucceededInDeployingVerticle()
helper.ensureClientSucceededInDeployingVerticle()
helper.ensureTextNotContainedInErrorStream("Unhandled Exception")
helper.ensureTextNotContainedInClientErrorStream("Unhandled Exception")

helper.ensureTextInOutputStream("pong")
helper.ensureTextInClientOutputStream("ping")
