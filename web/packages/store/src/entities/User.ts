/**
 * User entity
 */
export type User = {
  readonly name: string;
  readonly username: string;
  readonly email: string;
  readonly birthday: Date;
  readonly createdAt: Date;
  readonly updatedAt: Date | null;
  readonly deletedAt: Date | null;
};
