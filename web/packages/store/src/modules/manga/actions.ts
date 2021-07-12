/* eslint-disable no-unused-vars */
import {Connection, Manga} from '~/entities';

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
  payload: { manga: Manga };
};

export type FetchMangaConnectionSuccessAction = {
  type: MangaActions.FETCH_MANGA_CONNECTION_SUCCESS;
  payload: { connection: Connection<Manga> };
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

export const fetchMangaSuccess = (manga: Manga): FetchMangaSuccessAction => ({
  type: MangaActions.FETCH_MANGA_SUCCESS,
  payload: {manga},
});

export const fetchMangaConnectionSuccess = (connection: Connection<Manga>): FetchMangaConnectionSuccessAction => ({
  type: MangaActions.FETCH_MANGA_CONNECTION_SUCCESS,
  payload: {connection},
});

export const fetchMangaFail = (error: Error): FetchMangaFailAction => ({
  type: MangaActions.FETCH_MANGA_FAIL,
  payload: {error},
});
