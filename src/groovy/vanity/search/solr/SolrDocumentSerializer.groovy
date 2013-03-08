package vanity.search.solr

import org.apache.solr.common.SolrInputDocument
import vanity.search.ArticleDocument
import vanity.search.DocumentSerializer
import vanity.search.DocumentSpecification

class SolrDocumentSerializer implements DocumentSerializer<SolrInputDocument> {

    SolrInputDocument serializeArticleDocument(final ArticleDocument articleDocument) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField(DocumentSpecification.Aricle.ID_FIELD, articleDocument.id);
        document.addField(DocumentSpecification.Aricle.SOURCE_FIELD, articleDocument.source);
        document.addField(DocumentSpecification.Aricle.TITLE_FIELD, articleDocument.title);
        document.addField(DocumentSpecification.Aricle.CONTENT_FIELD, articleDocument.body);
        document.addField(DocumentSpecification.Aricle.CREATED_FIELD, articleDocument.created);
        articleDocument.tags.each {
            document.addField(DocumentSpecification.Aricle.TAGS_FIELD, it)
        }
        return document
    }
}
