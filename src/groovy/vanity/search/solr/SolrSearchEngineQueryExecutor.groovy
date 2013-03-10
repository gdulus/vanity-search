package vanity.search.solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.SolrParams
import vanity.ContentSource
import vanity.search.ArticleSearchResult
import vanity.search.DocumentSpecification
import vanity.search.SearchEngineQueryExecutor

class SolrSearchEngineQueryExecutor implements SearchEngineQueryExecutor {

    private static final String DEFAULT_FIELD = 'text'

    SolrServer solrServer

    List<ArticleSearchResult> getArticles(final String query) {
        if (!query){
            return Collections.<ArticleSearchResult>emptyList()
        }

        SolrParams parameters = new SolrQuery();
        parameters.setQuery("$DocumentSpecification.Aricle.TAGS_FIELD:$query^10 OR $DocumentSpecification.Aricle.TITLE_FIELD:$query~0.7^5 OR $DEFAULT_FIELD:$query")
        parameters.setSortField(DocumentSpecification.Aricle.CREATED_FIELD, SolrQuery.ORDER.desc)
        QueryResponse response = solrServer.query(parameters)
        SolrDocumentList list = response.getResults();
        return list.collect {
            getAsArticleSearchResult(it)
        }
    }

    private ArticleSearchResult getAsArticleSearchResult(final SolrDocument solrDocument){
        return new ArticleSearchResult(
            (String)solrDocument.getFieldValue(DocumentSpecification.Aricle.ID_FIELD),
            (String)solrDocument.getFieldValue(DocumentSpecification.Aricle.TITLE_FIELD),
            ContentSource.valueOf((String)solrDocument.getFieldValue(DocumentSpecification.Aricle.SOURCE_FIELD))
        )
    }

}
