const withTM = require('next-transpile-modules')(['@diekeditora/store'], {resolveSymlinks: true});

module.exports = withTM({
  pageExtensions: ['tsx'],
  reactStrictMode: true,
});
