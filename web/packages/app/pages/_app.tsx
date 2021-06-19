import React from 'react';

import {AppProps} from 'next/dist/next-server/lib/router/router';

import '../styles/globals.css';

const MyApp: React.VFC<AppProps> = ({Component, pageProps}) => {
  return <Component {...pageProps} />;
};

export default MyApp;
