import produce from 'immer';

import {Connection, Manga} from '~/entities';
import {MangaAction, MangaActions} from './actions';

export type MangaState = {
  connection: Connection<Manga> | null;
  error: Error | null;
  loading: boolean;
};

const defaultState: MangaState = {
  connection: null,
  error: null,
  loading: false,
};

export const userReducer = (state = defaultState, action: MangaAction) =>
  produce(state, (draft) => {
    switch (action.type) {
      case MangaActions.FETCH_MANGAS:
        draft.loading = true;
        draft.connection = null;
        draft.error = null;
        break;

      case MangaActions.FETCH_MANGAS_SUCCESS:
        draft.loading = false;
        draft.connection = action.payload.connection;
        draft.error = null;
        break;

      case MangaActions.FETCH_MANGAS_FAIL:
        draft.loading = false;
        draft.connection = null;
        draft.error = action.payload.error;
        break;
    }
  });
