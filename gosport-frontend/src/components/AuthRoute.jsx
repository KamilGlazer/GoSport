import { Navigate, Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';

const AuthRoute = ({ isPrivate = false }) => {
  const { token } = useSelector(state => state.auth);
  
  if (isPrivate) {
    return token ? <Outlet /> : <Navigate to="/login" replace />;
  } else {
    return token ? <Navigate to="/dashboard" replace /> : <Outlet />;
  }
};

export default AuthRoute;