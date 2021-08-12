import React from 'react';

import {AppProps} from 'next/app';
import {RelayEnvironmentProvider} from 'react-relay';

import {Global} from '@emotion/react';

import {getRelayEnvironment} from '@diekeditora/store';
import {ThemeProvider, http, globalStyles} from '@diekeditora/app';

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
