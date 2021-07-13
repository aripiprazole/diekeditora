import produce from 'immer';

import {CompleteUser, Connection, User} from '@diekeditora/store/entities';

import {UserAction, UserActions} from './actions';

export type UserState = {
  user: CompleteUser | null;
  connection: Connection<User> | null;
  error: Error | null;
  loading: boolean;
};

const defaultState: UserState = {
  user: null,
  connection: null,
  error: null,
  loading: false,
};

export const userReducer = (state = defaultState, action: UserAction) =>
  produce(state, (draft) => {
    switch (action.type) {
      case UserActions.FETCH_USER:
      case UserActions.FETCH_USER_CONNECTION:
        draft.user = null;
        draft.loading = true;
        draft.connection = null;
        draft.error = null;
        break;

      case UserActions.FETCH_USER_SUCCESS:
        draft.user = action.payload.user;
        draft.loading = false;
        draft.connection = null;
        draft.error = null;
        break;

      case UserActions.FETCH_USER_CONNECTION_SUCCESS:
        draft.user = null;
        draft.loading = false;
        draft.connection = action.payload.connection;
        draft.error = null;
        break;

      case UserActions.FETCH_USER_FAIL:
        draft.user = null;
        draft.loading = false;
        draft.connection = null;
        draft.error = action.payload.error;
        break;
    }
  });
