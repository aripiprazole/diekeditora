import React from 'react';

import {AppProps} from 'next/app';

import {ThemeProvider} from 'styled-components';

import {GlobalStyle} from '@diekeditora/ui/styles';
import {defaultTheme} from '@diekeditora/ui/theme';

const MyApp: React.VFC<AppProps> = ({Component, pageProps}) => {
  return (
    <ThemeProvider theme={defaultTheme}>
      <Component {...pageProps} />
      <GlobalStyle />
    </ThemeProvider>
  );
};

export default MyApp;
