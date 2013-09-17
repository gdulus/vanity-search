package vanity.search.utils

import vanity.article.Article
import vanity.search.ArticleDocument

class ConversionUtils {

    public static ArticleDocument asArticleDocuments(final Article article) {
        return new ArticleDocument(
            article.hash,
            article.title,
            article.body,
            article.source.target,
            article.publicationDate,
            article.flatTagSet()
        )
    }

    public static Set<ArticleDocument> asArticleDocuments(final List<Article> articles) {
        return articles ? articles.collect { asArticleDocuments(it) } as Set : Collections.emptySet()
    }
}
