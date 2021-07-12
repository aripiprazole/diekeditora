import {gql} from '@apollo/client';

export const USER_FRAGMENT = gql`
  fragment UserFragment on User {
    name
    username
    email
    createdAt
    updatedAt
    deletedAt
  }
`;

export const COMPLETE_USER_FRAGMENT = gql`
  fragment CompleteUserFragment on User {
    name
    username
    email
    createdAt
    updatedAt
    deletedAt
  }
`;
