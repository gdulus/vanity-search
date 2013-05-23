package vanity.search.solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import static vanity.article.ContentSource.Target
import vanity.search.ArticleSearchResult
import vanity.search.DocumentSpecification
import vanity.search.SearchEngineQueryExecutor

class SolrSearchEngineQueryExecutor implements SearchEngineQueryExecutor {

    SolrServer solrServer

    private static ArticleSearchResult getAsArticleSearchResult(final SolrDocument solrDocument){
        return new ArticleSearchResult(
            (String)solrDocument.getFieldValue(DocumentSpecification.Article.ID_FIELD),
            (String)solrDocument.getFieldValue(DocumentSpecification.Article.TITLE_FIELD),
            Target.valueOf((String)solrDocument.getFieldValue(DocumentSpecification.Article.SOURCE_FIELD))
        )
    }

    List<ArticleSearchResult> getArticles(final String queryString) {
        if (!queryString){
            return Collections.<ArticleSearchResult>emptyList()
        }

        SolrQuery query = new SolrQuery();
        query.add('defType', 'edismax');
        query.add('q', "$DocumentSpecification.Article.TAGS_FIELD:\"$queryString\" OR $DocumentSpecification.Article.TITLE_FIELD:*$queryString* OR $DocumentSpecification.Article.BODY_FIELD:*$queryString*")
        query.add('qf',"$DocumentSpecification.Article.TAGS_FIELD^100 $DocumentSpecification.Article.TITLE_FIELD $DocumentSpecification.Article.BODY_FIELD^0.1")
        query.add('sort', 'score desc, created desc')
        QueryResponse response = solrServer.query(query)
        SolrDocumentList list = response.getResults();
        return list.collect {getAsArticleSearchResult(it)}
    }

}
