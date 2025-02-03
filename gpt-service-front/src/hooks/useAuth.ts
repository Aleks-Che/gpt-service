import { useSelector, useDispatch } from 'react-redux';

import { setAuth, logout } from '../store/slices/authSlice';
import { RootState } from '../store';

export const useAuth = () => {
  const dispatch = useDispatch();
  const auth = useSelector((state: RootState) => state.auth);

  const login = (userData: { user: any; token: string }) => {
    dispatch(setAuth(userData));
  };

  const handleLogout = () => {
    dispatch(logout());
  };

  return {
    isAuthenticated: auth.isAuthenticated,
    user: auth.user,
    token: auth.token,
    login,
    logout: handleLogout
  };
};
