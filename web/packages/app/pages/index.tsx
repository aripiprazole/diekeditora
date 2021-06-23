import React from 'react';

import Head from 'next/head';

import {Header} from '@diekeditora/ui';

import {Container} from './styles';

const Home: React.VFC = () => {
  return (
    <Container>
      <Head>
        <title>Diek editora - Home</title>
        <meta name="description" content="Diek editora website" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <Header />

      <main></main>

      <footer></footer>
    </Container>
  );
};

export default Home;
