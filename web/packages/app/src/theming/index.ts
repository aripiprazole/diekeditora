import {DeepPartial, extendTheme} from '@chakra-ui/react';
import {Theme} from '@chakra-ui/theme';

const Button: DeepPartial<Theme['components']['Button']> = {
  baseStyle: {
    fontWeight: 'bold',
    borderRadius: 'base',
  },
};

export const theme: Theme = extendTheme({
  config: {
    useSystemColorMode: true,
  },
  components: {
    Button,
  },
});
