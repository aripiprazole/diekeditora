import React from 'react';

// scroll bar
import 'simplebar/src/simplebar.css';

import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { HelmetProvider } from 'react-helmet-async';
import { Provider } from 'react-redux';
import { ApolloProvider } from '@apollo/client';
import { PersistGate } from 'redux-persist/integration/react';
import { graphQLClient, makeStore, store, persistor } from '@diekeditora/store';

//
import App from './App';
import * as serviceWorker from './serviceWorker';
import reportWebVitals from './reportWebVitals';

//

// ----------------------------------------------------------------------

makeStore();

ReactDOM.render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <ApolloProvider client={graphQLClient}>
        <HelmetProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </HelmetProvider>
      </ApolloProvider>
    </PersistGate>
  </Provider>,
  document.getElementById('root')
);

// If you want to enable client cache, register instead.
serviceWorker.unregister();

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
