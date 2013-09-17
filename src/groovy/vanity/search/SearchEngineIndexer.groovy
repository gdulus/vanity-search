package vanity.search

public interface SearchEngineIndexer {

    public void indexArticle(ArticleDocument articleDocument)

    public void indexArticles(Set<ArticleDocument> articleDocument)

    public void deleteArticle(ArticleDocument articleDocument)

    public void deleteArticles(Set<ArticleDocument> articleDocument)

}