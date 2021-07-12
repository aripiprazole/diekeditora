import {all, call, put, takeEvery} from 'redux-saga/effects';
import {graphQLClient} from '~/client';

import {Connection, User} from '~/entities';
import {USERS_QUERY} from '~/queries';
import {FetchUsersAction, fetchUsersFail, fetchUsersSuccess, UserActions} from './actions';

type FetchUsersResponse = {
  users: Connection<User>;
};

/** Fetch users saga */
function* fetchUsersSaga(action: FetchUsersAction) {
  try {
    const response: FetchUsersResponse = yield call(() =>
      graphQLClient.query({
        query: USERS_QUERY,
        variables: action.payload,
      }),
    );

    yield put(fetchUsersSuccess(response.users));
  } catch (error) {
    yield put(fetchUsersFail(error));
  }
}

export const userSagas = all([takeEvery(UserActions.FETCH_USERS, fetchUsersSaga)]);
