import {gql} from '@apollo/client';

import {BASIC_USER_FRAGMENT} from '~/fragments';

export const ME_USER_QUERY = gql`
  ${BASIC_USER_FRAGMENT}

  query {
    me {
      ...BasicUserFragment
    }
  }
`;
