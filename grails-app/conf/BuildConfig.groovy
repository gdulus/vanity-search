grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        compile('org.apache.solr:solr-solrj:4.1.0') {
            exclude group: 'org.slf4j'
            exclude group: 'commons-codec'
        }
        compile('org.apache.httpcomponents:httpclient:4.2.5')

    }

    plugins {
        build(":release:2.2.0") {
            export = false
        }
    }
}

grails.plugin.location.'vanity-core' = '../vanity-core'



