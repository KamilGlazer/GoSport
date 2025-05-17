import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaSearch } from "react-icons/fa";
import { profileApi } from "../services/profileApi";

const TrainersPage = () => {
  const [city, setCity] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const [trainers, setTrainers] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSearch = async () => {
    setIsLoading(true);
    try {
      const res = await profileApi.searchTrainers({ city, postalCode });
      setTrainers(res);
    } catch (err) {
      console.error("Error searching trainers", err);
      setTrainers([]);
    }
    setIsLoading(false);
  };

  return (
    <div className="min-h-screen pt-32 px-4 max-w-4xl mx-auto">
      <div className="flex flex-col sm:flex-row justify-center gap-4 mb-8">
        <input
          type="text"
          placeholder="City"
          value={city}
          onChange={(e) => setCity(e.target.value)}
          className="w-full sm:w-64 border border-gray-300 rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <input
          type="text"
          placeholder="Postal Code"
          value={postalCode}
          onChange={(e) => setPostalCode(e.target.value)}
          className="w-full sm:w-64 border border-gray-300 rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button
          onClick={handleSearch}
          className="flex items-center gap-2 px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl shadow transition"
        >
          <FaSearch /> Search
        </button>
      </div>

      {isLoading ? (
        <p className="text-center text-gray-500">Loading trainers...</p>
      ) : (
        <div className="grid gap-4 grid-cols-2 sm:grid-cols-3 md:grid-cols-4">
          {trainers.map((trainer) => (
            <div
              key={trainer.userId}
              onClick={() => navigate(`/dashboard/profile/${trainer.userId}`)}
              className="bg-white rounded-2xl border border-gray-200 shadow-sm p-3 flex flex-col items-center text-center hover:shadow-md transition cursor-pointer"
            >
              <img
                src={trainer.profileImage || "/default-avatar.png"}
                alt={`${trainer.firstName} ${trainer.lastName}`}
                onError={(e) => {
                  e.target.onerror = null;
                  e.target.src = "/default-avatar.png";
                }}
                className="w-16 h-16 rounded-full object-cover mb-2"
              />
              <p className="text-sm font-semibold text-gray-800">
                {trainer.firstName} {trainer.lastName}
              </p>
              <p className="text-xs text-gray-500">
                {trainer.city}
                {trainer.postalCode ? `, ${trainer.postalCode}` : ""}
              </p>
            </div>
          ))}
          {trainers.length === 0 && (
            <p className="text-center text-gray-400 col-span-full">No trainers found.</p>
          )}
        </div>
      )}
    </div>
  );
};


export default TrainersPage;
