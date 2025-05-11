import { useSelector } from "react-redux";
import {
  FaUserCircle,
  FaPhone,
  FaMapMarkerAlt,
  FaEdit,
  FaExclamationCircle,
} from "react-icons/fa";
import { useEffect, useState } from "react";
import { profileApi } from "../services/profileApi";
import { postApi } from "../services/postApi";
import { commentApi } from "../services/commentApi";

const ProfilePage = () => {
  const user = useSelector((state) => state.auth.user);
  const userId = user?.profile?.userId;
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    firstName: user?.profile?.firstName || "",
    lastName: user?.profile?.lastName || "",
    headline: user?.profile?.headline || "",
    mobile: user?.profile?.mobile || "",
    city: user?.profile?.city || "",
    postalCode: user?.profile?.postalCode || "",
  });
  const [errors, setErrors] = useState({});
  const [myPosts, setMyPosts] = useState([]);
  const [expandedPostId, setExpandedPostId] = useState(null);
  const [comments, setComments] = useState({});
  const [newComment, setNewComment] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);
  const [postToDelete, setPostToDelete] = useState(null);

  useEffect(() => {
    const fetchMyPosts = async () => {
      try {
        const data = await postApi.getMyPosts();
        setMyPosts(data);
      } catch (err) {
        console.error("Failed to fetch user's posts", err);
      }
    };

    fetchMyPosts();
  }, []);

  useEffect(() => {
    if (showModal && user?.profile) {
      setFormData({
        firstName: user.profile.firstName || "",
        lastName: user.profile.lastName || "",
        headline: user.profile.headline || "",
        mobile: user.profile.mobile || "",
        city: user.profile.city || "",
        postalCode: user.profile.postalCode || "",
      });
    }
  }, [showModal, user]);

  const validateForm = () => {
    const newErrors = {};
    if (!formData.firstName.trim()) newErrors.firstName = "First name is required.";
    if (!formData.lastName.trim()) newErrors.lastName = "Last name is required.";

    const hasCity = formData.city.trim() !== "";
    const hasPostal = formData.postalCode.trim() !== "";
    if ((hasCity && !hasPostal) || (!hasCity && hasPostal)) {
      newErrors.city = "City and Postal Code must both be filled or both empty.";
      newErrors.postalCode = "City and Postal Code must both be filled or both empty.";
    }

    if (formData.mobile.trim()) {
      const phoneRegex = /^\+\d{1,4} \d{9}$/;
      if (!phoneRegex.test(formData.mobile.trim())) newErrors.mobile = "Example: +48 123456789";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const hasContactInfo = user?.profile?.mobile || user?.profile?.city || user?.profile?.postalCode;

  return (
    <div>
      <div className="max-w-4xl mx-auto mt-30 bg-white rounded-lg shadow-lg overflow-hidden relative">
        <div className="h-48 bg-blue-100 relative">
          <div className="absolute -bottom-16 left-8">
            {user?.avatar ? (
              <img src={user.avatar} alt="Avatar" className="w-32 h-32 rounded-full border-4 border-white object-cover shadow-lg" />
            ) : (
              <FaUserCircle className="w-32 h-32 text-gray-400 bg-white rounded-full border-4 border-white" />
            )}
          </div>
        </div>

        <div className="pt-20 px-8 pb-8 relative">
          <button
            onClick={() => setShowModal(true)}
            className="absolute top-5 right-5 p-2 bg-blue-500 hover:bg-blue-600 text-white rounded-full shadow-lg transition"
            title="Edit profile"
          >
            <FaEdit className="w-5 h-5" />
          </button>
          <div className="mb-6">
            <h1 className="text-3xl font-semibold text-gray-900">
              {user?.profile?.firstName} {user?.profile?.lastName}
            </h1>
            <p className="text-xl text-gray-600 mt-2">
              {user?.profile?.headline || "No additional information provided"}
            </p>
          </div>

          <div className="border-t  border-gray-200 pt-6">
            {hasContactInfo ? (
              <div className="flex flex-wrap gap-8">
                {user?.profile?.mobile && (
                  <div className="flex items-center text-gray-600">
                    <FaPhone className="mr-2 text-blue-500" />
                    <span>{user.profile.mobile}</span>
                  </div>
                )}
                {(user?.profile?.city || user?.profile?.postalCode) && (
                  <div className="flex items-center text-gray-600">
                    <FaMapMarkerAlt className="mr-2 text-blue-500" />
                    <span>
                      {user.profile.city}{user.profile.city && user.profile.postalCode && ", "}{user.profile.postalCode}
                    </span>
                  </div>
                )}
              </div>
            ) : (
              <p className="text-gray-500 italic">No contact information provided</p>
            )}
          </div>
        </div>
      </div>

      {/* TWOJE POSTY */}
      <div className="max-w-4xl mx-auto mt-10 space-y-6">
        {myPosts.map((post) => (
          <div key={post.id} className="bg-white rounded-2xl border border-gray-200 shadow-sm p-5 space-y-4">
            <div className="flex items-center gap-3">
              <img src={post.authorProfileImage} alt="Author" className="w-10 h-10 rounded-full object-cover" />
              <div>
                <div className="font-medium text-gray-900 text-sm">
                  {post.authorFirstName} {post.authorLastName}
                </div>
                <div className="text-xs text-gray-500">
                  {new Date(post.createdAt).toLocaleString()}
                </div>
              </div>
            </div>

            <div className="text-gray-800 text-sm whitespace-pre-wrap leading-relaxed break-words">
              {post.content}
            </div>

            <div className="flex justify-between items-center border-t border-gray-100 pt-3 text-sm">
              <span className="text-gray-500">👍 {post.likeCount} likes</span>
              <button
                onClick={async () => {
                  try {
                    await postApi.deletePost(post.id);
                    setMyPosts((prev) => prev.filter((p) => p.id !== post.id));
                  } catch (err) {
                    console.error("Error deleting post", err);
                  }
                }}
                className="px-3 py-1 rounded-full border border-red-500 text-red-500 hover:bg-red-50 transition font-medium"
              >
                Delete
              </button>
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
                      <img src={comment.authorProfileImage} alt="Author" className="w-8 h-8 rounded-full object-cover" />
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
      </div>

      {showModal && (
  <div className="fixed inset-0 flex justify-center items-center bg-black/30 backdrop-blur-sm z-50">
    <div className="bg-white rounded-xl shadow-2xl w-full max-w-xl p-8 relative">
      <button
        onClick={() => setShowModal(false)}
        className="absolute top-4 right-4 text-gray-500 hover:text-gray-700 text-3xl font-bold"
        title="Close"
      >
        &times;
      </button>

      <h2 className="text-2xl font-semibold text-gray-800 mb-6 border-b border-gray-300 pb-4">
        Edit "About me"
      </h2>

      <form
        onSubmit={async (e) => {
          e.preventDefault();
          if (!validateForm()) return;
          try {
            await profileApi.editUserProfile(formData);
            setShowModal(false);
            window.location.reload();
          } catch (error) {
            console.error("Update failed:", error);
          }
        }}
        className="space-y-5"
      >
        {[
          { name: "firstName", label: "First Name" },
          { name: "lastName", label: "Last Name" },
          { name: "headline", label: "Headline" },
          { name: "mobile", label: "Mobile" },
          { name: "city", label: "City" },
          { name: "postalCode", label: "Postal Code" },
        ].map(({ name, label }) => (
          <div key={name}>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              {label}
            </label>
            <input
              type="text"
              value={formData[name]}
              onChange={(e) => setFormData({ ...formData, [name]: e.target.value })}
              className="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
            />
            {errors[name] && (
              <p className="text-sm text-red-500 mt-1 flex items-center gap-1">
                <FaExclamationCircle className="text-red-500" />
                {errors[name]}
              </p>
            )}
          </div>
        ))}

        <div className="flex justify-end">
          <button
            type="submit"
            className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition"
          >
            Save
          </button>
        </div>
      </form>
    </div>
  </div>
)}

    </div>
  );
};

export default ProfilePage;