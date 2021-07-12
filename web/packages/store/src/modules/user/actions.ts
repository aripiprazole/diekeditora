/* eslint-disable no-unused-vars */
import {Connection, User} from '~/entities';

export type UserAction = FetchUserConnectionAction | FetchUserSuccessAction | FetchUserFailAction;

export type FetchUserConnectionAction = {
  type: UserActions.FETCH_USER_CONNECTION;
  payload: { after?: string; first: number };
};

export type FetchUserSuccessAction = {
  type: UserActions.FETCH_USER_SUCCESS;
  payload: { connection: Connection<User> };
};

export type FetchUserFailAction = {
  type: UserActions.FETCH_USER_FAIL;
  payload: { error: Error };
};

export enum UserActions {
  FETCH_USER_CONNECTION = '@user/FETCH_USER_CONNECTION',
  FETCH_USER_SUCCESS = '@user/FETCH_USER_SUCCESS',
  FETCH_USER_FAIL = '@user/FETCH_USER_FAIL',
}

export const fetchUserConnection = (first: number, after?: string): FetchUserConnectionAction => ({
  type: UserActions.FETCH_USER_CONNECTION,
  payload: {first, after},
});

export const fetchUserSuccess = (connection: Connection<User>): FetchUserSuccessAction => ({
  type: UserActions.FETCH_USER_SUCCESS,
  payload: {connection},
});

export const fetchUserFail = (error: Error): FetchUserFailAction => ({
  type: UserActions.FETCH_USER_FAIL,
  payload: {error},
});
