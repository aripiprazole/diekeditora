import React from 'react';

import {Story} from '@storybook/react';

import {Page, PageProps} from '.';

import * as HeaderStories from '../Header/index.stories';

export default {
  title: 'Example/Page',
  component: Page,
};

const Template: Story<PageProps> = (args) => <Page {...args} />;

export const LoggedIn = Template.bind({});
LoggedIn.args = {
  ...HeaderStories.LoggedIn.args,
};

export const LoggedOut = Template.bind({});
LoggedOut.args = {
  ...HeaderStories.LoggedOut.args,
};
