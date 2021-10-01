import {useLazyLoadQuery} from 'react-relay';

import graphql from 'babel-plugin-relay/macro';

export default function useAuthenticatedUser() {
  const response = useLazyLoadQuery(
    graphql`
      query UseAuthenticatedUserQuery {
        viewer {
          name
          username
        }
      }
    `,
    {},
  );

  return response;
}
