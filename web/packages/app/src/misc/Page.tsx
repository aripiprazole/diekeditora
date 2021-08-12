import React from 'react';

import Head from 'next/head';

const Page: React.FC = ({children}) => {
  return (
    <>
      <Head>
        <title>Diek editora</title>
        <meta name="description" content="Diek editora website" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      {children}
    </>
  );
};

export default Page;
