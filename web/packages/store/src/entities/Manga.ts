export type AgeAdvisory = 'Free' | 'Ten' | 'Twelve' | 'Fourteen' | 'Sixteen' | 'Eighteen';

/**
 * Manga entity
 */
export type Manga = {
  readonly title: string;
  readonly competing: boolean;
  readonly summary: string;
  readonly advisory: AgeAdvisory;
  readonly createdAt: Date;
  readonly updatedAt?: Date;
  readonly deletedAt?: Date;
};
