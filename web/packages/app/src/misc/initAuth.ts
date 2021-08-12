import {init} from 'next-firebase-auth';

export default function initAuth() {
  init({
    authPageURL: '/login',
    appPageURL: '/',
    loginAPIEndpoint: '/api/login', // required
    logoutAPIEndpoint: '/api/logout', // required
    firebaseClientInitConfig: {
      apiKey: process.env.NEXT_APP_FIREBASE_API_KEY,
      authDomain: process.env.NEXT_APP_FIREBASE_AUTH_DOMAIN,
      projectId: process.env.NEXT_APP_FIREBASE_PROJECT_ID,
      storageBucket: process.env.NEXT_APP_FIREBASE_STORAGE_BUCKET,
      messagingSenderId: process.env.NEXT_APP_FIREBASE_MESSAGING_SENDER_ID,
      appId: process.env.NEXT_APP_FIREBASE_APP_ID,
      measurementId: process.env.NEXT_APP_FIREBASE_MEASUREMENT_ID,
    },
    cookies: {
      name: 'DiekEditora', // required
      // Keys are required unless you set `signed` to `false`.
      // The keys cannot be accessible on the client side.
      keys: [
        process.env.COOKIE_SECRET_CURRENT,
        process.env.COOKIE_SECRET_PREVIOUS,
      ],
      httpOnly: true,
      maxAge: 12 * 60 * 60 * 24 * 1000, // twelve days
      overwrite: true,
      path: '/',
      sameSite: 'strict',
      secure: process.env.NODE_ENV !== 'development', // set this to false in local (non-HTTPS) development
      signed: true,
    },
  });
}
