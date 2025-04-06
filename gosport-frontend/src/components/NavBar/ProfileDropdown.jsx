import { Link, useNavigate } from "react-router-dom";
import { FaSignOutAlt, FaUser, FaCog } from "react-icons/fa";
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../../store/authSlice';


const ProfileDropdown = ({ isOpen, onClose }) => {
    if (!isOpen) return null;

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const user = useSelector(state => state.auth.user);

    const handleLogout = () => {
        dispatch(logout());
        onClose();
        navigate('/login');
    };

    return (
        <div className="absolute right-0 mt-4 w-56 rounded-md shadow-[0_0_15px_rgba(0,0,0,0.1)] z-20 bg-white">
            <div className="py-2" role="menu">
                <div className="px-4 py-3 border-b border-gray-100">
                    <div className="flex items-center">
                        {user?.avatar ? (
                            <img 
                                src={user.avatar} 
                                alt="Profile" 
                                className="h-10 w-10 rounded-full object-cover"
                            />
                        ) : (
                            <div className="h-10 w-10 rounded-full bg-gray-200 flex items-center justify-center">
                                <FaUser className="text-gray-500" />
                            </div>
                        )}
                        <div className="ml-3">
                            <p className="text-sm font-medium text-gray-900">
                                {user?.profile?.firstName} {user?.profile?.lastName}
                            </p>
                            
                        </div>
                    </div>
                </div>

                <Link 
                    to="/dashboard/profile" 
                    className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    role="menuitem"
                    onClick={onClose}
                >
                    <FaUser className="mr-3 text-gray-400" />
                    View Profile
                </Link>
                <Link 
                    to="/settings" 
                    className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    role="menuitem"
                    onClick={onClose}
                >
                    <FaCog className="mr-3 text-gray-400" />
                    Settings
                </Link>
                <button
                    className="flex items-center w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 border-t border-gray-100"
                    role="menuitem"
                    onClick={handleLogout}
                >
                    <FaSignOutAlt className="mr-3 text-gray-400" />
                    Sign Out
                </button>
            </div>
        </div>
    );

};

export default ProfileDropdown;