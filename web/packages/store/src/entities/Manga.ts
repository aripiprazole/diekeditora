export type AgeAdvisory = 'Free' | 'Ten' | 'Twelve' | 'Fourteen' | 'Sixteen' | 'Eighteen';

/**
 * Manga entity
 */
export class Manga {
  /**
   * Creates manga instance
   *
   * @param title
   * @param competing
   * @param summary
   * @param advisory
   * @param createdAt
   * @param updatedAt
   * @param deletedAt
   */
  public constructor(
    public readonly title: string,
    public readonly competing: boolean,
    public readonly summary: string,
    public readonly advisory: AgeAdvisory,
    public readonly createdAt: Date,
    public readonly updatedAt?: Date,
    public readonly deletedAt?: Date,
  ) {}
}
