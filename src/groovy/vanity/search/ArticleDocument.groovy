package vanity.search

import static vanity.article.ContentSource.Target

final class ArticleDocument {

    final String id

    final String title

    final String body

    final Target contentSourceTarget

    final Date created

    final Set<String> tags

    ArticleDocument(String id, String title, String body, Target contentSourceTarget, Date created, Set<String> tags) {
        this.id = id
        this.title = title
        this.body = body
        this.contentSourceTarget = contentSourceTarget
        this.created = created
        this.tags = tags
    }

    boolean equals(o) {
        if (this.is(o)){
            return true
        }

        if (getClass() != o.class){
            return false
        }

        ArticleDocument that = (ArticleDocument) o
        return id == that.id
    }

    int hashCode() {
        return id.hashCode()
    }

}
