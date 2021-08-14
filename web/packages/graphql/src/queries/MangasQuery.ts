import {graphql} from 'react-relay';

export default graphql`
  query MangasQuery($first: Int!, $after: UniqueId, $orderBy: MangaSort, $filterBy: [String!]) {
    mangas(first: $first, after: $after, orderBy: $orderBy, filterBy: $filterBy) 
      @connection(key: "MangasQuery_mangas") {
      pageInfo {
        hasNextPage
        hasPreviousPage
        startCursor
        endCursor
        totalPages
      }
      edges {
        node {
          uid
          title
          createdAt
          updatedAt
        }
        cursor
      }
    }
  }
`;
