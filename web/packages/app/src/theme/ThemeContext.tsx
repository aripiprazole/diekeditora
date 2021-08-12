import React, {createContext} from 'react';

import {ChakraProvider} from '@chakra-ui/react';

import {useLocalStorage} from '@diekeditora/app';

import DarkTheme from './DarkTheme';
import LightTheme from './LightTheme';

type ThemeContext = {
  dark: boolean,
  toggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContext>({
  dark: true,
  toggleTheme: () => {
  },
});

export const ThemeProvider: React.FC = ({children}) => {
  const [dark, setDark] = useLocalStorage('isDarkTheme', true);

  function toggleTheme() {
    setDark((dark) => !dark);
  }

  return (
    <ThemeContext.Provider value={{dark, toggleTheme}}>
      <ChakraProvider theme={dark ? DarkTheme : LightTheme}>
        {children}
      </ChakraProvider>
    </ThemeContext.Provider>
  );
};

export default ThemeContext;

