import {color, PaletteColor} from './color';
import {createTheme} from './theme';

export type Palette = {
  neutral: PaletteColor;
  primary: PaletteColor;
  secondary: PaletteColor;
  info: PaletteColor;
  success: PaletteColor;
  error: PaletteColor;
  extra: PaletteColor;

  bg: string;
};

export type Borders = {
  full: string;
  large: string;
  medium: string;
  small: string;
};

export type Theme = {
  palette: Palette;
  borders: Borders;
};

declare module 'styled-components' {
  export interface DefaultTheme extends Theme {}
}

export const defaultTheme = createTheme({
  borders: {
    full: '50%',
    large: '1.25rem',
    medium: '0.625rem',
    small: '0.25rem',
  },
  palette: {
    bg: '#242424',

    neutral: color({
      light: '#E0E0E0',
      default: '#FFFFFF',
      dark: '#686868',
      darker: '#888888',
      contrastText: '#000000',
    }),

    primary: color({
      lighter: '#F8F2FF',
      light: '#C07EFF',
      default: '#9B5DD6',
      dark: '#691D89',
      darker: '#4A2370',
      contrastText: '#FFFFFF',
    }),

    secondary: color({
      lighter: '#FFE1F0',
      light: '#FF5CB0',
      default: '#FF0083',
      dark: '#A91762',
      darker: '#7F0041',
      contrastText: '#FFFFFF',
    }),

    info: color({
      light: '#EEF7FF',
      default: '#2CB3FF',
      dark: '#2F80ED',
      contrastText: '#FFFFFF',
    }),

    success: color({
      light: '#1AB759',
      default: '#2AC769',
      dark: '#40DD7F',
      contrastText: '#FFFFFF',
    }),

    error: color({
      light: '#E93C3C',
      default: '#FB4E4E',
      dark: '#FF6262',
      contrastText: '#FFFFFF',
    }),

    extra: color({
      light: '#FF1B44',
      default: '#FF1B44',
      dark: '#FF1B44',
      contrastText: '#FFFFFF',
    }),
  },
});

export * from './color';
export * from './theme';
