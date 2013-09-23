import org.springframework.aop.scope.ScopedProxyFactoryBean
import vanity.search.solr.CustomSolrServer
import vanity.search.solr.SolrSearchEngineIndexer
import vanity.search.solr.SolrSearchEngineQueryExecutor
import vanity.search.solr.SolrServersRepository

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

        solrTagsServer(CustomSolrServer) { bean ->
            String url = "${application.config.search.solr.url}/${application.config.search.solr.tagsIndexs}"
            int queueSize = application.config.search.solr.queueSize.toInteger()
            int threadCount = application.config.search.solr.threadCount.toInteger()
            bean.constructorArgs = [url, queueSize, threadCount]
            bean.scope = 'prototype'
        }

        solrServersRepository(SolrServersRepository) {
            articleServer = { ScopedProxyFactoryBean bean ->
                targetBeanName = 'solrArticleServer'
                proxyTargetClass = true
            }

            tagsServer = { ScopedProxyFactoryBean bean ->
                targetBeanName = 'solrTagsServer'
                proxyTargetClass = true
            }
        }

        searchEngineIndexer(SolrSearchEngineIndexer) {
            solrServersRepository = ref('solrServersRepository')
        }

        searchEngineQueryExecutor(SolrSearchEngineQueryExecutor) {
            solrServersRepository = ref('solrServersRepository')
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
