import { configure } from 'mobx';

configure({ useProxies: 'never' });

export { default as auth } from './auth';
