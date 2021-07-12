import {gql} from '@apollo/client';

export const MANGA_FRAGMENT = gql`
  fragment MangaFragment on Manga {
    title
    competing
    summary
    advisory
    createdAt
    updatedAt
    deletedAt
  }
`;

export const COMPLETE_MANGA_FRAGMENT = gql`
  fragment CompleteMangaFragment on Manga {
    title
    competing
    summary
    advisory
    createdAt
    updatedAt
    deletedAt
  }
`;
