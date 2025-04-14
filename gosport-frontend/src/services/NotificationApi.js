import api from "./api";

export const notificationApi = {
  getUnreadCount: async () => {
    try {
      const response = await api.get("/notifications/unread/count");
      return response.data.unreadCount;
    } catch (error) {
      console.error("Error fetching unread notifications count:", error);
      return 0;
    }
  },

  getAll: async () => {
    try {
      const response = await api.get("/notifications");
      return response.data;
    } catch (error) {
      console.error("Error fetching notifications:", error);
      return [];
    }
  },

  markAllAsRead: async () => {
    try {
      await api.patch("/notifications");
    } catch (error) {
      console.error("Error marking notifications as read:", error);
    }
  },

  acceptConnection: async (id) => {
    try {
      await api.patch(`/connection/${id}?action=ACCEPTED`);
    } catch (error) {
      console.error("Error accepting connection:", error);
    }
  },

  deleteConnection: async (id) => {
    try {
      await api.patch(`/connection/${id}?action=DELETE`);
    } catch (error) {
      console.error("Error deleting connection request:", error);
    }
  },
  
};