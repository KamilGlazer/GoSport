import { createContext, useState, useEffect, useContext } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [authState, setAuthState] = useState({
    isValid: false,
    isLoading: true
  });

  const validateToken = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      setAuthState({ isValid: false, isLoading: false });
      return;
    }

    try {
      const response = await api.get('/auth/validate', {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      setAuthState({
        isValid: response.status === 200,
        isLoading: false
      });
    } catch (error) {
      localStorage.removeItem('token');
      setAuthState({
        isValid: false,
        isLoading: false
      });
    }
  };

  useEffect(() => {
    validateToken();
  }, []);

  return (
    <AuthContext.Provider value={{ ...authState, validateToken }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};