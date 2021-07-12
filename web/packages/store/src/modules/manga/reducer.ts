import produce from 'immer';

import {CompleteManga, Connection, Manga} from '~/entities';

import {MangaAction, MangaActions} from './actions';

export type MangaState = {
  manga: CompleteManga | null;
  connection: Connection<Manga> | null;
  error: Error | null;
  loading: boolean;
};

const defaultState: MangaState = {
  manga: null,
  connection: null,
  error: null,
  loading: false,
};

export const mangaReducer = (state = defaultState, action: MangaAction) =>
  produce(state, (draft) => {
    switch (action.type) {
      case MangaActions.FETCH_MANGA:
      case MangaActions.FETCH_MANGA_CONNECTION:
        draft.manga = null;
        draft.loading = true;
        draft.connection = null;
        draft.error = null;
        break;

      case MangaActions.FETCH_MANGA_SUCCESS:
        draft.manga = action.payload.manga;
        draft.loading = false;
        draft.connection = null;
        draft.error = null;
        break;

      case MangaActions.FETCH_MANGA_CONNECTION_SUCCESS:
        draft.manga = null;
        draft.loading = false;
        draft.connection = action.payload.connection;
        draft.error = null;
        break;

      case MangaActions.FETCH_MANGA_FAIL:
        draft.manga = null;
        draft.loading = false;
        draft.connection = null;
        draft.error = action.payload.error;
        break;
    }
  });
