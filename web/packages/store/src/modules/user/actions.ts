/* eslint-disable no-unused-vars */
import {CompleteUser, Connection, User} from '@diekeditora/store/entities';

export type UserAction =
  | FetchUserConnectionAction
  | FetchUserConnectionSuccessAction
  | FetchUserAction
  | FetchUserSuccessAction
  | FetchUserConnectionAction
  | FetchUserFailAction;

export type FetchUserConnectionAction = {
  type: UserActions.FETCH_USER_CONNECTION;
  payload: { after?: string; first: number };
};

export type FetchUserAction = {
  type: UserActions.FETCH_USER;
  payload: { username: string };
};

export type FetchUserSuccessAction = {
  type: UserActions.FETCH_USER_SUCCESS;
  payload: { user: CompleteUser };
};

export type FetchUserConnectionSuccessAction = {
  type: UserActions.FETCH_USER_CONNECTION_SUCCESS;
  payload: { connection: Connection<User> };
};

export type FetchUserFailAction = {
  type: UserActions.FETCH_USER_FAIL;
  payload: { error: Error };
};

export enum UserActions {
  FETCH_USER_CONNECTION = '@user/FETCH_USER_CONNECTION',
  FETCH_USER_CONNECTION_SUCCESS = '@user/FETCH_USER_CONNECTION_SUCCESS',
  FETCH_USER = '@user/FETCH_USER',
  FETCH_USER_SUCCESS = '@user/FETCH_USER_SUCCESS',
  FETCH_USER_FAIL = '@user/FETCH_USER_FAIL',
}

export const fetchUserConnection = (first: number, after?: string): FetchUserConnectionAction => ({
  type: UserActions.FETCH_USER_CONNECTION,
  payload: {first, after},
});

export const fetchUserSuccess = (user: CompleteUser): FetchUserSuccessAction => ({
  type: UserActions.FETCH_USER_SUCCESS,
  payload: {user},
});

export const fetchUserConnectionSuccess = (connection: Connection<User>): FetchUserConnectionSuccessAction => ({
  type: UserActions.FETCH_USER_CONNECTION_SUCCESS,
  payload: {connection},
});

export const fetchUserFail = (error: Error): FetchUserFailAction => ({
  type: UserActions.FETCH_USER_FAIL,
  payload: {error},
});
