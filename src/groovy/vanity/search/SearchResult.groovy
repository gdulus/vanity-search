package vanity.search

import groovy.transform.ToString

final class SearchResult {

    public static final SearchResult EMPTY = new SearchResult(0, 0, [])

    final Long numFound

    final Long start

    final List<SearchResultItem> items

    SearchResult(Long numFound, Long start, List<SearchResultItem> items) {
        this.numFound = numFound
        this.start = start
        this.items = items
    }

    @ToString
    public static final class SearchResultItem {

        final String id

        final String description

        SearchResultItem(String id, String description) {
            this.id = id
            this.description = description
        }
    }

}
