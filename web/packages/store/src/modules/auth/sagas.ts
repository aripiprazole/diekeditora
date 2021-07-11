import {all, call, put} from 'redux-saga/effects';
import {graphQLClient} from '~/client';
import {User} from '~/entities';
import {ME_USER_QUERY} from '~/queries';

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

export const authSagas = all([AuthActions.LOGIN, loginSaga]);
