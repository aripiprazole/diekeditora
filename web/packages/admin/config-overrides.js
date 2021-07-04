const JsconfigPathsPlugin = require('jsconfig-paths-webpack-plugin');

module.exports = function override(config) {
  config.resolve.plugins.push(new JsconfigPathsPlugin());

  return config;
};
