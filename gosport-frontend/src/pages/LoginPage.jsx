import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loginStart, loginSuccess, loginFailure } from "../store/authSlice";
import { login } from "../services/authApi";
import { useNavigate } from "react-router-dom";
import FloatingLabelInput from "../components/FloatingLabelInput";
import logo from "../assets/fans.png";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loading, error } = useSelector((state) => state.auth);

  const handleLogin = async (e) => {
    e.preventDefault();
    dispatch(loginStart());
    try {
      const data = await login({ email, password });
      dispatch(loginSuccess(data));
      navigate("/dashboard");
    } catch (err) {
      dispatch(loginFailure(err.response?.data?.message || "Blad logowania"));
    }
  };

  return (
    <div className="flex flex-col justify-between h-screen bg-gray-100">
      <div className="flex justify-center items-center h-screen bg-gray-100">
        <div className="bg-white p-6 rounded-xl shadow-lg w-96">
          <div className="flex flex-col gap-2 justify-center items-center">
            <div>
              <a href="/">
                {" "}
                <img
                  src={logo}
                  className="h-10 w-10 scale-150 mb-2 transition duration-700 ease-in-out hover:scale-180"
                />
              </a>
            </div>
            <div className="flex">
              <span className="text-2xl font-bold mr-1">
                <span className="text-blue-600">GO</span>SPORT
              </span>
              <h2 className="text-2xl font-bold ">LOG IN</h2>
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
          <form onSubmit={handleLogin}>
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
            <button
              type="submit"
              className="w-full bg-blue-500 text-white font-semibold p-2 rounded-2xl  hover:bg-blue-600"
              disabled={loading}
            >
              {loading ? "Logging in..." : "LOG IN"}
            </button>
            <p className="text-center mt-4 text-xs text-gray-800">
              Don't have an account?{" "}
              <a href="/register" className="underline font-semibold">
                Sign up
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

export default LoginPage;
