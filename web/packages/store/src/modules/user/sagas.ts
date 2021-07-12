import {all, call, put, takeEvery} from 'redux-saga/effects';
import {graphQLClient} from '~/client';

import {Connection, User} from '~/entities';
import {USERS_QUERY} from '~/queries';
import {FetchUserConnectionAction, fetchUserFail, fetchUserSuccess, UserActions} from './actions';

type FetchUserConnectionResponse = {
  users: Connection<User>;
};

/** Fetch users saga */
function* fetchUserConnectionSaga(action: FetchUserConnectionAction) {
  try {
    const response: FetchUserConnectionResponse = yield call(() =>
      graphQLClient.query({
        query: USERS_QUERY,
        variables: action.payload,
      }),
    );

    yield put(fetchUserSuccess(response.users));
  } catch (error) {
    yield put(fetchUserFail(error));
  }
}

export const userSagas = all([takeEvery(UserActions.FETCH_USER_CONNECTION, fetchUserConnectionSaga)]);
