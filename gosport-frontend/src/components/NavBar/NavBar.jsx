import { Link, useLocation } from "react-router-dom";
import { FaHome, FaEnvelope, FaUserFriends, FaBell } from "react-icons/fa";
import logo from "../../assets/fans.png";
import SearchBar from "./SearchBar";
import NavItem from "./NavItem";
import ProfileNavItem from "./ProfileNavItem";
import { notificationApi } from "../../services/NotificationApi";
import { useEffect, useState } from "react";

const NavBar = () => {
  const location = useLocation();
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    const fetchUnread = async () => {
      const count = await notificationApi.getUnreadCount();
      setUnreadCount(count);
    };

    fetchUnread();
    const interval = setInterval(fetchUnread, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <nav className="bg-white shadow-sm fixed top-0 w-full z-10">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between items-center h-19">
          <div className="flex items-center">
            <Link to="/dashboard" className="flex-shrink-0">
              <div className="flex items-center space-x-2 text-2xl font-[700]">
                <img
                  src={logo}
                  alt="Logo"
                  className="h-13 w-13 scale-100 transition duration-700 ease-in-out hover:scale-115"
                />
              </div>
            </Link>
            <SearchBar />
          </div>

          <div className="flex items-center space-x-6">
            <NavItem
              to="/dashboard"
              icon={<FaHome size={24} />}
              text="Main Page"
              isActive={location.pathname === "/dashboard"}
            />
            <NavItem
              to="/dashboard/notifications"
              icon={
                <div className="relative">
                  <FaBell size={24} />
                  {unreadCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-red-600 text-white text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center">
                      {unreadCount}
                    </span>
                  )}
                </div>
              }
              text="Notifications"
              isActive={location.pathname === "/dashboard/notifications"}
            />
            <NavItem
              to="/dashboard/messages"
              icon={<FaEnvelope size={24} />}
              text="Messages"
              isActive={location.pathname === "/dashboard/messages"}
            />
            <NavItem
              to="/dashboard/trainers"
              icon={<FaUserFriends size={24} />}
              text="Trainers"
              isActive={location.pathname === "/dashboard/trainers"}
            />
            <ProfileNavItem />
          </div>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
