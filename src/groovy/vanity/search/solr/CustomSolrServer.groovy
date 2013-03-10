package vanity.search.solr

import grails.util.Holders
import groovy.transform.InheritConstructors
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer

/**
 * Object created only to avoid limitations of spring ScopedProxyFactoryBean:
 * - for dynamic proxying incorrect type is used (http://forum.springsource.org/showthread.php?24973-UnsatisfiedDependencyException-when-injecting-scoped-beans)
 * - for CGLIB proxying default constructor is required
 */
@InheritConstructors
class CustomSolrServer extends ConcurrentUpdateSolrServer {

    CustomSolrServer() {
        super(
            Holders.config.search.solr.url,
            Holders.config.search.solr.queueSize.toInteger(),
            Holders.config.search.solr.threadCount.toInteger()
        )
    }
}
