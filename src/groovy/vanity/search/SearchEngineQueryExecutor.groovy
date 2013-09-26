package vanity.search

public interface SearchEngineQueryExecutor {

    public SearchResult findArticles(String query, Integer start, Integer rows)

    public SearchResult findTags(String query, Integer start, Integer rows)

}