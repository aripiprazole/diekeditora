import React from 'react';

import {AppProps} from 'next/app';
import {RelayEnvironmentProvider} from 'react-relay';

import {Global} from '@emotion/react';

import {getRelayEnvironment} from '@diekeditora/graphql';
import {ThemeProvider, initAuth, http, globalStyles} from '@diekeditora/app';

// Inits the firebase authentication
initAuth();

const MyApp: React.VFC<AppProps> = ({Component, pageProps}) => {
  return (
    <RelayEnvironmentProvider environment={getRelayEnvironment(http)}>
      <ThemeProvider>
        <Global styles={globalStyles} />

        <Component {...pageProps} />
      </ThemeProvider>
    </RelayEnvironmentProvider>
  );
};

export default MyApp;
