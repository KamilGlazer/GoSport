import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { postApi } from "../services/postApi";
import { commentApi } from "../services/commentApi";

const DashboardHome = () => {
  const [posts, setPosts] = useState([]);
  const [content, setContent] = useState("");
  const user = useSelector((state) => state.auth.user);
  const userId = user?.profile?.userId;
  const [showConfirm, setShowConfirm] = useState(false);
  const [postToDelete, setPostToDelete] = useState(null);
  const [expandedPostId, setExpandedPostId] = useState(null);
  const [comments, setComments] = useState({});
  const [newComment, setNewComment] = useState("");

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const data = await postApi.getFeed();
        setPosts(data);
      } catch (err) {
        console.error("Failed to fetch posts", err);
      }
    };

    fetchPosts();
  }, []);

  const handleCreatePost = async () => {
    if (!content.trim()) return;
    try {
      const newPost = await postApi.createPost({ content });
      setPosts((prev) => [newPost, ...prev]);
      setContent("");
    } catch (err) {
      console.error("Failed to create post", err);
    }
  };

  return (
    <div className="max-w-2xl mx-auto pt-24 px-4 space-y-6">
      <div className="bg-white rounded-2xl border border-gray-200 shadow-sm p-5">
        <div className="flex items-start gap-3">
          <img
            src={user?.avatar}
            alt="Avatar"
            className="w-11 h-11 rounded-full object-cover"
          />
          <textarea
            className="w-full border border-gray-300 rounded-xl px-4 py-2 text-sm text-gray-800 resize-none focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows={3}
            placeholder="Share your thoughts..."
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />
        </div>
        <div className="flex justify-end mt-3">
          <button
            onClick={handleCreatePost}
            className="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold rounded-full shadow transition"
          >
            Post
          </button>
        </div>
      </div>

      {posts.map((post) => (
        <div key={post.id} className="bg-white rounded-2xl border border-gray-200 shadow-sm p-5 space-y-4">
          <div className="flex items-center gap-3">
            <img src={post.authorProfileImage} alt="Author" className="w-10 h-10 rounded-full object-cover" />
            <div>
              <div className="font-medium text-gray-900 text-sm">
                {post.authorFirstName} {post.authorLastName}
              </div>
              <div className="text-xs text-gray-500">{new Date(post.createdAt).toLocaleString()}</div>
            </div>
          </div>

          <div className="text-gray-800 text-sm whitespace-pre-wrap leading-relaxed break-words">
            {post.content}
          </div>

          <div className="flex justify-between items-center border-t border-gray-100 pt-3 text-sm">
            <div className="flex items-center gap-4">
              <button
                onClick={async () => {
                  try {
                    const updated = await postApi.toggleLike(post.id);
                    setPosts((prev) => prev.map((p) => (p.id === post.id ? updated : p)));
                  } catch (err) {
                    console.error("Error liking post", err);
                  }
                }}
                className={`px-3 py-1 rounded-full border transition font-medium ${
                  post.likedByMe
                    ? "bg-blue-600 text-white border-blue-600 hover:bg-blue-700"
                    : "border-blue-500 text-blue-600 hover:bg-blue-50"
                }`}
              >
                👍 {post.likedByMe ? "Liked" : "Like"}
              </button>
              <span className="text-gray-500">{post.likeCount} likes</span>
            </div>

            {post.authorId === userId && (
              <button
                onClick={() => {
                  setPostToDelete(post.id);
                  setShowConfirm(true);
                }}
                className="px-3 py-1 rounded-full border border-red-500 text-red-500 hover:bg-red-50 transition font-medium"
              >
                Delete
              </button>
            )}
          </div>

          <button
            className="text-blue-600 text-sm hover:underline"
            onClick={async () => {
              if (expandedPostId === post.id) {
                setExpandedPostId(null);
              } else {
                const data = await commentApi.getComments(post.id);
                setComments((prev) => ({ ...prev, [post.id]: data }));
                setExpandedPostId(post.id);
              }
            }}
          >
            {expandedPostId === post.id ? "Hide comments" : "Show comments"}
          </button>

          {expandedPostId === post.id && (
            <div className="mt-4 space-y-4">
              <div className="flex gap-3 items-start">
                <img src={user?.avatar} alt="Avatar" className="w-8 h-8 rounded-full object-cover" />
                <textarea
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  rows={2}
                  placeholder="Write a comment..."
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                />
                <button
                  onClick={async () => {
                    if (!newComment.trim()) return;
                    const created = await commentApi.addComment({ postId: post.id, content: newComment });
                    setComments((prev) => ({ ...prev, [post.id]: [...prev[post.id], created] }));
                    setNewComment("");
                  }}
                  className="text-sm px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                >
                  Send
                </button>
              </div>

              <div className="space-y-2">
                {(comments[post.id] || []).map((comment) => (
                  <div key={comment.id} className="flex items-start gap-3">
                    <img
                      src={comment.authorProfileImage}
                      alt="Author"
                      className="w-8 h-8 rounded-full object-cover"
                    />
                    <div className="bg-gray-100 rounded-lg px-4 py-2 w-full">
                      <div className="text-sm font-medium text-gray-800">
                        {comment.authorFirstName} {comment.authorLastName}
                      </div>
                      <div className="text-sm text-gray-700 whitespace-pre-wrap">{comment.content}</div>
                      <div className="text-xs text-gray-400 mt-1 flex justify-between">
                        <span>{new Date(comment.createdAt).toLocaleString()}</span>
                        {comment.authorId === userId && (
                          <button
                            onClick={async () => {
                              await commentApi.deleteComment(comment.id);
                              setComments((prev) => ({
                                ...prev,
                                [post.id]: prev[post.id].filter((c) => c.id !== comment.id),
                              }));
                            }}
                            className="text-red-500 hover:underline"
                          >
                            Delete
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      ))}

      {showConfirm && (
  <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm">
    <div className="bg-white rounded-2xl shadow-xl p-6 w-full max-w-md animate-fade-in">
      <div className="flex flex-col items-center text-center space-y-4">
        <div className="text-red-500">
          <svg xmlns="http://www.w3.org/2000/svg" className="h-10 w-10" fill="none"
               viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                  d="M13 16h-1v-4h-1m1-4h.01M12 2a10 10 0 1010 10A10 10 0 0012 2z"/>
          </svg>
        </div>
        <h2 className="text-lg font-semibold text-gray-800">Delete Post?</h2>
        <p className="text-sm text-gray-500">
          This action cannot be undone. Are you sure you want to delete this post?
        </p>
      </div>

      <div className="flex gap-3 mt-6 flex justify-center">
        <button
          onClick={() => {
            setShowConfirm(false);
            setPostToDelete(null);
          }}
          className="px-4 py-2 rounded-full border border-gray-300 text-gray-600 hover:bg-gray-100 transition"
        >
          Cancel
        </button>
        <button
          onClick={async () => {
            try {
              await postApi.deletePost(postToDelete);
              setPosts((prev) => prev.filter((p) => p.id !== postToDelete));
            } catch (err) {
              console.error("Error deleting post", err);
            } finally {
              setShowConfirm(false);
              setPostToDelete(null);
            }
          }}
          className="px-4 py-2 rounded-full bg-red-600 text-white hover:bg-red-700 transition font-medium"
        >
          Yes, delete
        </button>
      </div>
    </div>
  </div>
)}

    </div>
  );
};

export default DashboardHome;