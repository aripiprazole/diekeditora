/* tslint:disable */
/* eslint-disable */
// @ts-nocheck

import { ConcreteRequest } from "relay-runtime";
export type MangaSort = "BestRated" | "Empty" | "MostRead" | "Older" | "Recent" | "%future added value";
export type MangasQueryVariables = {
    first: number;
    after?: unknown | null;
    orderBy?: MangaSort | null;
    filterBy?: Array<string> | null;
};
export type MangasQueryResponse = {
    readonly mangas: {
        readonly pageInfo: {
            readonly hasNextPage: boolean;
            readonly hasPreviousPage: boolean;
            readonly startCursor: string | null;
            readonly endCursor: string | null;
            readonly totalPages: number | null;
        };
        readonly edges: ReadonlyArray<{
            readonly node: {
                readonly uid: unknown;
                readonly title: string;
                readonly createdAt: unknown;
                readonly updatedAt: unknown | null;
            } | null;
            readonly cursor: string;
        } | null> | null;
    };
};
export type MangasQuery = {
    readonly response: MangasQueryResponse;
    readonly variables: MangasQueryVariables;
};



/*
query MangasQuery(
  $first: Int!
  $after: UniqueId
  $orderBy: MangaSort
  $filterBy: [String!]
) {
  mangas(first: $first, after: $after, orderBy: $orderBy, filterBy: $filterBy) {
    pageInfo {
      hasNextPage
      hasPreviousPage
      startCursor
      endCursor
      totalPages
    }
    edges {
      node {
        uid
        title
        createdAt
        updatedAt
        __typename
      }
      cursor
    }
  }
}
*/

const node: ConcreteRequest = (function(){
var v0 = {
  "defaultValue": null,
  "kind": "LocalArgument",
  "name": "after"
},
v1 = {
  "defaultValue": null,
  "kind": "LocalArgument",
  "name": "filterBy"
},
v2 = {
  "defaultValue": null,
  "kind": "LocalArgument",
  "name": "first"
},
v3 = {
  "defaultValue": null,
  "kind": "LocalArgument",
  "name": "orderBy"
},
v4 = {
  "kind": "Variable",
  "name": "filterBy",
  "variableName": "filterBy"
},
v5 = {
  "kind": "Variable",
  "name": "orderBy",
  "variableName": "orderBy"
},
v6 = [
  {
    "alias": null,
    "args": null,
    "concreteType": "PageInfo",
    "kind": "LinkedField",
    "name": "pageInfo",
    "plural": false,
    "selections": [
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "hasNextPage",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "hasPreviousPage",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "startCursor",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "endCursor",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "totalPages",
        "storageKey": null
      }
    ],
    "storageKey": null
  },
  {
    "alias": null,
    "args": null,
    "concreteType": "MangaEdge",
    "kind": "LinkedField",
    "name": "edges",
    "plural": true,
    "selections": [
      {
        "alias": null,
        "args": null,
        "concreteType": "Manga",
        "kind": "LinkedField",
        "name": "node",
        "plural": false,
        "selections": [
          {
            "alias": null,
            "args": null,
            "kind": "ScalarField",
            "name": "uid",
            "storageKey": null
          },
          {
            "alias": null,
            "args": null,
            "kind": "ScalarField",
            "name": "title",
            "storageKey": null
          },
          {
            "alias": null,
            "args": null,
            "kind": "ScalarField",
            "name": "createdAt",
            "storageKey": null
          },
          {
            "alias": null,
            "args": null,
            "kind": "ScalarField",
            "name": "updatedAt",
            "storageKey": null
          },
          {
            "alias": null,
            "args": null,
            "kind": "ScalarField",
            "name": "__typename",
            "storageKey": null
          }
        ],
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "cursor",
        "storageKey": null
      }
    ],
    "storageKey": null
  }
],
v7 = [
  {
    "kind": "Variable",
    "name": "after",
    "variableName": "after"
  },
  (v4/*: any*/),
  {
    "kind": "Variable",
    "name": "first",
    "variableName": "first"
  },
  (v5/*: any*/)
];
return {
  "fragment": {
    "argumentDefinitions": [
      (v0/*: any*/),
      (v1/*: any*/),
      (v2/*: any*/),
      (v3/*: any*/)
    ],
    "kind": "Fragment",
    "metadata": null,
    "name": "MangasQuery",
    "selections": [
      {
        "alias": "mangas",
        "args": [
          (v4/*: any*/),
          (v5/*: any*/)
        ],
        "concreteType": "MangaConnection",
        "kind": "LinkedField",
        "name": "__MangasQuery_mangas_connection",
        "plural": false,
        "selections": (v6/*: any*/),
        "storageKey": null
      }
    ],
    "type": "Query",
    "abstractKey": null
  },
  "kind": "Request",
  "operation": {
    "argumentDefinitions": [
      (v2/*: any*/),
      (v0/*: any*/),
      (v3/*: any*/),
      (v1/*: any*/)
    ],
    "kind": "Operation",
    "name": "MangasQuery",
    "selections": [
      {
        "alias": null,
        "args": (v7/*: any*/),
        "concreteType": "MangaConnection",
        "kind": "LinkedField",
        "name": "mangas",
        "plural": false,
        "selections": (v6/*: any*/),
        "storageKey": null
      },
      {
        "alias": null,
        "args": (v7/*: any*/),
        "filters": [
          "orderBy",
          "filterBy"
        ],
        "handle": "connection",
        "key": "MangasQuery_mangas",
        "kind": "LinkedHandle",
        "name": "mangas"
      }
    ]
  },
  "params": {
    "cacheID": "929268cd4ca344f219b9b20ed1bb7841",
    "id": null,
    "metadata": {
      "connection": [
        {
          "count": "first",
          "cursor": "after",
          "direction": "forward",
          "path": [
            "mangas"
          ]
        }
      ]
    },
    "name": "MangasQuery",
    "operationKind": "query",
    "text": "query MangasQuery(\n  $first: Int!\n  $after: UniqueId\n  $orderBy: MangaSort\n  $filterBy: [String!]\n) {\n  mangas(first: $first, after: $after, orderBy: $orderBy, filterBy: $filterBy) {\n    pageInfo {\n      hasNextPage\n      hasPreviousPage\n      startCursor\n      endCursor\n      totalPages\n    }\n    edges {\n      node {\n        uid\n        title\n        createdAt\n        updatedAt\n        __typename\n      }\n      cursor\n    }\n  }\n}\n"
  }
};
})();
(node as any).hash = 'c6198864f34e35214dde9299017b9e27';
export default node;
