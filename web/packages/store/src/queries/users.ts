import {gql} from '@apollo/client';
import {USER_FRAGMENT, COMPLETE_USER_FRAGMENT} from '~/fragments';

export const USER_CONNECTION_QUERY = gql`
  ${USER_FRAGMENT}

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
          ...UserFragment
        }
      }
    }
  }
`;

export const USER_QUERY = gql`
  ${COMPLETE_USER_FRAGMENT}

  query ($username: string) {
    ...CompleteUserFragment
  }
`;
