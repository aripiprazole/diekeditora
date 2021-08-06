/* eslint-disable no-unused-vars */
export type MangaAction =
  | FetchMangaConnectionAction
  | FetchMangaAction
  | FetchMangaSuccessAction
  | FetchMangaConnectionSuccessAction
  | FetchMangaFailAction;

export type FetchMangaAction = {
  type: MangaActions.FETCH_MANGA;
  payload: { title: string };
};

export type FetchMangaConnectionAction = {
  type: MangaActions.FETCH_MANGA_CONNECTION;
  payload: { after?: string; first: number };
};

export type FetchMangaSuccessAction = {
  type: MangaActions.FETCH_MANGA_SUCCESS;
  payload: { manga: {} };
};

export type FetchMangaConnectionSuccessAction = {
  type: MangaActions.FETCH_MANGA_CONNECTION_SUCCESS;
  payload: { connection: {} };
};

export type FetchMangaFailAction = {
  type: MangaActions.FETCH_MANGA_FAIL;
  payload: { error: Error };
};

export enum MangaActions {
  FETCH_MANGA_CONNECTION = '@manga/FETCH_MANGA_CONNECTION',
  FETCH_MANGA_CONNECTION_SUCCESS = '@manga/FETCH_MANGA_CONNECTION_SUCCESS',
  FETCH_MANGA = '@manga/FETCH_MANGA',
  FETCH_MANGA_SUCCESS = '@manga/FETCH_MANGA_SUCCESS',
  FETCH_MANGA_FAIL = '@manga/FETCH_MANGA_FAIL',
}

export const fetchMangaConnection = (first: number, after?: string): FetchMangaConnectionAction => ({
  type: MangaActions.FETCH_MANGA_CONNECTION,
  payload: {first, after},
});

export const fetchManga = (title: string): FetchMangaAction => ({
  type: MangaActions.FETCH_MANGA,
  payload: {title},
});

export const fetchMangaSuccess = (manga: {}): FetchMangaSuccessAction => ({
  type: MangaActions.FETCH_MANGA_SUCCESS,
  payload: {manga},
});

export const fetchMangaConnectionSuccess = (connection: {}): FetchMangaConnectionSuccessAction => ({
  type: MangaActions.FETCH_MANGA_CONNECTION_SUCCESS,
  payload: {connection},
});

export const fetchMangaFail = (error: Error): FetchMangaFailAction => ({
  type: MangaActions.FETCH_MANGA_FAIL,
  payload: {error},
});
