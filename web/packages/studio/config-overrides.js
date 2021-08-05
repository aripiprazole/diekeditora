const JsconfigPathsPlugin = require('jsconfig-paths-webpack-plugin');
const ModuleScopePlugin = require('react-dev-utils/ModuleScopePlugin');

module.exports = {
  webpack(config) {
    config.resolve.plugins = config.resolve.plugins.filter(
      (plugin) => !(plugin instanceof ModuleScopePlugin)
    );
    config.resolve.plugins.push(new JsconfigPathsPlugin());
    
    config.resolve.alias['@diekeditora/admin'] = './src';

    return config;
  },

  paths(paths) {
    return paths;
  }
};
