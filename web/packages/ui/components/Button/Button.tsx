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

  border?: Size | 'full';

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
    <StyledButton
      outline={outline}
      size={size}
      color={color}
      variant={variant}
      disabled={disabled}
      className={[disabled && 'disabled'].join(' ')}
      {...props}
    >
      <Typography variant="button">{children}</Typography>
    </StyledButton>
  );
};

const buttonSizes = {
  small: '0.6rem 1rem',
  medium: '0.6rem 4.25rem',
  large: '0.6rem 8.25rem',
};

export const TextButton = styled.button<ButtonProps>`
  background: transparent;
  color: ${({theme, color}) => theme.palette[color].default};

  font-weight: 600;

  border-radius: ${({theme, border = 'medium'}) => theme[border]};
  border: none;

  position: relative;

  transition: 200ms;

  user-select: none;

  padding: ${({size}) => buttonSizes[size]};

  :not(.disabled) {
    cursor: pointer;

    :focus {
      ${({outline}) =>
    outline &&
        css`
          outline: 0.125rem solid ${({theme}) => theme.palette.info.dark};
          outline-offset: 0.125rem;
          -moz-outline-radius: 0.125rem;
        `};
    }

    :hover {
      color: ${({theme, color}) => theme.palette[color].dark};
    }

    :active {
      color: ${({theme, color}) => theme.palette[color].darker};
    }
  }

  .disabled {
    color: ${({theme}) => theme.palette.neutral[300]};
  }
`;

export const ContainedButton = styled(TextButton)`
  background: ${({theme, color}) => theme.palette[color].default};
  color: ${({theme, color}) => theme.palette[color].contrastText};

  :disabled {
    background: ${({theme}) => theme.palette.neutral.light};
  }

  :not(.disabled) {
    :hover {
      background: ${({theme, color}) => theme.palette[color].dark};
      color: ${({theme, color}) => theme.palette[color].contrastText};
    }

    :active {
      background: ${({theme, color}) => theme.palette[color].darker};
      color: ${({theme, color}) => theme.palette[color].contrastText};
    }
  }
`;

export const OutlinedButton = styled(TextButton)`
  background: transparent;
  border: 0.125rem solid ${({theme, color}) => theme.palette[color].default};
  color: ${({theme, color}) => theme.palette[color].default};

  .disabled {
    border: 0.125rem solid ${({theme}) => theme.palette.neutral.light};
    color: ${({theme}) => theme.palette.neutral.light};
  }

  :not(.disabled) {
    cursor: pointer;

    :hover {
      border: 0.125rem solid ${({theme, color}) => theme.palette[color].dark};
    }

    :active {
      border: 0.125rem solid ${({theme, color}) => theme.palette[color].darker};
    }
  }
`;

OutlinedButton.defaultProps = defaultProps;
TextButton.defaultProps = defaultProps;
ContainedButton.defaultProps = defaultProps;
