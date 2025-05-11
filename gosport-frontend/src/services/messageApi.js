import api from "./api";

export const messageApi = {
  getConnectedUsers: async () => {
    const res = await api.get("/messages/getConnected");
    return res.data;
  },

  getMessagesWith: async (userId) => {
    const res = await api.get(`/messages/with/${userId}`);
    return res.data;
  },

  sendMessage: async (receiverId, content) => {
    const res = await api.post("/messages/send", { receiverId, content });
    return res.data;
  },
};