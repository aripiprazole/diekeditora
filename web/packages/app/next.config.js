const withTM = require('next-transpile-modules')(['@diekeditora/store'], {resolveSymlinks: false});

module.exports = withTM({
  pageExtensions: ['tsx'],
  reactStrictMode: true,
});
