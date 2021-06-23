export type PaletteColorOptions = {
  darker?: string;
  dark: string;
  default: string;
  light: string;
  lighter?: string;

  contrastText: string;
};

export type Color = 'primary' | 'secondary' | 'neutral';

export type PaletteColor = PaletteColorOptions & {
  darker: string;
  lighter: string;

  toString: () => string;
};

/**
 * Creates a color object
 *
 * @param {PaletteColorOptions} options
 * @return {PaletteColor}
 */
export function color(options: PaletteColorOptions): PaletteColor {
  return {
    ...options,
    darker: options.darker ?? options.dark,
    lighter: options.lighter ?? options.light,
    toString: () => options[300],
  };
}
