export type PaletteColorOptions = {
  [number: number]: string | null;

  contrastText: string;
};

export type Color = 'primary' | 'secondary' | 'neutral';

export type PaletteColor = PaletteColorOptions & {
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
    toString: () => options[300],
  };
}
