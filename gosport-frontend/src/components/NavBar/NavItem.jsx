import { Link } from "react-router-dom";

const NavItem = ({ to, icon, text, isActive }) => {
    return (
        <Link 
            to={to} 
            className={`flex flex-col items-center ${isActive ? 'text-blue-600' : 'text-gray-500 hover:text-blue-600'}`}
        >
            <div className="flex items-center justify-center h-8 w-8">
                {icon}
            </div>
            <span className="text-xs mt-1">{text}</span>
        </Link>
    );
};

export default NavItem;