module.exports = {
  presets: ['react-app'],
  plugins: [
    [
      'module-resolver',
      {
        root: ['./src'],
        alias: {
          '@diekeditora/admin': ['.'],
          '@diekeditora/admin/*': ['./*']
        }
      }
    ]
  ]
};
