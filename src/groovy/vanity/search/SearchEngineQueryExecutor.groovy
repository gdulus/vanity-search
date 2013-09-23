package vanity.search

public interface SearchEngineQueryExecutor {

    public List<SearchResult.ArticleSearchResult> getArticlesByQuery(final String query)

    public List<SearchResult.TagSearchResult> getTagsByQuery(final String query)

    public List<SearchResult.ArticleSearchResult> getArticlesByTagName(final String tagName)

}