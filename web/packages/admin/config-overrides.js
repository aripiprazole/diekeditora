const JsconfigPathsPlugin = require('jsconfig-paths-webpack-plugin');
const ModuleScopePlugin = require('react-dev-utils/ModuleScopePlugin');

module.exports = {
  webpack(config) {
    config.resolve.plugins = config.resolve.plugins.filter(
      (plugin) => !(plugin instanceof ModuleScopePlugin)
    );
    config.resolve.plugins.push(new JsconfigPathsPlugin());

    return config;
  },

  paths(paths) {
    paths['@diekeditora/admin'] = './src';
    paths['@diekeditora/admin/*'] = './src/*';

    return paths;
  }
};
