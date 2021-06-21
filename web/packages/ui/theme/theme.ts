import {Palette, Theme} from '~/theme';

type ThemeOptions = {
  palette: Palette;
};

export type Size = 'small' | 'medium' | 'large';

/**
 * Creates theme from options
 *
 * @param {ThemeOptions} options
 * @return {Theme}
 */
export function createTheme(options: ThemeOptions): Theme {
  return {
    palette: options.palette,
  };
}
