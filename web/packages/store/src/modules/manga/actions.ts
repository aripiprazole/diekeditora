/* eslint-disable no-unused-vars */
import {Connection, Manga} from '~/entities';

export type MangaAction = FetchMangaConnectionAction | FetchMangaSuccessAction | FetchMangaFailAction;

export type FetchMangaConnectionAction = {
  type: MangaActions.FETCH_MANGA_CONNECTION;
  payload: { after?: string; first: number };
};

export type FetchMangaSuccessAction = {
  type: MangaActions.FETCH_MANGA_SUCCESS;
  payload: { connection: Connection<Manga> };
};

export type FetchMangaFailAction = {
  type: MangaActions.FETCH_MANGA_FAIL;
  payload: { error: Error };
};

export enum MangaActions {
  FETCH_MANGA_CONNECTION = '@manga/FETCH_MANGA_CONNECTION',
  FETCH_MANGA_SUCCESS = '@manga/FETCH_MANGA_SUCCESS',
  FETCH_MANGA_FAIL = '@manga/FETCH_MANGA_FAIL',
}

export const fetchMangas = (first: number, after?: string): FetchMangaConnectionAction => ({
  type: MangaActions.FETCH_MANGA_CONNECTION,
  payload: {first, after},
});

export const fetchMangasSuccess = (connection: Connection<Manga>): FetchMangaSuccessAction => ({
  type: MangaActions.FETCH_MANGA_SUCCESS,
  payload: {connection},
});

export const fetchMangasFail = (error: Error): FetchMangaFailAction => ({
  type: MangaActions.FETCH_MANGA_FAIL,
  payload: {error},
});
