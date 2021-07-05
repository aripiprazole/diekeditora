/**
 * Connection entity
 */
export class Connection<T> {
  /**
   * Creates connection instance
   *
   * @param edges
   * @param pageInfo
   */
  public constructor(public readonly edges: Edge<T>[], public readonly pageInfo: PageInfo) {}
}

/**
 * Edge instance
 */
export class Edge<T> {
  /**
   * Creates edge instance
   *
   * @param node
   * @param cursor
   */
  public constructor(public readonly node: T, public readonly cursor: string) {}
}

/**
 * Page info entity
 */
export class PageInfo {
  /**
   * Creates page info instance
   * @param startCursor
   * @param endCursor
   * @param hasNextPage
   * @param hasPreviousPage
   */
  public constructor(
    public readonly startCursor: string,
    public readonly endCursor: string,
    public readonly hasNextPage: boolean,
    public readonly hasPreviousPage: boolean,
  ) {}
}
