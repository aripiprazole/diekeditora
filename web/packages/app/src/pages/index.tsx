import React from 'react';

import {GetServerSideProps, NextPage} from 'next';
import {loadQuery, usePreloadedQuery, PreloadedQuery} from 'react-relay';

import {MangasQuery as MangasQueryType} from '@diekeditora/graphql/__generated__/MangasQuery.graphql';
import {MangasQuery} from '@diekeditora/graphql';

import {Page, relayEnvironment} from '@diekeditora/app';

type Props = {
  initialQueryRef: PreloadedQuery<MangasQueryType>;
};

const Home: NextPage<Props> = ({initialQueryRef}) => {
  // eslint-disable-next-line no-unused-vars
  const {mangas} = usePreloadedQuery<MangasQueryType>(MangasQuery, initialQueryRef);

  return <Page>Hello, world</Page>;
};

export const getServerSideProps: GetServerSideProps = async () => ({
  props: {
    initialQueryRef: loadQuery<MangasQueryType>(relayEnvironment, MangasQuery, {first: 15}),
  },
});

export default Home;
