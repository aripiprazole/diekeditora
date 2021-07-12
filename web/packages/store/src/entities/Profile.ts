import {User} from './User';

export type Gender = 'Male' | 'Female' | 'NonBinary';

/**
 * Profile entity
 */
export type Profile = {
  readonly displayName: string;
  readonly gender: Gender;
  readonly createdAt: Date;
  readonly updatedAt: Date | null;
};

/**
 * Complete profile entity
 */
export type CompleteProfile = {
  readonly displayName: string;
  readonly gender: Gender;
  readonly user: User;
  readonly createdAt: Date;
  readonly updatedAt: Date | null;
};
