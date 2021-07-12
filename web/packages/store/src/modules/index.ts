import {combineReducers} from 'redux';
import {persistReducer} from 'redux-persist';

import {authReducer, authSagas} from './auth';
import {userReducer, userSagas} from './user';
import {mangaReducer, mangaSagas} from './manga';

import storage from 'redux-persist/lib/storage';

import {AuthTransformer} from './auth/transforms';

const config = (key: string, transforms: any[] = []) => ({
  key,
  storage,
  transforms,
});

export const rootReducer = combineReducers({
  user: userReducer,
  manga: mangaReducer,
  auth: persistReducer(config('auth', [AuthTransformer]), authReducer),
});

/** Root saga */
export function* rootSaga() {
  yield authSagas;
  yield userSagas;
  yield mangaSagas;
}
