import {Profile} from './Profile';

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

/**
 * Complete user entity
 */
export type CompleteUser = {
  readonly name: string;
  readonly username: string;
  readonly email: string;
  readonly birthday: Date;
  readonly createdAt: Date;
  readonly updatedAt: Date | null;
  readonly deletedAt: Date | null;
  readonly profile: Profile;
};
