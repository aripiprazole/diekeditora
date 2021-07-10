module.exports = {
  presets: [
    '@babel/preset-env',
    '@babel/preset-typescript',
    [
      'module-resolver',
      {
        root: ['./src'],
        alias: { '~/': './' },
      },
    ],
  ],
  plugins: ['@babel/plugin-transform-typescript'],
};
