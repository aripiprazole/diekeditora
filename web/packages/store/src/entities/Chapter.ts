/**
 * Chapter entity
 */
export type Chapter = {
  readonly title: string;
  readonly number: number;
  readonly pages: number;
  readonly enabled: boolean;
  readonly createdAt: Date;
  readonly updatedAt: Date | null;
};
