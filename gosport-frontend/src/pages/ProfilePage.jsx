import { useSelector } from "react-redux";
import { FaUserCircle, FaPhone, FaMapMarkerAlt } from "react-icons/fa";
import NavBar from "../components/NavBar/NavBar";



const ProfilePage = () => {
    const user = useSelector(state => state.auth.user);
    
    return (
        <div>
        <div className="max-w-4xl mx-auto mt-30 bg-white rounded-lg shadow-lg overflow-hidden">
            <div className="h-48 bg-blue-100 relative">
                <div className="absolute -bottom-16 left-8">
                    {user?.avatar ? (
                        <img 
                            src={user.avatar} 
                            alt="Avatar" 
                            className="w-32 h-32 rounded-full border-4 border-white object-cover shadow-lg"
                        />
                    ) : (
                        <FaUserCircle className="w-32 h-32 text-gray-400 bg-white rounded-full border-4 border-white" />
                    )}
                </div>
            </div>

            <div className="pt-20 px-8 pb-8">
                <div className="mb-6">
                    <h1 className="text-3xl font-semibold text-gray-900">
                        {user?.profile?.firstName} {user?.profile?.lastName}
                    </h1>
                    <p className="text-xl text-gray-600 mt-2">
                        {user?.profile?.headline || "Professional Headline"}
                    </p>
                </div>

                <div className="border-t border-b border-gray-200 py-6">
                    <div className="flex flex-wrap gap-8">
                        {user?.profile?.mobile && (
                            <div className="flex items-center text-gray-600">
                                <FaPhone className="mr-2 text-blue-500" />
                                <span>{user.profile.mobile}</span>
                            </div>
                        )}
                        
                        {(user?.profile?.city || user?.profile?.postalCode) && (
                            <div className="flex items-center text-gray-600">
                                <FaMapMarkerAlt className="mr-2 text-blue-500" />
                                <span>
                                    {user.profile.city && `${user.profile.city}, `}
                                    {user.profile.postalCode}
                                </span>
                            </div>
                        )}
                    </div>
                </div>


                <div>
                    <div className="mt-6">
                        <h2 className="text-xl font-semibold text-gray-900 mb-4">About</h2>
                        <p className="text-gray-600">
                            {user?.profile?.bio || "No additional information provided"}
                        </p>
                    </div>
                </div>
            </div>
        </div>
        </div>
    );
};

export default ProfilePage;