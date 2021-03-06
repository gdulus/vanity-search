package vanity.search

final class DocumentSpecification {

    public static final class Article {

        public static final String HASH_FIELD = 'hash'

        public static final String ID_FIELD = 'id'

        public static final String TITLE_FIELD = 'title'

        public static final String TAGS_FIELD = 'tags'

        public static final String BODY_FIELD = 'text'

        public static final String CREATED_FIELD = 'created'

    }

    public static final class Tag {

        public static final String HASH_FIELD = 'hash'

        public static final String ID_FIELD = 'id'

        public static final String NAME_FIELD = 'name'

        public static final String CHILDREN_FIELD = 'children'

    }

}
