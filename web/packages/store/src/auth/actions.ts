/* eslint-disable no-unused-vars */
import firebase from 'firebase';
import {User} from '~/entities';

export type AuthAction = LoginAction | LoginSuccess | LoginFail | LogoutAction;

export type LoginAction = {
  type: AuthActions.LOGIN;
  payload: { token: firebase.User };
};

export type LoginFail = {
  type: AuthActions.LOGIN_FAIL;
  payload: { error: Error };
};

export type LoginSuccess = {
  type: AuthActions.LOGIN_SUCCESS;
  payload: { user: User };
};

export type LogoutAction = {
  type: AuthActions.LOGOUT;
};

export enum AuthActions {
  LOGIN = '@auth/LOGIN',
  LOGIN_SUCCESS = '@auth/LOGIN_SUCCESS',
  LOGIN_FAIL = '@auth/LOGIN_FAIL',
  LOGOUT = '@auth/LOGOUT',
}

export const login = (token: firebase.User): LoginAction => ({
  type: AuthActions.LOGIN,
  payload: {token},
});

export const loginSuccess = (user: User): LoginSuccess => ({
  type: AuthActions.LOGIN_SUCCESS,
  payload: {user},
});

export const loginFail = (error: Error): LoginFail => ({
  type: AuthActions.LOGIN_FAIL,
  payload: {error},
});
