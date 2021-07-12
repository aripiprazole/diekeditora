/* eslint-disable no-unused-vars */
import {Connection, User} from '~/entities';

export type UserAction = FetchUsersAction | FetchUsersSuccessAction | FetchUsersFailAction;

export type FetchUsersAction = {
  type: UserActions.FETCH_USERS;
  payload: { after?: string; first: number };
};

export type FetchUsersSuccessAction = {
  type: UserActions.FETCH_USERS_SUCCESS;
  payload: { connection: Connection<User> };
};

export type FetchUsersFailAction = {
  type: UserActions.FETCH_USERS_FAIL;
  payload: { error: Error };
};

export enum UserActions {
  FETCH_USERS = '@user/FETCH_USERS',
  FETCH_USERS_SUCCESS = '@user/FETCH_USERS_SUCCESS',
  FETCH_USERS_FAIL = '@user/FETCH_USERS_FAIL',
}

export const fetchUsers = (first: number, after?: string): FetchUsersAction => ({
  type: UserActions.FETCH_USERS,
  payload: {first, after},
});

export const fetchUsersSuccess = (connection: Connection<User>): FetchUsersSuccessAction => ({
  type: UserActions.FETCH_USERS_SUCCESS,
  payload: {connection},
});

export const fetchUsersFail = (error: Error): FetchUsersFailAction => ({
  type: UserActions.FETCH_USERS_FAIL,
  payload: {error},
});
