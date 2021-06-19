import 'next';
import 'next/types/global';

declare module '*.json' {
  const content: any;
  export default content;
}
