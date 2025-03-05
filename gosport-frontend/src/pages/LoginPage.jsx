import { useState } from "react";
import { useDispatch,useSelector } from "react-redux";
import { loginStart,loginSuccess,loginFailure } from "../store/authSlice";
import { login } from "../services/authApi";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
    const [email,setEmail] = useState("");
    const [password, setPassword] = useState("");
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {loading,error} = useSelector((state) => state.auth);


    const handleLogin = async (e) => {
        e.preventDefault();
        dispatch(loginStart());
        try{
            const data = await login({email,password});
            dispatch(loginSuccess(data));
            navigate("/dashboard");
        }catch (err) {
            dispatch(loginFailure(err.response?.data?.message || "Blad logowania"));
        }
    };


    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <div className="bg-white p-6 rounded-lg shadow-lg w-96">
                <h2 className="text-2xl font-bold mb-4">Logowanie</h2>
                {error && <p className="text-red-500 mb-2">{error}</p>}
                <form onSubmit={handleLogin}>
                    <input
                        type="email"
                        placeholder="Email"
                        className="w-full p-2 border rounded mb-3"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Hasło"
                        className="w-full p-2 border rounded mb-3"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600"
                        disabled={loading}
                    >
                        {loading ? "Logowanie..." : "Zaloguj się"}
                    </button>
                </form>
            </div>
        </div>
    )

};

export default LoginPage;