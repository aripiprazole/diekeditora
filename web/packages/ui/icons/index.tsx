import React from 'react';

import Image, {ImageProps} from 'next/image';

export type IconProps = Omit<ImageProps, 'src'>;

const image = (assets: any): React.VFC<IconProps> => {
  const DiekEditoraIcon = (props: IconProps) => <Image src={assets} {...(props as any)} />;

  return DiekEditoraIcon;
};

export const FullLogo = image(require('./FullLogo.svg'));
