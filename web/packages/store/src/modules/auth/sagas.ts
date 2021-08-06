import {all, put, takeEvery} from 'redux-saga/effects';

import { todo } from '@diekeditora/store/utils';

import {AuthActions, LoginAction, loginFail, loginSuccess} from './actions';


/** Login saga */
function* loginSaga(login: LoginAction) {
  try {
    yield put(loginSuccess(todo()));
  } catch (error) {
    yield put(loginFail(error));
  }
}

export const authSagas = all([takeEvery(AuthActions.LOGIN, loginSaga)]);
