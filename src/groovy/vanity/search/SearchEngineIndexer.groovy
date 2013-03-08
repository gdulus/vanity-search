package vanity.search

public interface SearchEngineIndexer {

    public void indexArticle(final ArticleDocument articleDocument)

    public void indexArticles(final Set<ArticleDocument> articleDocument)

}