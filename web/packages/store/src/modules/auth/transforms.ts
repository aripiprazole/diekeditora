import {createTransform} from 'redux-persist';

export const AuthTransformer = createTransform((inbound, key) => {
  return key !== 'error' && inbound;
});
