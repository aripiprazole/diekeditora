import {gql} from '@apollo/client';

import {BASIC_MANGA_FRAGMENT} from '~/fragments/manga';

export const MANGAS_QUERY = gql`
  ${BASIC_MANGA_FRAGMENT}

  query ($after: Int, $before: UniqueId) {
    manga(after: $after, before: $before) {
      pageInfo {
        startCursor
        endCursor
        hasPreviousPage
        hasNextPage
      }

      edges {
        cursor
        node {
          ...BasicMangaFragment
        }
      }
    }
  }
`;
