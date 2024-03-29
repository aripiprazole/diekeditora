import {Navigate, useRoutes} from 'react-router-dom';
// layouts
import DashboardLayout from './layouts/dashboard';
import LogoOnlyLayout from './layouts/LogoOnlyLayout';
//
import Login from './pages/Login';
import DashboardApp from './pages/DashboardApp';
import User from './pages/User';
import NotFound from './pages/Page404';
import Authenticated from './components/authentication/Authenticated';

// ----------------------------------------------------------------------

export default function Router() {
  return useRoutes([
    {
      path: '/dashboard',
      element: <DashboardLayout />,
      children: [
        {path: '/', element: <Navigate to="/dashboard/app" replace />},
        {
          path: 'app',
          element: (
            <Authenticated>
              <DashboardApp />
            </Authenticated>
          ),
        },
        {path: 'user', element: <User />},
      ],
    },
    {
      path: '/',
      element: <LogoOnlyLayout />,
      children: [
        {path: 'login', element: <Login />},
        {path: '404', element: <NotFound />},
        {path: '/', element: <Navigate to="/dashboard" />},
        {path: '*', element: <Navigate to="/404" />},
      ],
    },

    {path: '*', element: <Navigate to="/404" replace />},
  ]);
}
