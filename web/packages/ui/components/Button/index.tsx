import React from 'react';

import './index.css';

export type ButtonProps = {
  /**
   * Is this the principal call to action on the page?
   */
  primary?: boolean;

  /**
   * What background color to use
   */
  backgroundColor?: string;

  /**
   * Button contents
   */
  label: string;

  /**
   * How large should the button be?
   */
  size?: 'small' | 'medium' | 'large';

  /**
   * Optional click handler
   */
  onClick: () => void;
};

/**
 * Primary UI component for user interaction
 *
 * @return {JSX.Element}
 */
export const Button: React.FC<ButtonProps> = ({size = 'medium', primary, backgroundColor, label, ...props}) => {
  const mode = primary ? 'storybook-button--primary' : 'storybook-button--secondary';

  return (
    <button
      type="button"
      className={['storybook-button', `storybook-button--${size}`, mode].join(' ')}
      style={backgroundColor && {backgroundColor}}
      {...props}
    >
      {label}
    </button>
  );
};
