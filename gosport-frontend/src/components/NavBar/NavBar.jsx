import { Link, useLocation } from "react-router-dom";
import { useState } from "react";
import { FaHome, FaEnvelope, FaUserFriends, FaUserCircle } from "react-icons/fa";
import logo from "../../assets/fans.png"
import SearchBar from "./SearchBar";
import NavItem from "./NavItem";
import ProfileDropdown from "./ProfileDropdown";
import ProfileNavItem from "./ProfileNavItem";

const NavBar = () => {
    const location = useLocation();

    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    const closeDropdown = () => {
        setIsDropdownOpen(false);
    };

    return (
        <nav className="bg-white shadow-sm fixed top-0 w-full z-10">
            <div className="max-w-7xl mx-auto px-4">
                <div className="flex justify-between items-center h-19">
                    <div className="flex items-center">
                        <Link to="/" className="flex-shrink-0">
                            <div className="flex items-center space-x-2 text-2xl font-[700]">
                                        <img src={logo} alt="Logo" className="h-13 w-13 scale-100 transition duration-700 ease-in-out hover:scale-115" />
                            </div>
                        </Link>
                        <SearchBar />
                    </div>

                    <div className="flex items-center space-x-6">
                        <NavItem 
                            to="/" 
                            icon={<FaHome size={24} />} 
                            text="Main Page" 
                            isActive={location.pathname === '/'} 
                        />
                        <NavItem 
                            to="/messages" 
                            icon={<FaEnvelope size={24} />} 
                            text="Messages" 
                            isActive={location.pathname === '/messages'} 
                        />
                        <NavItem 
                            to="/trainers" 
                            icon={<FaUserFriends size={24} />} 
                            text="Trainers" 
                            isActive={location.pathname === '/trainers'} 
                        />
                        <ProfileNavItem />
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default NavBar;