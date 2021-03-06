package vanity.search.solr

import org.apache.commons.lang.Validate
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.common.SolrInputDocument
import vanity.search.Document
import vanity.search.DocumentSpecification
import vanity.search.Index
import vanity.search.SearchEngineIndexer

class SolrSearchEngineIndexer implements SearchEngineIndexer {

    SolrServersRepository solrServersRepository

    @Override
    public void indexArticle(final Document.ArticleDocument articleDocument) {
        Validate.notNull(articleDocument, 'Document must be not null')
        indexArticles([articleDocument] as Set)
    }

    @Override
    public void indexArticles(final Set<Document.ArticleDocument> articleDocuments) {
        Validate.notEmpty((Set) articleDocuments, 'Documents set null or empty')
        List<SolrInputDocument> inputDocuments = articleDocuments.collect { serializeArticleDocument(it) }
        add(Index.ARTICLES, inputDocuments)
    }

    @Override
    void deleteArticle(final Document.ArticleDocument articleDocument) {
        Validate.notNull(articleDocument, 'Document must be not null')
        deleteArticles([articleDocument] as Set)
    }

    @Override
    void deleteArticles(final Set<Document.ArticleDocument> articleDocuments) {
        Validate.notEmpty((Set) articleDocuments, 'Documents set null or empty')
        delete(Index.ARTICLES, articleDocuments.collect { it.hash })
    }

    @Override
    void indexTag(final Document.TagDocument tagDocument) {
        Validate.notNull(tagDocument, 'Document must be not null')
        indexTags([tagDocument] as Set)
    }

    @Override
    void indexTags(final Set<Document.TagDocument> tagDocuments) {
        Validate.notEmpty((Set) tagDocuments, 'Documents set null or empty')
        List<SolrInputDocument> inputDocuments = tagDocuments.collect { serializeTagDocument(it) }
        add(Index.TAGS, inputDocuments)
    }

    @Override
    void deleteTag(final Document.TagDocument tagDocument) {
        Validate.notNull(tagDocument, 'Document must be not null')
        deleteTags([tagDocument] as Set)
    }

    @Override
    void deleteTags(final Set<Document.TagDocument> tagDocuments) {
        Validate.notEmpty((Set) tagDocuments, 'Documents set null or empty')
        delete(Index.TAGS, tagDocuments.collect { it.hash })
    }

    private SolrInputDocument serializeArticleDocument(final Document.ArticleDocument articleDocument) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField(DocumentSpecification.Article.HASH_FIELD, articleDocument.hash)
        document.addField(DocumentSpecification.Article.ID_FIELD, articleDocument.id)
        document.addField(DocumentSpecification.Article.TITLE_FIELD, articleDocument.title)
        document.addField(DocumentSpecification.Article.BODY_FIELD, articleDocument.body)
        document.addField(DocumentSpecification.Article.CREATED_FIELD, articleDocument.created)
        document.addField(DocumentSpecification.Article.TAGS_FIELD, articleDocument.tags)
        return document
    }

    private SolrInputDocument serializeTagDocument(final Document.TagDocument tagDocument) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField(DocumentSpecification.Tag.HASH_FIELD, tagDocument.hash);
        document.addField(DocumentSpecification.Tag.ID_FIELD, tagDocument.id);
        document.addField(DocumentSpecification.Tag.NAME_FIELD, tagDocument.name);
        document.addField(DocumentSpecification.Tag.CHILDREN_FIELD, tagDocument.children);
        return document
    }

    private void add(final Index index, List<SolrInputDocument> inputDocuments) {
        SolrServer solrServer = solrServersRepository.getServer(index)
        solrServer.add(inputDocuments)
        solrServer.commit()
    }

    private void delete(final Index index, List<String> ids) {
        SolrServer solrServer = solrServersRepository.getServer(index)
        solrServer.deleteById(ids)
        solrServer.commit()
    }
}
