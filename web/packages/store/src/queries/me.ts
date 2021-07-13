import {gql} from '@apollo/client';

import {USER_FRAGMENT} from '@diekeditora/store/fragments';

export const ME_USER_QUERY = gql`
  ${USER_FRAGMENT}

  query {
    me {
      ...UserFragment
    }
  }
`;
