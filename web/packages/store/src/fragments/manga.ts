import {gql} from '@apollo/client';

export const BASIC_MANGA_FRAGMENT = gql`
  fragment BasicMangaFragment on Manga {
    title
    competing
    summary
    advisory
    createdAt
    updatedAt
    deletedAt
  }
`;
