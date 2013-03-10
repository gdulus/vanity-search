package vanity.search

final class ArticleDocument {

    final String id

    final String title

    final String body

    final String source

    final Date created

    final Set<String> tags

    ArticleDocument(String id, String title, String body, String source, Date created, Set<String> tags) {
        this.id = id
        this.title = title
        this.body = body
        this.source = source
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
