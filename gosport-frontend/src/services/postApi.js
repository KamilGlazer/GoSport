import api from "./api";

export const postApi = {
  getFeed: async () => {
    const res = await api.get("/posts/feed");
    return res.data;
  },
  createPost: async (payload) => {
    const res = await api.post("/posts", payload);
    return res.data;
  },
  toggleLike: async (postId) => {
    const res = await api.post(`/posts/${postId}/like`);
    return res.data;
  },
  deletePost: async (postId) => {
    await api.delete(`/posts/delete/${postId}`);
  },
  getMyPosts: async () => {
  const res = await api.get("/posts/my");
  return res.data;
},
};