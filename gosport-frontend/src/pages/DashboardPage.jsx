import { useEffect, useState } from "react";
import NavBar from "../components/NavBar/NavBar";
import LoadingBar from "../components/LoadingBar";
import { useDispatch, useSelector } from "react-redux";
import {profileApi} from "../services/profileApi"
import { setAvatar,setUserProfile } from '../store/authSlice';


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
        <div className="pt-16">
            <NavBar />
            <main className="max-w-7xl mx-auto px-4 py-6">
                <section className="text-center">
                    <h1 className="text-2xl font-bold">Welcome to Your Dashboard</h1>
                    <p className="mt-4 text-gray-600">Start building your application here</p>
                </section>
            </main>
        </div>
    );
};

export default DashboardPage;