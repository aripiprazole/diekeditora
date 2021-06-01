import React from 'react';

import { AppProps } from 'next/app';

type Props = AppProps & {
  pageProps: any;
};

const MyApp: React.FC<Props> = ({ Component, pageProps }) => {
  return <Component {...pageProps} />;
};

export default MyApp;
