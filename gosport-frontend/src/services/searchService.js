import api from './api';

export const fetchSearchResults = async (query) => {
  try {
    const response = await api.get('/search', {
      params: { query }
    });
    return response.data;
  } catch (error) {
    console.error('Error searching users:', error);
    throw error;
  }
};