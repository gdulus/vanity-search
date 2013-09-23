package vanity.search.solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import vanity.search.DocumentSpecification.Article as DSA
import vanity.search.DocumentSpecification.Tag as DST
import vanity.search.Index
import vanity.search.SearchEngineQueryExecutor
import vanity.search.SearchResult

class SolrSearchEngineQueryExecutor implements SearchEngineQueryExecutor {

    SolrServersRepository solrServersRepository

    @Override
    public List<SearchResult.ArticleSearchResult> getArticlesByQuery(final String query) {
        executeArticleQuery(query)
    }

    @Override
    public List<SearchResult.ArticleSearchResult> getArticlesByTagName(final String tagName) {
        executeArticleQuery(tagName)
    }

    private List<SearchResult.ArticleSearchResult> executeArticleQuery(final String searchTerm) {
        if (!searchTerm) {
            return Collections.<SearchResult.ArticleSearchResult> emptyList()
        }

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.add('defType', 'edismax');
        solrQuery.add('q', prepareSearchTerm(searchTerm))
        solrQuery.add('qf', "$DSA.TAGS_FIELD^5 $DSA.TITLE_FIELD $DSA.BODY_FIELD^0.5")
        solrQuery.add('pf', "$DSA.TAGS_FIELD^5 $DSA.TITLE_FIELD $DSA.BODY_FIELD^0.5")
        solrQuery.add('sort', "score desc, $DSA.CREATED_FIELD desc")
        QueryResponse response = solrServersRepository.getServer(Index.ARTICLE).query(solrQuery)
        SolrDocumentList list = response.getResults();
        return list.collect { getAsArticleSearchResult(it) }
    }

    private static SearchResult.ArticleSearchResult getAsArticleSearchResult(final SolrDocument solrDocument) {
        return new SearchResult.ArticleSearchResult(
            (String) solrDocument.getFieldValue(DSA.ID_FIELD),
            (String) solrDocument.getFieldValue(DSA.TITLE_FIELD)
        )
    }

    @Override
    public List<SearchResult.TagSearchResult> getTagsByQuery(final String query) {
        if (!query) {
            return Collections.<SearchResult.TagSearchResult> emptyList()
        }

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.add('defType', 'edismax');
        solrQuery.add('q', prepareSearchTerm(query))
        solrQuery.add('qf', "$DST.NAME_FIELD^5 $DST.CHILDREN_FIELD")
        solrQuery.add('pf', "$DST.NAME_FIELD^5 $DST.CHILDREN_FIELD")
        solrQuery.add('sort', "score desc, $DST.NAME_FIELD desc")
        QueryResponse response = solrServersRepository.getServer(Index.TAG).query(solrQuery)
        SolrDocumentList list = response.getResults();
        return list.collect { getAsTagSearchResult(it) }
    }

    private static SearchResult.TagSearchResult getAsTagSearchResult(final SolrDocument solrDocument) {
        return new SearchResult.TagSearchResult(
            (String) solrDocument.getFieldValue(DST.ID_FIELD),
            (String) solrDocument.getFieldValue(DST.NAME_FIELD)
        )
    }

    private static String prepareSearchTerm(final String searchTerm) {
        searchTerm.tokenize().collect { "$it*" }.join(' ')
    }
}
