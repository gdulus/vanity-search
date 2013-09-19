import org.springframework.aop.scope.ScopedProxyFactoryBean
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

        solrArticleServer(CustomSolrServer) { bean ->
            String url = "${application.config.search.solr.url}/${application.config.search.solr.articleIndexs}"
            int queueSize = application.config.search.solr.queueSize.toInteger()
            int threadCount = application.config.search.solr.threadCount.toInteger()
            bean.constructorArgs = [url, queueSize, threadCount]
            bean.scope = 'prototype'
        }

        solrArticleServerProxy(ScopedProxyFactoryBean) { bean ->
            bean.scope = 'singleton'
            targetBeanName = 'solrArticleServer'
            proxyTargetClass = true
        }

        searchEngineIndexer(SolrSearchEngineIndexer) { bean ->
            documentSerializer = new SolrDocumentSerializer()
            solrServer = ref('solrArticleServerProxy')
        }

        searchEngineQueryExecutor(SolrSearchEngineQueryExecutor) { bean ->
            solrServer = ref('solrArticleServerProxy')
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
