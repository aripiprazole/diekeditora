import {all, put, takeEvery} from 'redux-saga/effects';

import { todo } from '@diekeditora/store/utils';

import {
  FetchMangaConnectionAction,
  fetchMangaFail,
  fetchMangaConnectionSuccess,
  MangaActions,
  FetchMangaAction,
  fetchMangaSuccess,
} from './actions';

/** Fetch manga connection saga */
function* fetchMangaConnectionSaga(action: FetchMangaConnectionAction) {
  try {
    yield put(fetchMangaConnectionSuccess(todo()));
  } catch (error) {
    yield put(fetchMangaFail(error));
  }
}

/** Fetch manga saga */
function* fetchMangaSaga(action: FetchMangaAction) {
  try {
    yield put(fetchMangaSuccess(todo()));
  } catch (error) {
    yield put(fetchMangaFail(error));
  }
}

export const mangaSagas = all([
  takeEvery(MangaActions.FETCH_MANGA_CONNECTION, fetchMangaConnectionSaga),
  takeEvery(MangaActions.FETCH_MANGA, fetchMangaSaga),
]);
