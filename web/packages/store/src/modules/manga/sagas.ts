import {all, call, put, takeEvery} from 'redux-saga/effects';

import {graphQLClient} from '@diekeditora/store/client';
import {CompleteManga, Connection, Manga} from '@diekeditora/store/entities';
import {MANGA_CONNECTION_QUERY, MANGA_QUERY} from '@diekeditora/store/queries';

import {
  FetchMangaConnectionAction,
  fetchMangaFail,
  fetchMangaConnectionSuccess,
  MangaActions,
  FetchMangaAction,
  fetchMangaSuccess,
} from './actions';

type FetchMangaConnectionResponse = {
  mangas: Connection<Manga>;
};

type FetchMangaResponse = {
  manga: CompleteManga;
};

/** Fetch manga connection saga */
function* fetchMangaConnectionSaga(action: FetchMangaConnectionAction) {
  try {
    const response: FetchMangaConnectionResponse = yield call(() =>
      graphQLClient.query({
        query: MANGA_CONNECTION_QUERY,
        variables: action.payload,
      }),
    );

    yield put(fetchMangaConnectionSuccess(response.mangas));
  } catch (error) {
    yield put(fetchMangaFail(error));
  }
}

/** Fetch manga saga */
function* fetchMangaSaga(action: FetchMangaAction) {
  try {
    const response: FetchMangaResponse = yield call(() =>
      graphQLClient.query({
        query: MANGA_QUERY,
        variables: action.payload,
      }),
    );

    yield put(fetchMangaSuccess(response.manga));
  } catch (error) {
    yield put(fetchMangaFail(error));
  }
}

export const mangaSagas = all([
  takeEvery(MangaActions.FETCH_MANGA_CONNECTION, fetchMangaConnectionSaga),
  takeEvery(MangaActions.FETCH_MANGA, fetchMangaSaga),
]);
