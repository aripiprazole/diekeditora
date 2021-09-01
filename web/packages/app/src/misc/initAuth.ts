import {init} from 'next-firebase-auth';

export default function initAuth() {
  try {
    init({
      authPageURL: '/login',
      appPageURL: '/',
      loginAPIEndpoint: '/api/login', // required
      logoutAPIEndpoint: '/api/logout', // required
      firebaseAdminInitConfig: {
        databaseURL: process.env.FIREBASE_DATABASE_URL,
        credential: {
          privateKey: process.env.FIREBASE_PRIVATE_KEY,
          clientEmail: process.env.FIREBASE_CLIENT_EMAIL,
          projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
        },
      },
      firebaseClientInitConfig: {
        apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
        authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
        projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
        storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
        messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
        appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
        measurementId: process.env.NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID,
      },
      cookies: {
        name: 'DiekEditora', // required
        // Keys are required unless you set `signed` to `false`.
        // The keys cannot be accessible on the client side.
        keys: [process.env.COOKIE_SECRET_CURRENT, process.env.COOKIE_SECRET_PREVIOUS],
        httpOnly: true,
        maxAge: 12 * 60 * 60 * 24 * 1000, // twelve days
        overwrite: true,
        path: '/',
        sameSite: 'strict',
        secure: process.env.NODE_ENV !== 'development', // set this to false in local (non-HTTPS) development
        signed: true,
      },
    });
  } catch (error) {
    console.error(error);
  }
}
