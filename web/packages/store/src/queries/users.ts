import {gql} from '@apollo/client';
import {BASIC_USER_FRAGMENT} from '~/fragments';

export const USERS_QUERY = gql`
  ${BASIC_USER_FRAGMENT}

  query ($after: Int, $before: UniqueId) {
    users(after: $after, before: $before) {
      pageInfo {
        startCursor
        endCursor
        hasPreviousPage
        hasNextPage
      }

      edges {
        cursor
        node {
          ...BasicUserFragment
        }
      }
    }
  }
`;
