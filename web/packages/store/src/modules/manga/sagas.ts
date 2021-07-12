import {all, call, put, takeEvery} from 'redux-saga/effects';
import {graphQLClient} from '~/client';

import {Connection, Manga} from '~/entities';
import {USERS_QUERY} from '~/queries';
import {FetchMangasAction, fetchMangasFail, fetchMangasSuccess, MangaActions} from './actions';

type FetchMangasResponse = {
  mangas: Connection<Manga>;
};

/** Fetch users saga */
function* fetchUsersSaga(action: FetchMangasAction) {
  try {
    const response: FetchMangasResponse = yield call(() =>
      graphQLClient.query({
        query: USERS_QUERY,
        variables: action.payload,
      }),
    );

    yield put(fetchMangasSuccess(response.mangas));
  } catch (error) {
    yield put(fetchMangasFail(error));
  }
}

export const mangaSagas = all([takeEvery(MangaActions.FETCH_MANGAS, fetchUsersSaga)]);
