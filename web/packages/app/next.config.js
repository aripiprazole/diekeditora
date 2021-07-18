const withTM = require('next-transpile-modules')(['@diekeditora/store']);

module.exports = withTM({
  pageExtensions: ['tsx'],
  reactStrictMode: true,
});
