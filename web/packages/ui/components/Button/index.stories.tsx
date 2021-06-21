import React from 'react';

import {Meta, Story} from '@storybook/react';

import {Button, ButtonProps} from '.';

export default {
  title: 'Library/Components/Button',
  component: Button,
  argTypes: {
    color: {
      options: ['primary', 'secondary', 'neutral'],
      control: {type: 'select'},
    },
    variant: {
      options: ['outlined', 'contained', 'text'],
      control: {type: 'select'},
    },
    size: {
      options: ['small', 'medium', 'large'],
      control: {type: 'select'},
    },
    disabled: {
      control: {type: 'boolean'},
    },
  },
  args: {
    size: 'medium',
    variant: 'contained',
    color: 'primary',
  },
} as Meta;

export const Primary: Story<ButtonProps> = (props) => <Button {...props} />;
Primary.args = {
  children: 'Button',
};

export const Secondary: Story<ButtonProps> = (props) => <Button {...props} />;
Secondary.args = {
  color: 'secondary',
  children: 'Button',
};

export const Outlined: Story<ButtonProps> = (props) => <Button {...props} />;
Outlined.args = {
  color: 'primary',
  variant: 'outlined',
  children: 'Button',
};

export const Disabled: Story<ButtonProps> = (props) => <Button {...props} />;
Disabled.args = {
  color: 'primary',
  disabled: true,
  children: 'Button',
};

export const OutlineDisabled: Story<ButtonProps> = (props) => <Button {...props} />;
OutlineDisabled.args = {
  variant: 'outlined',
  disabled: true,
  children: 'Button',
};

export const Text: Story<ButtonProps> = (props) => <Button {...props} />;
Text.args = {
  variant: 'text',
  children: 'Button',
};

export const TextDisabled: Story<ButtonProps> = (props) => <Button {...props} />;
TextDisabled.args = {
  variant: 'text',
  disabled: true,
  children: 'Button',
};
