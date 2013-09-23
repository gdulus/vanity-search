package vanity.search.solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import vanity.search.DocumentSpecification
import vanity.search.Index
import vanity.search.SearchEngineQueryExecutor
import vanity.search.SearchResult

class SolrSearchEngineQueryExecutor implements SearchEngineQueryExecutor {

    SolrServersRepository solrServersRepository

    @Override
    List<SearchResult.ArticleSearchResult> getArticlesByQuery(final String query) {
        executeArticleQuery(query)
    }

    @Override
    List<SearchResult.ArticleSearchResult> getArticlesByTagName(final String tagName) {
        executeArticleQuery(tagName)
    }

    private List<SearchResult.ArticleSearchResult> executeArticleQuery(final String searchTerm) {
        if (!searchTerm) {
            return Collections.<SearchResult.ArticleSearchResult> emptyList()
        }

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.add('defType', 'edismax');
        solrQuery.add('q', prepareSearchTerm(searchTerm))
        solrQuery.add('qf', "$DocumentSpecification.Article.TAGS_FIELD^5 $DocumentSpecification.Article.TITLE_FIELD $DocumentSpecification.Article.BODY_FIELD^0.5")
        solrQuery.add('pf', "$DocumentSpecification.Article.TAGS_FIELD^5 $DocumentSpecification.Article.TITLE_FIELD $DocumentSpecification.Article.BODY_FIELD^0.5")
        solrQuery.add('sort', 'score desc, created desc')
        QueryResponse response = solrServersRepository.getServer(Index.ARTICLE).query(solrQuery)
        SolrDocumentList list = response.getResults();
        return list.collect { getAsArticleSearchResult(it) }
    }

    private static SearchResult.ArticleSearchResult getAsArticleSearchResult(final SolrDocument solrDocument) {
        return new SearchResult.ArticleSearchResult(
            (String) solrDocument.getFieldValue(DocumentSpecification.Article.ID_FIELD),
            (String) solrDocument.getFieldValue(DocumentSpecification.Article.TITLE_FIELD)
        )
    }

    private static String prepareSearchTerm(final String searchTerm) {
        searchTerm.tokenize().collect { "$it*" }.join(' ')
    }

}
