import { Link, useLocation } from "react-router-dom";
import { FaHome, FaEnvelope, FaUserFriends, FaBell } from "react-icons/fa";
import logo from "../../assets/fans.png"
import SearchBar from "./SearchBar";
import NavItem from "./NavItem";
import ProfileNavItem from "./ProfileNavItem";

const NavBar = () => {
    const location = useLocation();
    return (
        <nav className="bg-white shadow-sm fixed top-0 w-full z-10">
            <div className="max-w-7xl mx-auto px-4">
                <div className="flex justify-between items-center h-19">
                    <div className="flex items-center">
                        <Link to="/dashboard" className="flex-shrink-0">
                            <div className="flex items-center space-x-2 text-2xl font-[700]">
                                        <img src={logo} alt="Logo" className="h-13 w-13 scale-100 transition duration-700 ease-in-out hover:scale-115" />
                            </div>
                        </Link>
                        <SearchBar />
                    </div>

                    <div className="flex items-center space-x-6">
                        <NavItem 
                            to="/dashboard" 
                            icon={<FaHome size={24} />} 
                            text="Main Page" 
                            isActive={location.pathname === '/dashboard'} 
                        />
                        <NavItem 
                            to="/dashboard/notifications" 
                            icon={<FaBell size={24} />} 
                            text="Notifications" 
                            isActive={location.pathname === '/dashboard/notifications'} 
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