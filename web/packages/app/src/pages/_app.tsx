import React, {ComponentType} from 'react';

import {AppContext, AppProps} from 'next/app';
import {RelayEnvironmentProvider} from 'react-relay';

import {ChakraProvider, cookieStorageManager} from '@chakra-ui/react';

import {getRelayEnvironment} from '@diekeditora/graphql';
import {GlobalStyles, http, initAuth, theme} from '@diekeditora/app';

// Inits the firebase authentication
initAuth();

type MyAppProps = { cookie?: string };

type MyAppType = ComponentType<AppProps & MyAppProps> & {
  getInitialProps?(context: AppContext): MyAppProps
}

const MyApp: MyAppType = ({Component, pageProps, cookie}) => {
  return (
    <RelayEnvironmentProvider environment={getRelayEnvironment(http)}>
      <ChakraProvider
        theme={theme}
        colorModeManager={cookieStorageManager(cookie ?? document.cookie)}
      >
        <GlobalStyles />

        <Component {...pageProps} />
      </ChakraProvider>
    </RelayEnvironmentProvider>
  );
};

MyApp.getInitialProps = ({ctx}) => ({
  cookie: ctx.req.headers.cookie,
});

export default MyApp;
