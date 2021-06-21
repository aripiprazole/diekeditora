import React from 'react';

import {Meta, Story} from '@storybook/react';

import {Typography, TypographyProps} from './Typography';

export default {
  title: 'Library/Components/Typography',
  component: Typography,
  argTypes: {
    fontFamily: {control: 'text'},
    fontWeight: {control: 'number'},
    lineHeight: {control: 'number'},
    fontSize: {control: 'number'},
    variant: {
      options: ['h1', 'h2', 'h3', 'h4', 'body1', 'body2', 'body3', 'button'],
      control: {type: 'select'},
    },
  },
} as Meta;

export const H1: Story<TypographyProps> = (props) => <Typography {...props} />;
H1.args = {
  variant: 'h1',
  children: 'Headline 1',
};

export const H2: Story<TypographyProps> = (props) => <Typography {...props} />;
H2.args = {
  variant: 'h2',
  children: 'Headline 2',
};

export const H3: Story<TypographyProps> = (props) => <Typography {...props} />;
H3.args = {
  variant: 'h3',
  children: 'Headline 3',
};

export const H4: Story<TypographyProps> = (props) => <Typography {...props} />;
H4.args = {
  variant: 'h4',
  children: 'Headline 4',
};

export const Body1: Story<TypographyProps> = (props) => <Typography {...props} />;
Body1.args = {
  variant: 'body1',
  children: 'Body 1',
};

export const Body2: Story<TypographyProps> = (props) => <Typography {...props} />;
Body2.args = {
  variant: 'body2',
  children: 'Body 2',
};

export const Body3: Story<TypographyProps> = (props) => <Typography {...props} />;
Body3.args = {
  variant: 'body3',
  children: 'Body 3',
};

export const Button: Story<TypographyProps> = (props) => <Typography {...props} />;
Button.args = {
  variant: 'button',
  children: 'Button',
};
