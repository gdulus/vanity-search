import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer
import vanity.search.solr.SolrDocumentSerializer
import vanity.search.solr.SolrSearchEngineIndexer

class VanitySearchGrailsPlugin {

    def version = "0.1"

    def grailsVersion = "2.2 > *"

    def dependsOn = [:]

    def pluginExcludes = [
       "grails-app/views/error.gsp"
    ]

    def title = "Vanity Search Plugin"

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
        searchEngineIndexer(SolrSearchEngineIndexer){bean->
            documentSerializer = new SolrDocumentSerializer()
            solrServer = new ConcurrentUpdateSolrServer(application.config.search.solr.url,
                                                        application.config.search.solr.queueSize.toInteger(),
                                                        application.config.search.solr.threadCount.toInteger())
        }
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def onShutdown = { event ->
    }
}
