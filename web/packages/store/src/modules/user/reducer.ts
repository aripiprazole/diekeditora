import produce from 'immer';

import {Connection, User} from '~/entities';
import {UserAction, UserActions} from './actions';

export type UserState = {
  connection: Connection<User> | null;
  error: Error | null;
  loading: boolean;
};

const defaultState: UserState = {
  connection: null,
  error: null,
  loading: false,
};

export const userReducer = (state = defaultState, action: UserAction) =>
  produce(state, (draft) => {
    switch (action.type) {
      case UserActions.FETCH_USERS:
        draft.loading = true;
        draft.connection = null;
        draft.error = null;
        break;

      case UserActions.FETCH_USERS_SUCCESS:
        draft.loading = false;
        draft.connection = action.payload.connection;
        draft.error = null;
        break;

      case UserActions.FETCH_USERS_FAIL:
        draft.loading = false;
        draft.connection = null;
        draft.error = action.payload.error;
        break;
    }
  });
