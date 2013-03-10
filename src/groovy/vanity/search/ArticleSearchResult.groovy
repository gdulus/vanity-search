package vanity.search

class ArticleSearchResult {

    final Long id

    final String title

    final String source

    ArticleSearchResult(Long id, String title, String source) {
        this.id = id
        this.title = title
        this.source = source
    }
}
