import { Navigate, Outlet } from "react-router-dom";
import { useEffect,useState } from "react";
import api from "../services/api";

const PrivateRoute = () =>{
    const[isValid,setIsValid] = useState(null);
    const token = localStorage.getItem("token");

    useEffect(() => {
        const validateToken = async () => {
            if(!token) {
                setIsValid(false);
                return;
            }

            try{
                const response = await api.get("/auth/validate", {
                    headers : { Authorization : `Bearer ${token}`},
                });
                if(response.status===200){
                    setIsValid(true);
                }else{
                    throw new Error("Invalid token");
                }
            }catch {
                localStorage.removeItem("token");
                setIsValid(false);
            }
        };
        validateToken();
    },[token]);

    if(isValid == null) return <p>SPrawdzanie tokena....</p>;
    return isValid ? <Outlet /> : <Navigate to="/login"/>;
};


export default PrivateRoute;