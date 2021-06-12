declare module '*.svg' {
  const component: React.FC<React.ComponentProps<'svg'>>;

  export default component;
}

declare module global {
  interface ProcessEnv {
    NEXT_PUBLIC_FIREBASE_PROJECT_ID: string;
    NEXT_PUBLIC_FIREBASE_APP_ID: string;
    NEXT_PUBLIC_FIREBASE_API_KEY: string;
    NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN: string;
    NEXT_PUBLIC_API_URL: string;
  }
}
