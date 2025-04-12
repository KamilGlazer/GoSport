import { useEffect, useState } from "react";
import NavBar from "../components/NavBar/NavBar";
import { notificationApi } from "../services/NotificationApi";


const NotificationsPage = () => {
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        const fetchAndMark = async () => {
          const data = await notificationApi.getAll(); // ⬅️ najpierw pobierz
          setNotifications(data);                      // ⬅️ pokaż użytkownikowi
          setTimeout(() => {
            notificationApi.markAllAsRead();           // ⬅️ oznacz jako przeczytane z opóźnieniem
          }, 1000); // 1 sekunda opóźnienia, żeby użytkownik zauważył które były nieprzeczytane
        };
      
        fetchAndMark();
      }, []);

    return (
        <div className="mt-30 max-w-2xl mx-auto px-4">
            <div className="bg-white rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.05)] p-6">

                {notifications.length === 0 ? (
                    <p className="text-gray-500 text-center">You have no notifications at the moment.</p>
                ) : (
                    <ul className="space-y-2">
                        {notifications.map((n, index) => (
                            <li
                                key={index}
                                className={`flex items-start p-4 rounded-lg cursor-pointer transition  ${
                                    !n.read ? "bg-blue-50 hover:bg-blue-200" : "bg-white hover:bg-gray-100"
                                }`}
                            >
                                <div className="mr-4">
                                    {n.profileImage ? (
                                        <img
                                            src={n.profileImage}
                                            alt={`${n.firstName} ${n.lastName}`}
                                            className="w-12 h-12 rounded-full object-cover border border-gray-300"
                                        />
                                    ) : (
                                        <FaUserCircle className="w-12 h-12 text-gray-400" />
                                    )}
                                </div>

                                <div className="flex-1">
                                    <p className={`text-sm ${!n.read ? "font-semibold text-gray-800" : "text-gray-700"}`}>
                                        {n.message}
                                    </p>
                                    <p className="text-xs text-gray-400 mt-1">{new Date(n.createdAt).toLocaleString()}</p>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};

export default NotificationsPage;