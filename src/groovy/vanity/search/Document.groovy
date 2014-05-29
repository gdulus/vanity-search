package vanity.search

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import vanity.article.Article
import vanity.article.ContentSource
import vanity.article.Tag

final class Document {

    @EqualsAndHashCode(includes = ['id'])
    @ToString
    public final static class ArticleDocument {

        final String hash

        final Long id

        final String title

        final String body

        final ContentSource.Target contentSourceTarget

        final Date created

        final Set<String> tags

        private ArticleDocument(String hash, Long id, String title, String body, Date created, Set<String> tags) {
            this.hash = hash
            this.id = id
            this.title = title
            this.body = body
            this.contentSourceTarget = contentSourceTarget
            this.created = created
            this.tags = tags
        }
    }

    @ToString
    @EqualsAndHashCode(includes = ['id'])
    public static final class TagDocument {

        final Boolean validForIndexing

        final String hash

        final Long id

        final String name

        final Set<String> children

        private TagDocument(final Boolean validForIndexing, String hash, Long id, String name, Set<String> children) {
            this.validForIndexing = validForIndexing
            this.hash = hash
            this.id = id
            this.name = name
            this.children = children
        }
    }

    public static ArticleDocument asArticleDocument(final Article article) {
        return new ArticleDocument(article.hash, article.id, article.title, article.body, article.publicationDate, article.flatTagSet())
    }

    public static Set<ArticleDocument> asArticleDocuments(final List<Article> articles) {
        return articles ? articles.collect { asArticleDocument(it) } as Set : Collections.emptySet()
    }

    public static TagDocument asTagDocument(final Tag tag) {
        return new TagDocument(tag.searchable(), tag.hash, tag.id, tag.name, tag.flatChildrenSet())
    }

    public static Set<TagDocument> asTagDocuments(final List<Tag> tags) {
        return tags ? tags.collect { asTagDocument(it) } as Set : Collections.emptySet()
    }

}
