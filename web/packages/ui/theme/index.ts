import {PaletteColor} from './color';
import {createTheme} from './theme';

export type Palette = {
  neutral: PaletteColor;
  primary: PaletteColor;
  secondary: PaletteColor;
  info: PaletteColor;
  success: PaletteColor;
  error: PaletteColor;
  extra: PaletteColor;
};

export type Theme = {
  palette: Palette;
};

declare module 'styled-components' {
  export interface DefaultTheme extends Theme {}
}

export const defaultTheme = createTheme({
  palette: {
    neutral: {
      [500]: '#FFFFFF',
      [400]: '#E0E0E0',
      [300]: '#888888',
      [200]: '#686868',
      [100]: '#000000',
      contrastText: '#FFFFFF',
    },

    primary: {
      [900]: '#F8F2FF',
      [600]: '#C07EFF',
      [450]: '#9B5DD6',
      [300]: '#691D89',
      [200]: '#4A2370',
      contrastText: '#FFFFFF',
    },

    secondary: {
      [900]: '#FFE1F0',
      [600]: '#FF5CB0',
      [450]: '#FF0083',
      [300]: '#A91762',
      [200]: '#7F0041',
      contrastText: '#FFFFFF',
    },

    info: {
      [100]: '#2F80ED',
      [200]: '#2CB3FF',
      [300]: '#EEF7FF',
      contrastText: '#FFFFFF',
    },

    success: {
      [100]: '#1AB759',
      [200]: '#2AC769',
      [300]: '#40DD7F',
      contrastText: '#FFFFFF',
    },

    error: {
      [100]: '#E93C3C',
      [200]: '#FB4E4E',
      [300]: '#FF6262',
      contrastText: '#FFFFFF',
    },

    extra: {
      [100]: '#FF1B44',
      contrastText: '#FFFFFF',
    },
  },
});

export * from './color';
export * from './theme';
