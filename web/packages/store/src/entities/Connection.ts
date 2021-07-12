/**
 * Connection entity
 */
export type Connection<T> = {
  readonly edges: Edge<T>[];
  readonly pageInfo: PageInfo;
};

/**
 * Edge instance
 */
export type Edge<T> = {
  readonly node: T;
  readonly cursor: string;
};

/**
 * Page info entity
 */
export type PageInfo = {
  readonly startCursor: string;
  readonly endCursor: string;
  readonly hasNextPage: boolean;
  readonly hasPreviousPage: boolean;
};
