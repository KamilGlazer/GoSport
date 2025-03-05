import logo from "../assets/fans.png"

const Logo = () => {
    return (
        <div className="flex items-center space-x-2 text-2xl font-bold">
            <img src={logo} alt="Logo" className="h-10 w-10 scale-130" />
            <span>
                <span className="text-blue-600">Go</span>Sport
            </span>
        </div>
    );
}

export default Logo;