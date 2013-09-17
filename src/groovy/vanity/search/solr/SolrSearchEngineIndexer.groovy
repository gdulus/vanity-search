package vanity.search.solr

import org.apache.commons.lang.Validate
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.common.SolrInputDocument
import vanity.search.ArticleDocument
import vanity.search.SearchEngineIndexer

class SolrSearchEngineIndexer implements SearchEngineIndexer {

    SolrServer solrServer

    SolrDocumentSerializer documentSerializer

    public void indexArticle(final ArticleDocument articleDocument){
        Validate.notNull(articleDocument, 'Document must be not null')
        indexArticles([articleDocument] as Set)
    }

    public void indexArticles(final Set<ArticleDocument> articleDocuments){
        Validate.notEmpty((Set)articleDocuments, 'Documents set null or empty')
        List<SolrInputDocument> inputDocuments = articleDocuments.collect {documentSerializer.serializeArticleDocument(it)}
        solrServer.add(inputDocuments)
        solrServer.commit()
    }

    void deleteArticle(final ArticleDocument articleDocument) {
        Validate.notNull(articleDocument, 'Document must be not null')
        solrServer.deleteById(articleDocument.id)
        solrServer.commit()
    }

    void deleteArticles(final Set<ArticleDocument> articleDocuments) {
        Validate.notEmpty((Set)articleDocuments, 'Documents set null or empty')
        solrServer.deleteById(articleDocuments.collect{it.id})
        solrServer.commit()
    }
}
