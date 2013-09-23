package vanity.search

final class SearchResult {

    static final class ArticleSearchResult {

        final String id

        final String title

        ArticleSearchResult(String id, String title) {
            this.id = id
            this.title = title
        }
    }

    static final class TagSearchResult {

        final String id

        final String name

        TagSearchResult(String id, String name) {
            this.id = id
            this.name = name
        }
    }
}
