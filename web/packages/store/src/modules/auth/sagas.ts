import {all, call, put, takeEvery} from 'redux-saga/effects';

import {graphQLClient} from '@diekeditora/store/client';
import {User} from '@diekeditora/store/entities';
import {ME_USER_QUERY} from '@diekeditora/store/queries';

import {AuthActions, LoginAction, loginFail, loginSuccess} from './actions';

type LoginResponse = {
  user: User;
};

/** Login saga */
function* loginSaga(login: LoginAction) {
  try {
    const response: LoginResponse = yield call(() =>
      graphQLClient.query({
        query: ME_USER_QUERY,
      }),
    );

    yield put(loginSuccess(response.user));
  } catch (error) {
    yield put(loginFail(error));
  }
}

export const authSagas = all([takeEvery(AuthActions.LOGIN, loginSaga)]);
