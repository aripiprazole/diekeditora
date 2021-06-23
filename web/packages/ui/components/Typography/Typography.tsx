import React, {ReactNode} from 'react';

import styled from 'styled-components';

import {fontFamily} from '@diekeditora/ui/styles';

export type TypographyProps = React.HTMLAttributes<HTMLElement> & {
  /**
   * Typography variant type
   */
  variant?: 'h1' | 'h2' | 'h3' | 'h4' | 'body1' | 'body2' | 'body3' | 'button';

  /**
   * Typography component type
   */
  as?: any;

  /**
   * Typography will be in italic?
   */
  italic?: boolean;

  /**
   * Typography will be bold?
   */
  bold?: number;

  /**
   * Typography will in which line height?
   */
  lineHeight?: string;

  /**
   * Typography will in which size?
   */
  fontSize?: string;

  /**
   * Typography font weight?
   */
  fontWeight?: number;

  /**
   * Typography will be in which font family?
   */
  fontFamily?: string;

  /**
   * Typography contents
   */
  children?: ReactNode;
};

export const Typography: React.VFC<TypographyProps> = ({variant, fontFamily = 'Poppins', ...props}) => {
  const typographyVariants = {
    h1: H1,
    h2: H2,
    h3: H3,
    h4: H4,
    body1: Body1,
    body2: Body2,
    body3: Body3,
    button: Button,
  };

  if (typographyVariants[variant]) {
    const StyledTypography = typographyVariants[variant];

    return <StyledTypography className="DiekEditora_Typography" fontFamily={fontFamily} {...(props as any)} />;
  }

  return null;
};

const H1 = styled.h1<TypographyProps>`
  font-size: ${({fontSize = '24px'}) => fontSize};
  line-height: ${({lineHeight = '32px'}) => lineHeight};
  font-weight: ${({fontWeight = 700}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;

const H2 = styled.h2<TypographyProps>`
  font-size: ${({fontSize = '22px'}) => fontSize};
  line-height: ${({lineHeight = '32px'}) => lineHeight};
  font-weight: ${({fontWeight = 700}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;

const H3 = styled.h3<TypographyProps>`
  font-size: ${({fontSize = '18px'}) => fontSize};
  line-height: ${({lineHeight = '32px'}) => lineHeight};
  font-weight: ${({fontWeight = 600}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;

const H4 = styled.h4<TypographyProps>`
  font-size: ${({fontSize = '16px'}) => fontSize};
  line-height: ${({lineHeight = '24px'}) => lineHeight};
  font-weight: ${({fontWeight = 700}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;

const Body1 = styled.p<TypographyProps>`
  font-size: ${({fontSize = '14px'}) => fontSize};
  line-height: ${({lineHeight = '32px'}) => lineHeight};
  font-weight: ${({fontWeight = 700}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;

const Body2 = styled.p<TypographyProps>`
  font-size: ${({fontSize = '12px'}) => fontSize};
  line-height: ${({lineHeight = '24px'}) => lineHeight};
  font-weight: ${({fontWeight = 700}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;

const Body3 = styled.p<TypographyProps>`
  font-size: ${({fontSize = '10px'}) => fontSize};
  line-height: ${({lineHeight = '16px'}) => lineHeight};
  font-weight: ${({fontWeight = 600}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;

const Button = styled.span<TypographyProps>`
  font-size: ${({fontSize = '12px'}) => fontSize};
  line-height: ${({lineHeight = '24px'}) => lineHeight};
  font-weight: ${({fontWeight = 600}) => fontWeight};

  ${(props) => fontFamily(props.fontFamily)}
`;
