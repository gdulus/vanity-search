import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer
import vanity.search.solr.SolrDocumentSerializer
import vanity.search.solr.SolrSearchEngineIndexer
import vanity.search.solr.SolrSearchEngineQueryExecutor

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
        def solrServerInstance = new ConcurrentUpdateSolrServer(application.config.search.solr.url,
                                                                application.config.search.solr.queueSize.toInteger(),
                                                                application.config.search.solr.threadCount.toInteger())

        searchEngineIndexer(SolrSearchEngineIndexer){
            documentSerializer = new SolrDocumentSerializer()
            solrServer = solrServerInstance
        }

        searchEngineQueryExecutor(SolrSearchEngineQueryExecutor){
            solrServer = solrServerInstance
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
