import React from 'react';

import { AppProps } from 'next/app';

import '~/styles/_globals.scss';

const MyApp: React.FC<AppProps> = ({ Component, pageProps }) => {
  return <Component {...pageProps} />;
};

export default MyApp;