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

    public SearchResult findArticles(final String query, final Integer start, final Integer rows) {
        if (!query) {
            return SearchResult.EMPTY
        }

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.start = start
        solrQuery.rows = rows
        solrQuery.add('defType', 'edismax');
        solrQuery.add('q', prepareSearchTerm(query))
        solrQuery.add('qf', "$DSA.TAGS_FIELD^5 $DSA.TITLE_FIELD $DSA.BODY_FIELD^0.5")
        solrQuery.add('pf', "$DSA.TAGS_FIELD^5 $DSA.TITLE_FIELD $DSA.BODY_FIELD^0.5")
        solrQuery.add('sort', "score desc, $DSA.CREATED_FIELD desc")
        QueryResponse response = solrServersRepository.getServer(Index.ARTICLE).query(solrQuery)
        SolrDocumentList result = response.getResults();

        List<SearchResult.SearchResultItem> items = result.collect { getAsArticleSearchResultItems(it) }
        return new SearchResult(result.numFound, result.start, items)
    }

    private static SearchResult.SearchResultItem getAsArticleSearchResultItems(final SolrDocument solrDocument) {
        return new SearchResult.SearchResultItem(
            (String) solrDocument.getFieldValue(DSA.ID_FIELD),
            (String) solrDocument.getFieldValue(DSA.TITLE_FIELD)
        )
    }

    @Override
    public SearchResult findTags(final String query, final Integer start, final Integer rows) {
        if (!query) {
            return SearchResult.EMPTY
        }

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.start = start
        solrQuery.rows = rows
        solrQuery.add('defType', 'edismax');
        solrQuery.add('q', prepareSearchTerm(query))
        solrQuery.add('qf', "$DST.NAME_FIELD^5 $DST.CHILDREN_FIELD")
        solrQuery.add('pf', "$DST.NAME_FIELD^5 $DST.CHILDREN_FIELD")
        solrQuery.add('sort', "score desc, $DST.NAME_FIELD desc")
        QueryResponse response = solrServersRepository.getServer(Index.TAG).query(solrQuery)
        SolrDocumentList result = response.getResults();

        List<SearchResult.SearchResultItem> items = result.collect { getAsTagSearchResultItems(it) }
        return new SearchResult(result.numFound, result.start, items)
    }

    private static SearchResult.SearchResultItem getAsTagSearchResultItems(final SolrDocument solrDocument) {
        return new SearchResult.SearchResultItem(
            (String) solrDocument.getFieldValue(DST.ID_FIELD),
            (String) solrDocument.getFieldValue(DST.NAME_FIELD)
        )
    }

    private static String prepareSearchTerm(final String searchTerm) {
        searchTerm.tokenize().collect { "$it*" }.join(' ')
    }
}
