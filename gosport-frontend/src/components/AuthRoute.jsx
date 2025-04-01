import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const AuthRoute = ({ isPrivate }) => {
  const { isValid, isLoading } = useAuth();

  if (isLoading) {
    return <p>Sprawdzanie sesji...</p>;
  }

  if (isPrivate) {
    return isValid ? <Outlet /> : <Navigate to="/login" />;
  }

  return isValid ? <Navigate to="/dashboard" /> : <Outlet />;
};

export default AuthRoute;