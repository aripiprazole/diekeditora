/* eslint-disable no-unused-vars */
import {Connection, Manga} from '~/entities';

export type MangaAction = FetchMangasAction | FetchMangasSuccessAction | FetchMangasFailAction;

export type FetchMangasAction = {
  type: MangaActions.FETCH_MANGAS;
  payload: { after?: string; first: number };
};

export type FetchMangasSuccessAction = {
  type: MangaActions.FETCH_MANGAS_SUCCESS;
  payload: { connection: Connection<Manga> };
};

export type FetchMangasFailAction = {
  type: MangaActions.FETCH_MANGAS_FAIL;
  payload: { error: Error };
};

export enum MangaActions {
  FETCH_MANGAS = '@manga/FETCH_MANGAS',
  FETCH_MANGAS_SUCCESS = '@manga/FETCH_MANGAS_SUCCESS',
  FETCH_MANGAS_FAIL = '@manga/FETCH_MANGAS_FAIL',
}

export const fetchMangas = (first: number, after?: string): FetchMangasAction => ({
  type: MangaActions.FETCH_MANGAS,
  payload: {first, after},
});

export const fetchMangasSuccess = (connection: Connection<Manga>): FetchMangasSuccessAction => ({
  type: MangaActions.FETCH_MANGAS_SUCCESS,
  payload: {connection},
});

export const fetchMangasFail = (error: Error): FetchMangasFailAction => ({
  type: MangaActions.FETCH_MANGAS_FAIL,
  payload: {error},
});
