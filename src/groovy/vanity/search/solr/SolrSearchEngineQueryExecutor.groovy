package vanity.search.solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import vanity.search.ArticleSearchResult
import vanity.search.DocumentSpecification
import vanity.search.SearchEngineQueryExecutor

import static vanity.article.ContentSource.Target

class SolrSearchEngineQueryExecutor implements SearchEngineQueryExecutor {

    SolrServer solrServer

    @Override
    List<ArticleSearchResult> getArticlesByQuery(final String query) {
        executeArticleQuery(query)
    }

    @Override
    List<ArticleSearchResult> getArticlesByTagName(final String tagName) {
        executeArticleQuery(tagName)
    }

    private List<ArticleSearchResult> executeArticleQuery(final String searchTerm) {
        if (!searchTerm) {
            return Collections.<ArticleSearchResult> emptyList()
        }

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.add('defType', 'edismax');
        solrQuery.add('q', prepareSearchTerm(searchTerm))
        solrQuery.add('qf', "$DocumentSpecification.Article.TAGS_FIELD^5 $DocumentSpecification.Article.TITLE_FIELD $DocumentSpecification.Article.BODY_FIELD^0.5")
        solrQuery.add('pf', "$DocumentSpecification.Article.TAGS_FIELD^5 $DocumentSpecification.Article.TITLE_FIELD $DocumentSpecification.Article.BODY_FIELD^0.5")
        solrQuery.add('sort', 'score desc, created desc')
        QueryResponse response = solrServer.query(solrQuery)
        SolrDocumentList list = response.getResults();
        return list.collect { getAsArticleSearchResult(it) }
    }

    private ArticleSearchResult getAsArticleSearchResult(final SolrDocument solrDocument) {
        return new ArticleSearchResult(
            (String) solrDocument.getFieldValue(DocumentSpecification.Article.ID_FIELD),
            (String) solrDocument.getFieldValue(DocumentSpecification.Article.TITLE_FIELD),
            Target.valueOf((String) solrDocument.getFieldValue(DocumentSpecification.Article.SOURCE_FIELD))
        )
    }

    private String prepareSearchTerm(final String searchTerm) {
        searchTerm.tokenize().collect { "$it*" }.join(' ')
    }

}
