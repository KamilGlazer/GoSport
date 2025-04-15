import { useSelector } from "react-redux";
import {
  FaUserCircle,
  FaPhone,
  FaMapMarkerAlt,
  FaEdit,
  FaExclamationCircle,
} from "react-icons/fa";
import NavBar from "../components/NavBar/NavBar";
import { useEffect, useState } from "react";
import { profileApi } from "../services/profileApi";

const ProfilePage = () => {
  const user = useSelector((state) => state.auth.user);
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

    if (!formData.firstName.trim()) {
      newErrors.firstName = "First name is required.";
    }
    if (!formData.lastName.trim()) {
      newErrors.lastName = "Last name is required.";
    }

    const hasCity = formData.city.trim() !== "";
    const hasPostal = formData.postalCode.trim() !== "";
    if ((hasCity && !hasPostal) || (!hasCity && hasPostal)) {
      newErrors.city =
        "City and Postal Code must both be filled or both empty.";
      newErrors.postalCode =
        "City and Postal Code must both be filled or both empty.";
    }

    if (formData.mobile.trim()) {
      const phoneRegex = /^\+\d{1,4} \d{9}$/;
      if (!phoneRegex.test(formData.mobile.trim())) {
        newErrors.mobile = "Example: +48 123456789";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const hasContactInfo =
    user?.profile?.mobile || user?.profile?.city || user?.profile?.postalCode;

  return (
    <div>
      <div className="max-w-4xl mx-auto mt-30 bg-white rounded-lg shadow-lg overflow-hidden relative">
        <div className="h-48 bg-blue-100 relative">
          <div className="absolute -bottom-16 left-8">
            {user?.avatar ? (
              <img
                src={user.avatar}
                alt="Avatar"
                className="w-32 h-32 rounded-full border-4 border-white object-cover shadow-lg"
              />
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
                      {user.profile.city}
                      {user.profile.city && user.profile.postalCode && ", "}
                      {user.profile.postalCode}
                    </span>
                  </div>
                )}
              </div>
            ) : (
              <p className="text-gray-500 italic">
                No contact information provided
              </p>
            )}
          </div>
        </div>
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
                    onChange={(e) =>
                      setFormData({ ...formData, [name]: e.target.value })
                    }
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
