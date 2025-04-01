import { Link } from "react-router-dom";
import { FaSignOutAlt, FaUser, FaCog } from "react-icons/fa";

const ProfileDropdown = ({ isOpen, onClose }) => {
    if (!isOpen) return null;

    return (
        <div className="absolute right-0 mt-4 w-56 rounded-md shadow-[0_0_15px_rgba(0,0,0,0.1)] z-20 bg-white">
            <div className="py-1" role="menu" aria-orientation="vertical">
                <Link 
                    to="/profile" 
                    className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    role="menuitem"
                    onClick={onClose}
                >
                    <FaUser className="mr-3" />
                    Mój profil
                </Link>
                <Link 
                    to="/settings" 
                    className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    role="menuitem"
                    onClick={onClose}
                >
                    <FaCog className="mr-3" />
                    Ustawienia
                </Link>
                <button
                    className="flex items-center w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    role="menuitem"
                    onClick={() => {
                        //logika wylogowania
                        onClose();
                    }}
                >
                    <FaSignOutAlt className="mr-3" />
                    Wyloguj się
                </button>
            </div>
        </div>
    );
};

export default ProfileDropdown;