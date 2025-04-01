import { Link, useLocation } from "react-router-dom";
import { FaHome, FaEnvelope, FaUserFriends, FaUserCircle } from "react-icons/fa";
import Logo from "../Logo";
import SearchBar from "./SearchBar";
import NavItem from "./NavItem";

const NavBar = () => {
    const location = useLocation();

    return (
        <nav className="bg-white shadow-sm fixed top-0 w-full z-10">
            <div className="max-w-7xl mx-auto px-4">
                <div className="flex justify-between items-center h-19">
                    <div className="flex items-center">
                        <Link to="/" className="flex-shrink-0">
                            <Logo />
                        </Link>
                        <SearchBar />
                    </div>

                    <div className="flex items-center space-x-6">
                        <NavItem 
                            to="/" 
                            icon={<FaHome size={24} />} 
                            text="Strona główna" 
                            isActive={location.pathname === '/'} 
                        />
                        <NavItem 
                            to="/messages" 
                            icon={<FaEnvelope size={24} />} 
                            text="Wiadomości" 
                            isActive={location.pathname === '/messages'} 
                        />
                        <NavItem 
                            to="/trainers" 
                            icon={<FaUserFriends size={24} />} 
                            text="Trenerzy" 
                            isActive={location.pathname === '/trainers'} 
                        />
                        <NavItem 
                            to="/profile" 
                            icon={<FaUserCircle size={24} />} 
                            text="Ja" 
                            isActive={location.pathname === '/profile'} 
                        />
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default NavBar;