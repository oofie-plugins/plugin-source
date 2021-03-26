

version = "2.0.3"

project.extra["PluginName"] = "KittyToKat"
project.extra["PluginDescription"] = "Pets and Feeds Kitten"

dependencies {
    compileOnly(group = "com.openosrs.externals", name = "iutils", version = "3.1.0+")
    compileOnly(group = "com.owain.externals", name = "chinbreakhandler", version = "+")
}

tasks {
    jar {
        manifest {
            attributes(mapOf(
                    "Plugin-Version" to project.version,
                    "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                    "Plugin-Provider" to project.extra["PluginProvider"],
                    "Plugin-Dependencies" to
                            arrayOf(
                                nameToId("iUtils"),
                                nameToId("ChinBreakHandler")
                            ).joinToString(),
                    "Plugin-Description" to project.extra["PluginDescription"],
                    "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}