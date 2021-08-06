import {all, put, takeEvery} from 'redux-saga/effects';

import { todo } from '@diekeditora/store/utils';

import {
  FetchUserConnectionAction,
  fetchUserFail,
  fetchUserConnectionSuccess,
  UserActions,
  FetchUserAction,
  fetchUserSuccess,
} from './actions';

/** Fetch user connection saga */
function* fetchUserConnectionSaga(action: FetchUserConnectionAction) {
  try {
    yield put(fetchUserConnectionSuccess(todo()));
  } catch (error) {
    yield put(fetchUserFail(error));
  }
}

/** Fetch user saga */
function* fetchUserSaga(action: FetchUserAction) {
  try {
    yield put(fetchUserSuccess(todo()));
  } catch (error) {
    yield put(fetchUserFail(error));
  }
}

export const userSagas = all([
  takeEvery(UserActions.FETCH_USER_CONNECTION, fetchUserConnectionSaga),
  takeEvery(UserActions.FETCH_USER, fetchUserSaga),
]);
