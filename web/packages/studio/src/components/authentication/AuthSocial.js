import {useNavigate} from 'react-router-dom';

import {Icon} from '@iconify/react';
import googleFill from '@iconify/icons-eva/google-fill';
import twitterFill from '@iconify/icons-eva/twitter-fill';
import facebookFill from '@iconify/icons-eva/facebook-fill';
// material
import {Stack, Button, Divider, Typography} from '@material-ui/core';

// firebase
import {signInWithPopup, getAuth, GoogleAuthProvider, FacebookAuthProvider, TwitterAuthProvider} from 'firebase/auth';

// ----------------------------------------------------------------------

const googleProvider = new GoogleAuthProvider();
const facebookProvider = new FacebookAuthProvider();
const twitterProvider = new TwitterAuthProvider();

export default function AuthSocial() {
  const navigate = useNavigate();

  async function loginWith(provider) {
    const auth = getAuth();

    try {
      const result = await signInWithPopup(auth, provider);

      localStorage.setItem('Authorization', result.user.accessToken);

      console.log('Result from sign-in with google', result);

      navigate('/dashboard');
    } catch (error) {
      console.error('Error when signing-in with google', error);
    }
  }

  return (
    <>
      <Stack direction="row" spacing={2}>
        <Button fullWidth size="large" color="inherit" variant="outlined" onClick={() => loginWith(googleProvider)}>
          <Icon icon={googleFill} color="#DF3E30" height={24} />
        </Button>

        <Button fullWidth size="large" color="inherit" variant="outlined" onClick={() => loginWith(facebookProvider)}>
          <Icon icon={facebookFill} color="#1877F2" height={24} />
        </Button>

        <Button fullWidth size="large" color="inherit" variant="outlined" onClick={() => loginWith(twitterProvider)}>
          <Icon icon={twitterFill} color="#1C9CEA" height={24} />
        </Button>
      </Stack>

      <Divider sx={{my: 3}}>
        <Typography variant="body2" sx={{color: 'text.secondary'}}>
          OR
        </Typography>
      </Divider>
    </>
  );
}
