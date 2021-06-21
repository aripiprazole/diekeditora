import {createGlobalStyle} from 'styled-components';
import {fontFamily} from './fonts';

export const GlobalStyle = createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }
  
  *, input, button, select, textarea, optgroup, option {
    ${fontFamily('Poppins')}
  }
  
  html,
  body,
  #root,
  #__next {
    height: 100%;
    width: 100%;
    min-width: 25.625rem;
  }

  *:focus {
    border: none;
    outline: none;
  }

  a {
    text-decoration: none;
  }
  
  ul,
  li {
    list-style: none;
  }

  body {
  }
`;
