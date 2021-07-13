import {all, call, put, takeEvery} from 'redux-saga/effects';

import {graphQLClient} from '@diekeditora/store/client';
import {CompleteUser, Connection, User} from '@diekeditora/store/entities';
import {USER_CONNECTION_QUERY} from '@diekeditora/store/queries';

import {
  FetchUserConnectionAction,
  fetchUserFail,
  fetchUserConnectionSuccess,
  UserActions,
  FetchUserAction,
  fetchUserSuccess,
} from './actions';

type FetchUserConnectionResponse = {
  users: Connection<User>;
};

type FetchUserResponse = {
  user: CompleteUser;
};

/** Fetch user connection saga */
function* fetchUserConnectionSaga(action: FetchUserConnectionAction) {
  try {
    const response: FetchUserConnectionResponse = yield call(() =>
      graphQLClient.query({
        query: USER_CONNECTION_QUERY,
        variables: action.payload,
      }),
    );

    yield put(fetchUserConnectionSuccess(response.users));
  } catch (error) {
    yield put(fetchUserFail(error));
  }
}

/** Fetch user saga */
function* fetchUserSaga(action: FetchUserAction) {
  try {
    const response: FetchUserResponse = yield call(() =>
      graphQLClient.query({
        query: USER_CONNECTION_QUERY,
        variables: action.payload,
      }),
    );

    yield put(fetchUserSuccess(response.user));
  } catch (error) {
    yield put(fetchUserFail(error));
  }
}

export const userSagas = all([
  takeEvery(UserActions.FETCH_USER_CONNECTION, fetchUserConnectionSaga),
  takeEvery(UserActions.FETCH_USER, fetchUserSaga),
]);
