import {gql} from '@apollo/client';

export const ME_USER_QUERY = gql`
  query {
    me {
      name
      username
      email
      createdAt
      updatedAt
      deletedAt
    }
  }
`;
