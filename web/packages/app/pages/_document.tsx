import React, {Component} from 'react';

import Document, {DocumentContext, Head, Html, Main, NextScript} from 'next/document';

import {ServerStyleSheet} from 'styled-components';

type DocumentProps = {
  styles: Component;
};

/**
 * Next document
 */
class AppDocument extends Document<DocumentProps> {
  /**
   * Get initial props of document
   *
   * @param {DocumentContext} ctx
   */
  public static async getInitialProps(ctx: DocumentContext) {
    const sheet = new ServerStyleSheet();
    const originalRenderPage = ctx.renderPage;

    try {
      ctx.renderPage = () =>
        originalRenderPage({
          enhanceApp: (App) => (props) => sheet.collectStyles(<App {...props} />),
        });

      const initialProps = await Document.getInitialProps(ctx);

      return {
        ...initialProps,
        styles: (
          <>
            {initialProps.styles}
            {sheet.getStyleElement()}
          </>
        ),
      };
    } finally {
      sheet.seal();
    }
  }

  /**
   * @return {JSX.Element}
   */
  public render(): JSX.Element {
    return (
      <Html>
        <Head>{this.props.styles}</Head>

        <body>
          <Main />
          <NextScript />
        </body>
      </Html>
    );
  }
}

export default AppDocument;
