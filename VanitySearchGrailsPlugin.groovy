import vanity.search.solr.CustomSolrServer
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

        solrServerPrototypeBean(CustomSolrServer) {bean->
            bean.scope = 'prototype'
        }

        solrServerProxy(org.springframework.aop.scope.ScopedProxyFactoryBean) {bean ->
            bean.scope = 'singleton'
            targetBeanName = 'solrServerPrototypeBean'
            proxyTargetClass = true
        }

        searchEngineIndexer(SolrSearchEngineIndexer){bean->
            documentSerializer = new SolrDocumentSerializer()
            solrServer = ref('solrServerProxy')
        }

        searchEngineQueryExecutor(SolrSearchEngineQueryExecutor){bean->
            solrServer = ref('solrServerProxy')
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
