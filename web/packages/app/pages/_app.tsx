import React from 'react';

import {AppProps} from 'next/app';

const MyApp: React.VFC<AppProps> = ({Component, pageProps}) => {
  return <Component {...pageProps} />;
};

export default MyApp;
