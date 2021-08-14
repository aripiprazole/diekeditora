/* tslint:disable */
/* eslint-disable */
// @ts-nocheck

import { ConcreteRequest } from "relay-runtime";
export type MangaQueryVariables = {
    uid: unknown;
};
export type MangaQueryResponse = {
    readonly manga: {
        readonly advisory: number;
        readonly competing: boolean;
        readonly createdAt: unknown;
        readonly updatedAt: unknown | null;
        readonly deletedAt: unknown | null;
        readonly title: string;
        readonly uid: unknown;
        readonly authors: ReadonlyArray<{
            readonly displayName: string;
        }>;
        readonly genres: ReadonlyArray<{
            readonly name: string;
        }>;
        readonly ratings: ReadonlyArray<number>;
    } | null;
};
export type MangaQuery = {
    readonly response: MangaQueryResponse;
    readonly variables: MangaQueryVariables;
};



/*
query MangaQuery(
  $uid: UniqueId!
) {
  manga(uid: $uid) {
    advisory
    competing
    createdAt
    updatedAt
    deletedAt
    title
    uid
    authors {
      displayName
    }
    genres {
      name
    }
    ratings
  }
}
*/

const node: ConcreteRequest = (function(){
var v0 = [
  {
    "defaultValue": null,
    "kind": "LocalArgument",
    "name": "uid"
  }
],
v1 = [
  {
    "alias": null,
    "args": [
      {
        "kind": "Variable",
        "name": "uid",
        "variableName": "uid"
      }
    ],
    "concreteType": "Manga",
    "kind": "LinkedField",
    "name": "manga",
    "plural": false,
    "selections": [
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "advisory",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "competing",
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
        "name": "deletedAt",
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
        "name": "uid",
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "concreteType": "Profile",
        "kind": "LinkedField",
        "name": "authors",
        "plural": true,
        "selections": [
          {
            "alias": null,
            "args": null,
            "kind": "ScalarField",
            "name": "displayName",
            "storageKey": null
          }
        ],
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "concreteType": "Genre",
        "kind": "LinkedField",
        "name": "genres",
        "plural": true,
        "selections": [
          {
            "alias": null,
            "args": null,
            "kind": "ScalarField",
            "name": "name",
            "storageKey": null
          }
        ],
        "storageKey": null
      },
      {
        "alias": null,
        "args": null,
        "kind": "ScalarField",
        "name": "ratings",
        "storageKey": null
      }
    ],
    "storageKey": null
  }
];
return {
  "fragment": {
    "argumentDefinitions": (v0/*: any*/),
    "kind": "Fragment",
    "metadata": null,
    "name": "MangaQuery",
    "selections": (v1/*: any*/),
    "type": "Query",
    "abstractKey": null
  },
  "kind": "Request",
  "operation": {
    "argumentDefinitions": (v0/*: any*/),
    "kind": "Operation",
    "name": "MangaQuery",
    "selections": (v1/*: any*/)
  },
  "params": {
    "cacheID": "76da513f061af59a53b015aacd790aed",
    "id": null,
    "metadata": {},
    "name": "MangaQuery",
    "operationKind": "query",
    "text": "query MangaQuery(\n  $uid: UniqueId!\n) {\n  manga(uid: $uid) {\n    advisory\n    competing\n    createdAt\n    updatedAt\n    deletedAt\n    title\n    uid\n    authors {\n      displayName\n    }\n    genres {\n      name\n    }\n    ratings\n  }\n}\n"
  }
};
})();
(node as any).hash = '16c851ec25f3dddf96f174f85fe0e766';
export default node;
