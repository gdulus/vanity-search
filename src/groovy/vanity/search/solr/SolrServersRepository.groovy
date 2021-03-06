package vanity.search.solr

import org.apache.solr.client.solrj.SolrServer
import vanity.search.Index

class SolrServersRepository {

    SolrServer articleServer

    SolrServer tagsServer

    public SolrServer getServer(final Index index) {
        switch (index) {
            case Index.ARTICLES:
                return articleServer
            case Index.TAGS:
                return tagsServer
        }

        throw new IllegalArgumentException("Not supported index ${index}")
    }

}
