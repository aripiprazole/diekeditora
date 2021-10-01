import React, {Suspense} from 'react';
import {useNavigate} from 'react-router-dom';
import PropTypes from 'prop-types';

import useAuthenticatedUser from '@diekeditora/admin/utils/UseAuthenticatedUser';
import ErrorBoundaryWithRetry from '@diekeditora/admin/components/ErrorBoundaryWithRetry';

Authenticated.propTypes = {
  children: PropTypes.node.isRequired,
};

function AuthenticatedContent({children}) {
  useAuthenticatedUser();

  return children;
}

export default function Authenticated({children}) {
  const navigate = useNavigate();

  // TODO: add loading screen component in Suspense fallback
  return (
    <ErrorBoundaryWithRetry
      fallback={(error) => {
        console.error('Error was caught when trying to get authenticated', error);

        // Redirect to login page if not authenticated
        // TODO: check if the error is authentication error before redirect
        navigate('/login', {replace: true});

        return null;
      }}
    >
      <Suspense fallback={<div />}>
        <AuthenticatedContent>{children}</AuthenticatedContent>
      </Suspense>
    </ErrorBoundaryWithRetry>
  );
}
