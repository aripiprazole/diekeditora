import {Environment, GraphQLResponse, Network, RecordSource, RequestParameters, Store, Variables} from 'relay-runtime';

import {AxiosInstance} from 'axios';

const fetchGraphQL =
  (http: AxiosInstance) =>
  async (params: RequestParameters, variables: Variables): Promise<GraphQLResponse> => {
    const response = await http.post<GraphQLResponse>('/graphql', {query: params.text, variables});

    return response.data;
  };

export default function getRelayEnvironment(http: AxiosInstance): Environment {
  return new Environment({
    network: Network.create(fetchGraphQL(http)),
    store: new Store(new RecordSource()),
  });
}
