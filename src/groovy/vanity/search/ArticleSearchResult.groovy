package vanity.search

import static vanity.article.ContentSource.Target

class ArticleSearchResult {

    final String id

    final String title

    final Target contentSourceTarget

    ArticleSearchResult(String id, String title, Target contentSourceTarget) {
        this.id = id
        this.title = title
        this.contentSourceTarget = contentSourceTarget
    }
}
