import { useState } from "react";
import { register } from "../services/authApi";
import { useNavigate } from "react-router-dom";
import logo from "../assets/fans.png";
import { FaUserLarge } from "react-icons/fa6";
import { FiUser } from "react-icons/fi";
import FloatingLabelInput from "../components/FloatingLabelInput";

const RegisterPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState(null);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [acceptTerms, setAcceptTerms] = useState(false);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      setError("Passwords dont match.");
      return;
    }

    if (!acceptTerms) {
      setError("You have to accept private policy.");
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
    <div className="flex flex-col justify-between h-screen bg-gray-100">
      <div className="flex justify-center items-center h-screen bg-gray-100">
        <div className="bg-white p-6 rounded-xl shadow-lg w-96">
          <div className="flex flex-col gap-2 justify-center items-center">
            <div>
              <a href="/">
                <img
                  src={logo}
                  className="h-10 w-10 scale-150 mb-2  transition duration-700 ease-in-out hover:scale-180"
                />
              </a>
            </div>
            <div className="flex">
              <span className="text-2xl font-bold mr-1">
                <span className="text-blue-600">GO</span>SPORT
              </span>
              <h2 className="text-2xl font-bold ">REGISTER</h2>
            </div>
            <div className="mb-3">
              <span className="font-normal text-gray-600 leading-tight text-sm">
                <span className="text-blue-600 font-medium ">Train </span>
                with us.
                <span className="text-blue-600 font-medium"> Talk </span>
                with us.
              </span>
            </div>
          </div>
          {error && <p className="text-red-500 mb-4 text-center">{error}</p>}
          <form onSubmit={handleRegister}>
            <FloatingLabelInput
              type="text"
              label="First Name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
            <FloatingLabelInput
              type="text"
              label="Last Name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
            <FloatingLabelInput
              type="date"
              label=""
              value={dateOfBirth}
              onChange={(e) => setDateOfBirth(e.target.value)}
            />
            <FloatingLabelInput
              type="email"
              label="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <FloatingLabelInput
              type="password"
              label="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <FloatingLabelInput
              type="password"
              label="Confirm Password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            <div className="flex items-center mt-1 mb-4 justify-center">
              <input
                type="checkbox"
                id="terms"
                checked={acceptTerms}
                onChange={(e) => setAcceptTerms(e.target.checked)}
                className="w-5 h-5 mr-2 accent-blue-500"
              />
              <label htmlFor="terms" className="text-sm text-gray-700">
                I agree to the{" "}
                <a href="#" className="underline font-semibold">
                  Privacy Policy
                </a>
                .
              </label>
            </div>
            <button
              type="submit"
              className="w-full bg-blue-500 text-white font-semibold p-2 rounded-2xl  hover:bg-blue-600"
              disabled={loading}
            >
              {loading ? "Rejestrowanie..." : "CREATE ACCOUNT"}
            </button>
            <p className="text-center mt-4 text-xs text-gray-800">
              Already have an account?{" "}
              <a href="/login" className="underline font-semibold">
                Log in
              </a>{" "}
            </p>
          </form>
        </div>
      </div>

      <footer className="text-center text-gray-600 text-sm py-4 bg-white shadow-inner">
        <p className="mb-2">GoSport © 2025</p>
        <div className="flex justify-center space-x-4 flex-wrap">
          <a href="#" className="hover:underline">
            User Agreement
          </a>
          <a href="#" className="hover:underline">
            Privacy Policy
          </a>
          <a href="#" className="hover:underline">
            Community Guidelines
          </a>
          <a href="#" className="hover:underline">
            Cookie Policy
          </a>
          <a href="#" className="hover:underline">
            Copyright Policy
          </a>
          <a href="#" className="hover:underline">
            Send Feedback
          </a>
        </div>
      </footer>
    </div>
  );
};

export default RegisterPage;
