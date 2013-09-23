package vanity.search

public interface SearchEngineIndexer {

    public void indexArticle(Document.ArticleDocument articleDocument)

    public void indexArticles(Set<Document.ArticleDocument> articleDocuments)

    public void deleteArticle(Document.ArticleDocument articleDocument)

    public void deleteArticles(Set<Document.ArticleDocument> articleDocuments)

    public void indexTag(Document.TagDocument tagDocument)

    public void indexTags(Set<Document.TagDocument> tagDocuments)

    public void deleteTag(Document.TagDocument tagDocument)

    public void deleteTags(Set<Document.TagDocument> tagDocuments)

}