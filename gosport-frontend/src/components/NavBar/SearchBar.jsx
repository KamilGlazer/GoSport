import { FaSearch } from "react-icons/fa";

const SearchBar = () => {
    return (
        <div className="relative hidden md:block ml-6">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <FaSearch className="text-gray-400" />
            </div>
            <input
                type="text"
                placeholder="Search..."
                className="block w-full pl-10 pr-20 pr-3 py-2 border border-gray-300 rounded-2xl leading-5 bg-gray-50 placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            />
        </div>
    );
};

export default SearchBar;