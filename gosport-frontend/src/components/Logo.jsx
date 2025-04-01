import logo from "../assets/fans.png"

const Logo = () => {
    return (
        <div className="flex items-center space-x-2 text-2xl font-[700]">
            <img src={logo} alt="Logo" className="h-13 w-13 scale-110" />
            <span className="text-2xl">
                <span className="text-blue-600">Go</span>Sport
            </span>
        </div>
    );
}

export default Logo;