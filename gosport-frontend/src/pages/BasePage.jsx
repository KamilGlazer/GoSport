import { Link } from "react-router-dom";
import Logo from "../components/Logo";

// Importujemy kilka zdjęć do tła
import base1 from "../assets/base1.jpg";
import base2 from "../assets/base2.jpg";
import base3 from "../assets/base3.jpg";

const BasePage = () => {
    return (
        <div className="relative w-full min-h-screen bg-gray-100 flex flex-col items-center">
        {/* NAVBAR */}
        <header className="w-4/5 max-w-[1400px] bg-gray-100 flex">
            <div className="w-4/5 max-w-[1400px] mx-auto flex justify-between items-center px-8 py-4">
                <Logo />

                <div className="flex space-x-8 items-center">
                <a href="#about" className="nav-link">About us</a>
                <a href="#trainers" className="nav-link">For trainers</a>
                <a href="#athletes" className="nav-link">For athletes</a>
                <Link to="/login" className="text-base font-medium text-white bg-blue-500 px-10 py-2 rounded-3xl hover:bg-blue-600">
                    Log in
                </Link>
                </div>
            </div>
        </header>

        
        
        </div>
    );
};

export default BasePage;
