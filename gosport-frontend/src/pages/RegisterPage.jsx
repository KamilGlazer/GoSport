import { useState } from "react";
import { register } from "../services/authApi";
import { useNavigate } from "react-router-dom";
import logo from "../assets/fans.png";
import { FaUserLarge } from "react-icons/fa6";

const RegisterPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState(null);
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();


    const handleRegister = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
          setError("Hasła nie są takie same");
          return;
        }
        setLoading(true);
        try {
          await register({ email, password });
          navigate("/login"); 
        } catch (err) {
          setError(err.response?.data?.message || "Błąd rejestracji");
        } finally {
          setLoading(false);
        }
      };

      return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
          <div className="bg-white p-6 rounded-xl shadow-lg w-96">
            <div className="flex flex-col gap-2 justify-center items-center">
              <div>
                <img src={logo} className="h-10 w-10 scale-150 mb-2"/>
              </div>
              <div className="flex">
                <span className="text-2xl font-bold mr-1">
                  <span className="text-blue-600">GO</span>SPORT
                </span>
                <h2 className="text-2xl font-bold ">REGISTER</h2>
              </div>  
              <div className="mb-3">
                <span className="font-normal text-gray-600 leading-tight text-sm">
                  <span className="text-blue-600 font-medium " >Train </span>
                   with us.
                  <span className="text-blue-600 font-medium"> Talk </span>
                   with us.
                </span>
              </div>
            </div>
            {error && <p className="text-red-500 mb-2">{error}</p>}
            <form onSubmit={handleRegister}>
            <div className="relative w-full">
              <FaUserLarge  className="absolute left-3 top-1/3 transform -translate-y-1 scale-110 " />
              <input
                type="text"
                placeholder="First Name"
                className="w-full pl-10 p-2 border rounded-2xl mb-3"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                required
              />
            </div>
               <input
                type="text"
                placeholder="Last Name"
                className="w-full p-2 border rounded-2xl mb-3"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                required
              />
              <input
                type="date"
                placeholder="Last Name"
                className="w-full p-2 border rounded-2xl mb-3"
                value={dateOfBirth}
                onChange={(e) => setDateOfBirth(e.target.value)}
                required
              />
              <input
                type="email"
                placeholder="Email"
                className="w-full p-2 border rounded-2xl  mb-3"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
              <input
                type="password"
                placeholder="Password"
                className="w-full p-2 border rounded-2xl  mb-3"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <input
                type="password"
                placeholder="Confirm password"
                className="w-full p-2 border rounded-2xl  mb-3"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
              <button
                type="submit"
                className="w-full bg-blue-500 text-white font-semibold p-2 rounded-2xl  hover:bg-blue-600"
                disabled={loading}
              >
                {loading ? "Rejestrowanie..." : "CREATE ACCOUNT"}
              </button>
            </form>
          </div>
        </div>
      );
};


export default RegisterPage;