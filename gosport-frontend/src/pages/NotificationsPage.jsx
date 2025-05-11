import { useEffect, useState } from "react";
import NavBar from "../components/NavBar/NavBar";
import { notificationApi } from "../services/NotificationApi";
import { FaUserCircle } from "react-icons/fa";

const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [acceptedRequests, setAcceptedRequests] = useState([]);
  const [deletedRequests, setDeletedRequests] = useState([]);

  useEffect(() => {
    const fetchAndMark = async () => {
      const data = await notificationApi.getAll();
      setNotifications(data);
      setTimeout(() => {
        notificationApi.markAllAsRead();
      }, 1000);
    };

    fetchAndMark();
  }, []);

  const handleAccept = async (userId) => {
    await notificationApi.acceptConnection(userId);
    setAcceptedRequests((prev) => [...prev, userId]);
  };

  const handleDelete = async (userId) => {
    await notificationApi.deleteConnection(userId);
    setDeletedRequests((prev) => [...prev, userId]);
  };

  return (
    <div className="mt-30 max-w-2xl mx-auto px-4">
      <div className="bg-white rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.05)] p-6">
        {notifications.length === 0 ? (
          <p className="text-gray-500 text-center">
            You have no notifications at the moment.
          </p>
        ) : (
          <ul className="space-y-2">
            {notifications.map((n, index) => (
              <li
                key={index}
                className={`flex items-start p-4 rounded-lg cursor-pointer transition ${
                  !n.read
                    ? "bg-blue-50 hover:bg-blue-200"
                    : "bg-white hover:bg-gray-100"
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
                  <p
                    className={`text-sm ${
                      !n.read ? "font-semibold text-gray-800" : "text-gray-700"
                    }`}
                  >
                    {n.message}
                  </p>
                  <p className="text-xs text-gray-400 mt-1">
                    {new Date(n.createdAt).toLocaleString()}
                  </p>

                  {n.type === "CONNECTION_REQUEST" && (
                    <div className="flex gap-2 mt-2">
                      {acceptedRequests.includes(n.userId) ? (
                        <button
                          disabled
                          className="px-3 py-1 text-sm bg-gray-400 text-white rounded cursor-not-allowed"
                        >
                          Accepted
                        </button>
                      ) : deletedRequests.includes(n.userId) ? (
                        <button
                          disabled
                          className="px-3 py-1 text-sm bg-gray-400 text-white rounded cursor-not-allowed"
                        >
                          Deleted
                        </button>
                      ) : (
                        <>
                          <button
                            onClick={() => handleAccept(n.userId)}
                            className="px-3 py-1 text-sm bg-blue-500 text-white rounded hover:bg-blue-600"
                          >
                            Accept
                          </button>

                          <button
                            onClick={() => handleDelete(n.userId)}
                            className="px-3 py-1 text-sm bg-red-500 text-white rounded hover:bg-red-600"
                          >
                            Delete invitation
                          </button>
                        </>
                      )}
                    </div>
                  )}
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
