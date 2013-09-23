package vanity.search.solr

import org.apache.solr.client.solrj.SolrServer
import vanity.search.Index

class SolrServersRepository {

    SolrServer articleServer

    SolrServer tagsServer

    public SolrServer getServer(final Index index) {
        switch (index) {
            case Index.ARTICLE:
                return articleServer
            case Index.TAG:
                return tagsServer
        }

        throw new IllegalArgumentException("Not supported index ${index}")
    }

}
