import {Environment, GraphQLResponse, Network, RecordSource, RequestParameters, Store, Variables} from 'relay-runtime';

import axios from 'axios';

const http = axios.create({
  baseURL: process.env.REACT_APP_GRAPHQL_API_URL,
});

/**
 * Fetches graphql with axios
 *
 * @param params
 * @param variables
 */
async function fetchGraphQL(params: RequestParameters, variables: Variables): Promise<GraphQLResponse> {
  const response = await http.post<GraphQLResponse>('/graphql', {query: params.text, variables});

  return response.data;
}

export default new Environment({
  network: Network.create(fetchGraphQL),
  store: new Store(new RecordSource()),
});
