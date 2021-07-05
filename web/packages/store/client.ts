import {ApolloClient, InMemoryCache} from '@apollo/client';

export const graphQLClient = new ApolloClient({
  uri: 'http://localhost:8080/graphql',
  cache: new InMemoryCache(),
});
