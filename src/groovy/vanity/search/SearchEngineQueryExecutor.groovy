package vanity.search

public interface SearchEngineQueryExecutor {

    public List<ArticleSearchResult> getArticlesByQuery(final String query)

    public List<ArticleSearchResult> getArticlesByTagName(final String tagName)

}