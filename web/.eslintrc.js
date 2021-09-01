module.exports = {
  env: {
    browser: true,
    es2021: true,
    mocha: true,
  },
  extends: ['plugin:react/recommended', 'plugin:prettier/recommended', 'google'],
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
  plugins: ['react', 'react-hooks', 'prettier', '@typescript-eslint'],
  rules: {
    'max-len': ['error', {code: 120}],
    'indent': [2, 2],
    'quotes': ['error', 'single', {avoidEscape: true}],
    'operator-linebreak': ['error', 'after', {overrides: {'?': 'before', ':': 'before'}}],
    'valid-jsdoc': 'off',
    'indent': 'off',
    'space-before-function-paren': 'off',
    'require-jsdoc': 'off',
    'react/react-in-jsx-scope': 'off', // TODO: remove this
    'react/prop-types': 'off',
    'object-curly-spacing': ['error', 'never'],
    'array-bracket-spacing': ['error', 'never'],
    'computed-property-spacing': ['error', 'never'],
    'prettier/prettier': [
      'error',
      {
        tabWidth: 2,
        singleQuote: true,
        printWidth: 120,
        bracketSpacing: false,
        quoteProps: 'consistent',
        trailingComma: 'all',
      },
    ],
  },
};
