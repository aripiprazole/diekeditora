import firebase from 'firebase';
import produce from 'immer';

import {User} from '@diekeditora/store/entities';

import {AuthAction, AuthActions} from './actions';

export type AuthState = {
  token: firebase.User | null;
  user: User | null;
  error: Error | null;
  loading: boolean;
};

const defaultState: AuthState = {
  error: null,
  loading: false,
  user: null,
  token: null,
};

export const authReducer = (state = defaultState, action: AuthAction) =>
  produce(state, (draft) => {
    switch (action.type) {
      case AuthActions.LOGIN:
        draft.token = action.payload.token;
        draft.loading = true;
        draft.error = null;
        draft.user = null;
        break;

      case AuthActions.LOGIN_FAIL:
        draft.error = action.payload.error;
        draft.loading = false;
        draft.token = null;
        draft.user = null;
        break;

      case AuthActions.LOGIN_SUCCESS:
        draft.user = action.payload.user;
        draft.loading = false;
        draft.error = null;
        break;

      case AuthActions.LOGOUT:
        draft.token = null;
        draft.user = null;
        draft.loading = false;
        draft.error = null;
        break;

      default:
        break;
    }
  });
