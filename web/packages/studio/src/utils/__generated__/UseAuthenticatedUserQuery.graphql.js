/**
 * @flow
 */

/* eslint-disable */

'use strict';

/*::
import type { ConcreteRequest } from 'relay-runtime';
export type UseAuthenticatedUserQueryVariables = {||};
export type UseAuthenticatedUserQueryResponse = {|
  +viewer: {|
    +name: string,
    +username: string,
  |}
|};
export type UseAuthenticatedUserQuery = {|
  variables: UseAuthenticatedUserQueryVariables,
  response: UseAuthenticatedUserQueryResponse,
|};
*/


/*
query UseAuthenticatedUserQuery {
  viewer {
    name
    username
  }
}
*/

const node/*: ConcreteRequest*/ = (function(){
var v0 = [
  {
    "alias": null,
    "args": null,
    "concreteType": "User",
    "kind": "LinkedField",
    "name": "viewer",
    "plural": false,
    "selections": [
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "name",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "username",
        "storageKey": null
      }
    ],
    "storageKey": null
  }
];
return {
  "fragment": {
    "argumentDefinitions": [],
    "kind": "Fragment",
    "metadata": null,
    "name": "UseAuthenticatedUserQuery",
    "selections": (v0/*: any*/),
    "type": "Query",
    "abstractKey": null
  },
  "kind": "Request",
  "operation": {
    "argumentDefinitions": [],
    "kind": "Operation",
    "name": "UseAuthenticatedUserQuery",
    "selections": (v0/*: any*/)
  },
  "params": {
    "cacheID": "8ea009693a369444c33d7e5e462535f4",
    "id": null,
    "metadata": {},
    "name": "UseAuthenticatedUserQuery",
    "operationKind": "query",
    "text": "query UseAuthenticatedUserQuery {\n  viewer {\n    name\n    username\n  }\n}\n"
  }
};
})();
// prettier-ignore
(node/*: any*/).hash = 'a6b65ccb832b8f7acd4577e7a8d9967d';

module.exports = node;
