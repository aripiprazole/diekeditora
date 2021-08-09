import React from 'react';

import {Global} from '@emotion/react';
import {ChakraProvider} from '@chakra-ui/react';

import {AppProps} from 'next/app';

import {globalStyles} from '@diekeditora/app/src/styles';

const MyApp: React.VFC<AppProps> = ({Component, pageProps}) => {
  return (
    <ChakraProvider>
      <Global styles={globalStyles} />

      <Component {...pageProps} />
    </ChakraProvider>
  );
};

export default MyApp;
