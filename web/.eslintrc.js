module.exports = {
  env: {
    browser: true,
    es2021: true,
    mocha: true,
  },
  extends: ['plugin:react/recommended', 'google'],
  parser: '@typescript-eslint/parser',
  settings: {
    react: {
      version: 'detect',
    },
  },
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 12,
    sourceType: 'module',
  },
  plugins: ['react', '@typescript-eslint'],
  rules: {
    'max-len': ['error', {code: 120}],
    'react/prop-types': [0],
  },
};
