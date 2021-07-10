import firebase from 'firebase';

firebase.initializeApp({
  apiKey: 'AIzaSyCF7sbQj2KR37WMXl_xbhpVdJ9ZGl-BCL8',
  authDomain: 'diekeditora.firebaseapp.com',
  projectId: 'diekeditora',
  storageBucket: 'diekeditora.appspot.com',
  messagingSenderId: '795441068994',
  appId: '1:795441068994:web:7da9625b100c394c1cf45b',
  measurementId: 'G-EPX9LNJX2E',
});
firebase.analytics();

export {firebase};
