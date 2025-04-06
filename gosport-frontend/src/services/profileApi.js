import api from "./api"

export const profileApi = {
    getAvatar: async () => {
        try {
          const response = await api.get('/profile/avatar', {
            responseType: 'blob'
          });
          return URL.createObjectURL(response.data);
        } catch (error) {
          console.error('Error fetching avatar:', error);
          return null;
        }
      },
      getProfile: async () => {
        try {
          const response = await api.get('/profile');
          return response.data;
        } catch (error) {
          console.error('Error fetching profile:', error);
          throw error;
        }
      }
};

