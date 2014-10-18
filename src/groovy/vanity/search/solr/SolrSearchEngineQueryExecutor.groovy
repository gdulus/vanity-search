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
    SearchResult findArticlesByTag(final String tagName, final Integer start, final Integer rows) {
        if (!tagName) {
            return SearchResult.EMPTY
        }

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.start = start
        solrQuery.rows = rows
        solrQuery.add('defType', 'edismax');
        solrQuery.add('q', tagName)
        solrQuery.add('qf', "$DSA.TAGS_FIELD^10 $DSA.TITLE_FIELD^5")
        solrQuery.add('pf', "$DSA.TAGS_FIELD^10 $DSA.TITLE_FIELD^5")
        solrQuery.add('mm', '2')
        solrQuery.add('sort', "$DSA.CREATED_FIELD desc, score desc")
        QueryResponse response = solrServersRepository.getServer(Index.ARTICLES).query(solrQuery)
        SolrDocumentList result = response.getResults();

        List<SearchResult.SearchResultItem> items = result.collect { getAsArticleSearchResultItems(it) }
        return new SearchResult(result.numFound, result.start, items)
    }

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
        solrQuery.add('sort', "$DSA.CREATED_FIELD desc, score desc")
        QueryResponse response = solrServersRepository.getServer(Index.ARTICLES).query(solrQuery)
        SolrDocumentList result = response.getResults();

        List<SearchResult.SearchResultItem> items = result.collect { getAsArticleSearchResultItems(it) }
        return new SearchResult(result.numFound, result.start, items)
    }

    private static SearchResult.SearchResultItem getAsArticleSearchResultItems(final SolrDocument solrDocument) {
        return new SearchResult.SearchResultItem(
            (Long) solrDocument.getFieldValue(DSA.ID_FIELD),
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
        solrQuery.add('q', query)
        solrQuery.add('mm', '2')
        solrQuery.add('qf', "$DST.NAME_FIELD^5 $DST.CHILDREN_FIELD")
        solrQuery.add('pf', "$DST.NAME_FIELD^5 $DST.CHILDREN_FIELD")
        solrQuery.add('sort', "$DST.NAME_FIELD desc, score desc")
        QueryResponse response = solrServersRepository.getServer(Index.TAGS).query(solrQuery)
        SolrDocumentList result = response.getResults();

        List<SearchResult.SearchResultItem> items = result.collect { getAsTagSearchResultItems(it) }
        return new SearchResult(result.numFound, result.start, items)
    }

    private static SearchResult.SearchResultItem getAsTagSearchResultItems(final SolrDocument solrDocument) {
        return new SearchResult.SearchResultItem(
            (Long) solrDocument.getFieldValue(DST.ID_FIELD),
            (String) solrDocument.getFieldValue(DST.NAME_FIELD)
        )
    }

    private static String prepareSearchTerm(final String searchTerm) {
        searchTerm.tokenize().collect { "$it*" }.join(' ')
    }
}
