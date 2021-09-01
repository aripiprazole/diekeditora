import React from 'react';

import {MangasQueryResponse} from '@diekeditora/graphql/__generated__/MangasQuery.graphql';

type Props = {
  manga: MangasQueryResponse['mangas']['edges'][0]['node'];
};

const Manga: React.VFC<Props> = ({}) => {
  return null;
};

export default Manga;
