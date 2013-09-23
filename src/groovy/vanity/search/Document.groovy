package vanity.search

import groovy.transform.EqualsAndHashCode
import vanity.article.Article
import vanity.article.ContentSource
import vanity.article.Tag

final class Document {

    @EqualsAndHashCode(includes = ['id'])
    public final static class ArticleDocument {

        final String id

        final String title

        final String body

        final ContentSource.Target contentSourceTarget

        final Date created

        final Set<String> tags

        private ArticleDocument(String id, String title, String body, Date created, Set<String> tags) {
            this.id = id
            this.title = title
            this.body = body
            this.contentSourceTarget = contentSourceTarget
            this.created = created
            this.tags = tags
        }
    }

    @EqualsAndHashCode(includes = ['id'])
    public static final class TagDocument {

        final String id

        final String name

        final Set<String> children

        private TagDocument(String id, String name, Set<String> children) {
            this.id = id
            this.name = name
            this.children = children
        }
    }

    public static ArticleDocument asArticleDocument(final Article article) {
        return new ArticleDocument(article.hash, article.title, article.body, article.publicationDate, article.flatTagSet())
    }

    public static Set<ArticleDocument> asArticleDocuments(final List<Article> articles) {
        return articles ? articles.collect { asArticleDocument(it) } as Set : Collections.emptySet()
    }

    public static TagDocument asTagDocument(final Tag tag) {
        return new TagDocument(tag.hash, tag.name, tag.flatChildrenSet())
    }

    public static Set<TagDocument> asTagDocuments(final List<Tag> tags) {
        return tags ? tags.collect { asTagDocument(it) } as Set : Collections.emptySet()
    }

}
