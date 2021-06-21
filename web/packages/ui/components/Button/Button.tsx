import React, {ReactNode, ButtonHTMLAttributes} from 'react';

import styled from 'styled-components';

import {Typography} from '~/components';
import {Color, Size} from '~/theme';

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   */
  color?: Color;

  /**
   * How large should the button be?
   */
  size?: Size;

  /**
   * Which variant the button will be?
   */
  variant?: 'outlined' | 'contained' | 'text';

  /**
   * Button contents
   */
  children?: ReactNode;

  /**
   * Button will be disabled
   */
  disabled?: boolean;
};

/**
 * Primary UI component for user interaction
 */
export const Button: React.FC<ButtonProps> = ({
  size = 'medium',
  color = 'primary',
  variant = 'contained',
  disabled = false,
  children,
  ...props
}) => {
  const buttonVariants = {
    text: TextButton,
    contained: ContainedButton,
    outlined: OutlinedButton,
  };

  const StyledButton = buttonVariants[variant] ?? TextButton;

  return (
    <StyledButton size={size} color={color} variant={variant} disabled={disabled} {...props}>
      <Typography variant="button">{children}</Typography>
    </StyledButton>
  );
};

const TextButton = styled.button<ButtonProps>`
  background: transparent;
  color: ${({theme, color}) => theme.palette[color][450]};

  font-weight: 600;

  border: none;
  border-radius: 0.25rem;

  position: relative;

  transition: 200ms;

  user-select: none;

  padding: ${({size}) =>
    size === 'small' ?
      '0.4rem 2.25rem' :
      size === 'medium' ?
      '0.6rem 4.25rem' :
      size === 'large' ?
      '0.8rem 6.25rem' :
      null};

  :not(:disabled) {
    cursor: pointer;

    :focus {
      outline: 0.125rem solid ${({theme}) => theme.palette.info[100]};
      outline-offset: 0.125rem;
      -moz-outline-radius: 0.125rem;
    }

    :hover {
      color: ${({theme, color}) => theme.palette[color][300]};
    }

    :active {
      color: ${({theme, color}) => theme.palette[color][200]};
    }
  }

  :disabled {
    color: ${({theme}) => theme.palette.neutral[300]};
  }
`;

const ContainedButton = styled(TextButton)`
  background: ${({theme, color}) => theme.palette[color][450]};
  color: ${({theme, color}) => theme.palette[color].contrastText};

  :disabled {
    background: ${({theme}) => theme.palette.neutral[400]};
  }

  :not(:disabled) {
    :hover {
      background: ${({theme, color}) => theme.palette[color][300]};
      color: ${({theme, color}) => theme.palette[color].contrastText};
    }

    :active {
      background: ${({theme, color}) => theme.palette[color][200]};
      color: ${({theme, color}) => theme.palette[color].contrastText};
    }
  }
`;

const OutlinedButton = styled(TextButton)`
  background: transparent;
  border: 0.125rem solid ${({theme, color}) => theme.palette[color][450]};
  color: ${({theme, color}) => theme.palette[color][450]};

  :disabled {
    border: 0.125rem solid ${({theme}) => theme.palette.neutral[400]};
    color: ${({theme}) => theme.palette.neutral[400]};
  }

  :not(:disabled) {
    cursor: pointer;

    :hover {
      border: 0.125rem solid ${({theme, color}) => theme.palette[color][300]};
    }

    :active {
      border: 0.125rem solid ${({theme, color}) => theme.palette[color][200]};
    }
  }
`;
