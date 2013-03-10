package vanity.search

import vanity.ContentSource

class ArticleSearchResult {

    final String id

    final String title

    final ContentSource source

    ArticleSearchResult(String id, String title, ContentSource source) {
        this.id = id
        this.title = title
        this.source = source
    }
}
