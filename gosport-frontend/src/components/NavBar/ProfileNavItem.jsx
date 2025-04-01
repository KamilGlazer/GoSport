import { useState } from 'react';
import { FaUserCircle } from "react-icons/fa";
import ProfileDropdown from "./ProfileDropdown";

const ProfileNavItem = () => {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    return (
        <div className="relative">
            <button 
                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                className="flex flex-col items-center text-gray-500 hover:text-blue-600"
            >
                <div className="flex items-center justify-center h-8 w-8">
                    <FaUserCircle size={24} />
                </div>
                <span className="text-xs mt-1">Me</span>
            </button>
            <ProfileDropdown 
                isOpen={isDropdownOpen} 
                onClose={() => setIsDropdownOpen(false)} 
            />
        </div>
    );
};

export default ProfileNavItem;