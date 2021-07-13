import {gql} from '@apollo/client';

import {COMPLETE_MANGA_FRAGMENT, MANGA_FRAGMENT} from '@diekeditora/store/fragments';

export const MANGA_QUERY = gql`
  ${COMPLETE_MANGA_FRAGMENT}

  query ($title: string) {
    manga(title: $title) {
      ...CompleteMangaFragment
    }
  }
`;

export const MANGA_CONNECTION_QUERY = gql`
  ${MANGA_FRAGMENT}

  query ($after: Int, $before: UniqueId) {
    mangas(after: $after, before: $before) {
      pageInfo {
        startCursor
        endCursor
        hasPreviousPage
        hasNextPage
      }

      edges {
        cursor
        node {
          ...MangaFragment
        }
      }
    }
  }
`;
