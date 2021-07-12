import {gql} from '@apollo/client';

export const BASIC_USER_FRAGMENT = gql`
  fragment BasicUserFragment on User {
    name
    username
    email
    createdAt
    updatedAt
    deletedAt
  }
`;
