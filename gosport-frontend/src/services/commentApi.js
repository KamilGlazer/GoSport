import api from "./api";

export const commentApi = {
  getComments: async (postId) => {
    const res = await api.get(`/comments/${postId}`);
    return res.data;
  },
  addComment: async (payload) => {
    const res = await api.post("/comments", payload);
    return res.data;
  },
  deleteComment: async (commentId) => {
    await api.delete(`/comments/delete/${commentId}`);
  },
};