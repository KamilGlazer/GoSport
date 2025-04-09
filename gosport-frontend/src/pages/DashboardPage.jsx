import { useEffect, useState } from "react";
import NavBar from "../components/NavBar/NavBar";
import LoadingBar from "../components/LoadingBar";
import { useDispatch} from "react-redux";
import {profileApi} from "../services/profileApi"
import { setAvatar,setUserProfile } from '../store/authSlice';
import { Outlet } from "react-router-dom";



const LOADING_DELAY = 1500;

const DashboardPage = () => {
    const [isLoading, setIsLoading] = useState(true);
    const dispatch = useDispatch();

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const [avatarUrl, profileData] = await Promise.all([
                    profileApi.getAvatar(),
                    profileApi.getProfile() 
                ]);
                
                console.log("DASHOBARD!");
                
                dispatch(setAvatar(avatarUrl));
                dispatch(setUserProfile(profileData));
                
                await new Promise(resolve => setTimeout(resolve, LOADING_DELAY));
            } catch (error) {
                console.error("Error during initial data loading:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchInitialData();
    }, [dispatch]);

    if (isLoading) {
        return (
            <div className="fixed inset-0 flex items-center justify-center bg-white z-50">
                <LoadingBar />
            </div>
        );
    }

    return (
        <div>
            <NavBar />
            <Outlet /> 
      </div>
    );
};

export default DashboardPage;