import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import NavBar from "../components/NavBar/NavBar";
import api from "../services/api";

const PublicProfilePage = () => {
    const { userId } = useParams();
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await api.get(`/profile/${userId}`);
                setUser(response.data);
            } catch (err) {
                setError("Nie udało się załadować profilu użytkownika");
            } finally {
                setIsLoading(false);
            }
        };

        fetchUserProfile();
    }, [userId]);

    if (isLoading) return <div>Ładowanie profilu...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div>
            <NavBar/>
            <div className="max-w-4xl mx-auto mt-30 bg-white rounded-lg shadow-lg overflow-hidden">
                <h1 className="text-3xl font-semibold text-gray-900">
                    {user.firstName} {user.lastName}
                </h1>
            </div>
        </div>
    );
};

export default PublicProfilePage;