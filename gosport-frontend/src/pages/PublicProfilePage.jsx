import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { FaUserCircle, FaPhone, FaMapMarkerAlt,FaUserPlus, FaUserCheck, FaHourglassHalf  } from "react-icons/fa";
import {profileApi} from "../services/profileApi";

const PublicProfilePage = () => {
    const { userId } = useParams();
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [connectionStatus, setConnectionStatus] = useState(null);
    const [isProcessing, setIsProcessing] = useState(false);

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const profileData = await profileApi.getPublicProfile(userId);
                setUser(profileData);
                setConnectionStatus(profileData.connectionStatus || null);
            } catch (err) {
                console.error("Failed to load profile:", err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchUserProfile();
    }, [userId]);

    const handleAddFriend = async () => {
        setIsProcessing(true);
        try {
            await profileApi.sendConnectionRequest(userId);
            setConnectionStatus("PENDING");
        } catch (error) {
            console.error("Failed to send connection request:", error);
        } finally {
            setIsProcessing(false);
        }
    };

    if (isLoading) return (
        <div>
            <div className="max-w-4xl mx-auto mt-30 p-8 text-center">Loading profile...</div>
        </div>
    );

    if (!user) return (
        <div>
            <div className="max-w-4xl mx-auto mt-30 p-8 text-center">Profile not found</div>
        </div>
    );

    return (
        <div>
            <div className="max-w-4xl mx-auto mt-30 bg-white rounded-lg shadow-lg overflow-hidden">
                <div className="h-48 bg-blue-100 relative">
                    <div className="absolute -bottom-16 left-8">
                        {user.profileImage ? (
                            <img 
                                src={user.profileImage} 
                                alt="Avatar" 
                                className="w-32 h-32 rounded-full border-4 border-white object-cover shadow-lg"
                                onError={(e) => {
                                    e.target.onerror = null;
                                    e.target.src = '/default-avatar.png';
                                }}
                            />
                        ) : (
                            <FaUserCircle className="w-32 h-32 text-gray-400 bg-white rounded-full border-4 border-white" />
                        )}
                    </div>
                    <div className="absolute -bottom-16 right-8 flex items-center space-x-2">
                        {connectionStatus === null && (
                            <button 
                                onClick={handleAddFriend}
                                disabled={isProcessing}
                                className={`flex items-center gap-2 px-4 py-2 rounded-full shadow transition
                                    ${isProcessing 
                                        ? 'bg-gray-400 cursor-not-allowed' 
                                        : 'bg-blue-600 hover:bg-blue-700 text-white'
                                    }`
                                }
                            >
                                {isProcessing ? (
                                    <>
                                        <FaHourglassHalf className="text-lg animate-spin" />
                                        <span>Processing...</span>
                                    </>
                                ) : (
                                    <>
                                        <FaUserPlus className="text-lg" />
                                        <span>Add Friend</span>
                                    </>
                                )}
                            </button>
                        )}

                        {connectionStatus === "PENDING" && (
                            <div className="flex items-center gap-2 text-yellow-700 bg-yellow-100 border border-yellow-300 px-4 py-2 rounded-full shadow">
                                <FaHourglassHalf className="text-lg" />
                                <span>Awaiting response</span>
                            </div>
                        )}

                        {connectionStatus === "ACCEPTED" && (
                            <div className="flex items-center gap-2 text-green-700 bg-green-100 border border-green-300 px-4 py-2 rounded-full shadow">
                                <FaUserCheck className="text-lg" />
                                <span>Friends</span>
                            </div>
                        )}
                    </div>
                </div>

                <div className="pt-20 px-8 pb-8">
                    <div className="mb-6">
                        <h1 className="text-3xl font-semibold text-gray-900">
                            {user.firstName} {user.lastName}
                        </h1>
                        <p className="text-xl text-gray-600 mt-2">
                            {user.headline || ""}
                        </p>
                    </div>

                    <div className="border-t border-b border-gray-200 py-6">
                        <div className="flex flex-wrap gap-8">
                            {user.mobile && (
                                <div className="flex items-center text-gray-600">
                                    <FaPhone className="mr-2 text-blue-500" />
                                    <span>{user.mobile}</span>
                                </div>
                            )}
                            
                            {(user.city || user.postalCode) && (
                                <div className="flex items-center text-gray-600">
                                    <FaMapMarkerAlt className="mr-2 text-blue-500" />
                                    <span>
                                        {user.city && `${user.city}, `}
                                        {user.postalCode}
                                    </span>
                                </div>
                            )}
                        </div>
                    </div>

                    <div>
                        <div className="mt-6">
                            <h2 className="text-xl font-semibold text-gray-900 mb-4">About</h2>
                            <p className="text-gray-600">
                                {user.bio || "No additional information provided"}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PublicProfilePage;