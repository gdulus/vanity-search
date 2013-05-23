package vanity.search.solr

import org.apache.solr.common.SolrInputDocument
import vanity.search.ArticleDocument
import vanity.search.DocumentSerializer
import vanity.search.DocumentSpecification

class SolrDocumentSerializer implements DocumentSerializer<SolrInputDocument> {

    SolrInputDocument serializeArticleDocument(final ArticleDocument articleDocument) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField(DocumentSpecification.Article.ID_FIELD, articleDocument.id);
        document.addField(DocumentSpecification.Article.SOURCE_FIELD, articleDocument.contentSourceTarget.toString());
        document.addField(DocumentSpecification.Article.TITLE_FIELD, articleDocument.title);
        document.addField(DocumentSpecification.Article.BODY_FIELD, articleDocument.body);
        document.addField(DocumentSpecification.Article.CREATED_FIELD, articleDocument.created);
        articleDocument.tags.each {
            document.addField(DocumentSpecification.Article .TAGS_FIELD, it)
        }
        return document
    }
}
