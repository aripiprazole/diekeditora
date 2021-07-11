import {createStore, applyMiddleware, State, Store} from 'redux';

import createSagaMiddleware, {Task} from 'redux-saga';
import {persistStore, Persistor} from 'redux-persist';

import {logger} from './middlewares';
import {rootReducer, rootSaga} from './modules';

import {AuthState} from './modules/auth';

export let persistor: Persistor;
export let store: Store;

export const makeStore = () => {
  const sagaMiddleware = createSagaMiddleware();
  const sagaStore = createStore(rootReducer, applyMiddleware(sagaMiddleware, logger));

  sagaStore.sagaTask = sagaMiddleware.run(rootSaga);

  store = sagaStore;
  persistor = persistStore(sagaStore);

  return sagaStore;
};

export type HydrateAction = {
  type: '__NEXT_REDUX_WRAPPER_HYDRATE__';
  payload: State;
};

declare module 'react-redux' {
  export interface DefaultRootState extends State {}
}

declare module 'redux' {
  export interface State {
    auth: AuthState;
  }

  export interface Store {
    sagaTask: Task;
  }
}
