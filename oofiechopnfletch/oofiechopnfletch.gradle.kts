

version = "0.0.1"

project.extra["PluginName"] = "ChopnFletch"
project.extra["PluginDescription"] = "Chops AND fletches"

dependencies {
    compileOnly(group = "com.openosrs.externals", name = "iutils", version = "+")
    compileOnly(group = "com.owain.externals", name = "chinbreakhandler", version = "+")
    //compileOnly(project(":bodutils"))
    //compileOnly(project(":bodbreakhandler"))
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