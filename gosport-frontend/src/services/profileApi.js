import api from "./api";

export const profileApi = {
  getAvatar: async () => {
    try {
      const response = await api.get("/profile/avatar", {
        responseType: "blob",
      });
      return URL.createObjectURL(response.data);
    } catch (error) {
      console.error("Error fetching avatar:", error);
      return null;
    }
  },
  getProfile: async () => {
    try {
      const response = await api.get("/profile");
      return response.data;
    } catch (error) {
      console.error("Error fetching profile:", error);
      throw error;
    }
  },
  getPublicProfile: async (userId) => {
    try {
      const response = await api.get(`/profile/${userId}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching public profile:", error);
      throw error;
    }
  },
  sendConnectionRequest: async (userId) => {
    try {
      await api.post(`/connection/${userId}`);
      return true;
    } catch (error) {
      console.error("Error sending connection request:", error);
      throw error;
    }
  },
  editUserProfile: async (data) => {
    return await api.put("/profile", data);
  },
  toggleTrainerStatus: async () => {
    await api.post("/trainers/toggle");
  },
  getTrainerStatus: async () => {
    const res = await api.get("/trainers/status");
    return res.data;
  },
  searchTrainers: async ({ city, postalCode }) => {
  const res = await api.get('/trainers/search', { params: { city, postalCode } });
  return res.data;
}
};
