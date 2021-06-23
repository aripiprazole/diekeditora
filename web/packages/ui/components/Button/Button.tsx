import React, {ReactNode, ButtonHTMLAttributes} from 'react';

import styled, {css} from 'styled-components';

import {Color, Size} from '@diekeditora/ui/theme';
import {Typography} from '@diekeditora/ui';

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  /**
   * The color of the component. It supports those theme colors that make sense for this component.
   */
  color?: Color;

  /**
   * The type of the component
   */
  as?: any;

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

  /**
   * Button outline be disabled
   */
  outline?: boolean;
};

const defaultProps: ButtonProps = {
  size: 'medium',
  color: 'primary',
  variant: 'contained',
  disabled: false,
  outline: true,
};

/**
 * Primary UI component for user interaction
 */
export const Button: React.FC<ButtonProps> = ({
  size = defaultProps.size,
  color = defaultProps.color,
  variant = defaultProps.variant,
  disabled = defaultProps.disabled,
  outline = defaultProps.outline,
  as,
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
    <StyledButton outline={outline} size={size} color={color} variant={variant} disabled={disabled} {...props}>
      <Typography variant="button">{children}</Typography>
    </StyledButton>
  );
};

export const TextButton = styled.button<ButtonProps>`
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
      ${({outline}) =>
    outline &&
        css`
          outline: 0.125rem solid ${({theme}) => theme.palette.info[100]};
          outline-offset: 0.125rem;
          -moz-outline-radius: 0.125rem;
        `};
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

export const ContainedButton = styled(TextButton)`
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

export const OutlinedButton = styled(TextButton)`
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

OutlinedButton.defaultProps = defaultProps;
TextButton.defaultProps = defaultProps;
ContainedButton.defaultProps = defaultProps;
