const withTM = require('next-transpile-modules')(['@diekeditora/graphql'], {resolveSymlinks: true});

module.exports = withTM({
  pageExtensions: ['tsx'],
  reactStrictMode: true,
});
