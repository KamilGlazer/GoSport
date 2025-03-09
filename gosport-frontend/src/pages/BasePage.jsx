import { Link } from "react-router-dom";
import Logo from "../components/Logo";
import sportsImage from "../assets/sports.png";
import sports2Image from "../assets/sports2.png";
import { useState, useRef, useEffect } from "react";
import { motion } from "framer-motion";
import { FaUsers, FaDumbbell, FaShieldAlt } from "react-icons/fa";

const sportsCategories = [
  "Football",
  "Basketball",
  "Tennis",
  "Swimming",
  "Martial Arts",
  "Cycling",
  "Athletics",
  "Volleyball",
  "Baseball",
  "Golf",
  "Boxing",
  "Gymnastics",
  "Esports",
  "Rugby",
  "MMA",
  "Running",
  "Bodybuilding",
  "Wrestling",
  "Skiing",
  "Snowboarding",
  "Climbing",
  "Table Tennis",
  "Handball",
  "Rowing",
  "CrossFit",
  "Triathlon",
  "Diving",
  "Skateboarding",
];

const BasePage = () => {
  const [showAll, setShowAll] = useState(false);
  const containerRef = useRef(null);
  const [dynamicHeight, setDynamicHeight] = useState(250);

  useEffect(() => {
    if (containerRef.current) {
      containerRef.current.style.maxHeight = "none";
      const fullHeight = containerRef.current.scrollHeight;
      const measuredHeight = showAll ? fullHeight : 120;
      setDynamicHeight(measuredHeight);
      containerRef.current.style.maxHeight = `${dynamicHeight}px`;
    }
  }, [showAll, sportsCategories]);

  return (
    <div className="relative w-full min-h-screen bg-white flex flex-col items-center">
      <header className="w-4/5 max-w-[1400px] bg-white flex">
        <div className="w-4/5 max-w-[1400px] mx-auto flex justify-between items-center px-8 py-4">
          <Logo />

          <div className="flex space-x-8 items-center">
            <a href="#about" className="nav-link">
              About us
            </a>
            <a href="#trainers" className="nav-link">
              For trainers
            </a>
            <a href="#athletes" className="nav-link">
              For athletes
            </a>
            <Link
              to="/login"
              className="text-base font-medium text-white bg-blue-500 px-10 py-2 rounded-3xl hover:bg-blue-600"
            >
              Log in
            </Link>
          </div>
        </div>
      </header>

      <section className="w-4/5 max-w-[1400px] mx-auto mt-24 flex flex-col md:flex-row items-center justify-between">
        <div className="md:w-1/2 text-center">
          <h1 className="text-2xl md:text-5xl font-semibold text-gray-900 ">
            Dream big. Train hard.
          </h1>

          <div className="mt-6 space-y-4 flex flex-col items-center w-full">
            <button className="w-full md:w-[80%] flex items-center justify-center bg-white border border-gray-400 px-6 py-3 rounded-full shadow-md hover:bg-gray-200 transition-all hover:scale-105 duration-400">
              <img
                src="https://th.bing.com/th?id=ODLS.d5309c11-4633-48cb-861d-ac819d6066d0&w=32&h=32&qlt=90&pcl=fffffa&o=6&pid=1.2"
                alt="Google Logo"
                className="h-5 w-5 mr-3"
              />
              Continue with Google
            </button>
            <button className="md:w-[80%] flex items-center justify-center bg-white border border-gray-400 px-6 py-3 rounded-full shadow-md hover:bg-gray-100 transition-all hover:scale-105 duration-400">
              <img
                src="https://upload.wikimedia.org/wikipedia/commons/4/44/Microsoft_logo.svg"
                alt="Microsoft Logo"
                className="h-5 w-5 mr-3"
              />
              Continue with Microsoft
            </button>
            <button className="w-full md:w-[80%] bg-blue-500 text-white px-6 py-3 rounded-full shadow-md hover:bg-blue-600 transition-all hover:scale-105 duration-400">
              Sign in with email
            </button>
          </div>

          <p className="text-xs text-gray-600 mt-4 md:w-[40%] text-center mx-auto">
            By continuing, you agree to our{" "}
            <a href="#" className="text-blue-600 hover:underline">
              Terms of Use
            </a>
            ,{" "}
            <a href="#" className="text-blue-600 hover:underline">
              Privacy Policy
            </a>{" "}
            i{" "}
            <a href="#" className="text-blue-600 hover:underline">
              Cookie Policy
            </a>
            .
          </p>
        </div>

        <div className="md:w-1/2 flex justify-center mt-10 md:mt-0">
          <img
            src={sportsImage}
            alt="Sport community"
            className="max-w-md w-full scale-170"
          />
        </div>
      </section>

      <section className="w-full mt-10 flex flex-col md:flex-row items-center justify-between bg-gray-100 px-4 md:px-10 py-20 min-h-[30vh]">
        <div className="md:w-1/2 text-center mb-8 md:mb-0">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 leading-tight">
            Find the right sport for you
          </h2>
          <p className="text-gray-600 mt-4 md:text-lg">
            Connect with athletes, find trainers, and explore sports
            communities.
          </p>
        </div>

        <div className="md:w-1/2 w-full max-w-2xl mr-30">
          <motion.div
            ref={containerRef}
            initial={{ maxHeight: 250 }}
            animate={{ maxHeight: dynamicHeight }}
            transition={{ duration: 0.5, ease: "easeInOut" }}
            className="overflow-hidden flex flex-wrap gap-3 justify-center w-full"
          >
            {sportsCategories.map((sport, index) => (
              <motion.button
                key={index}
                whileHover={{ scale: 1.05, backgroundColor: "#3b82f6" }}
                transition={{ duration: 0.2, ease: "easeInOut" }}
                className="px-4 mt-1 mb-1 py-2 text-sm md:text-base border border-gray-300 rounded-full bg-white hover:bg-blue-500 hover:text-white transition-colors shadow-sm relative overflow-hidden"
              >
                <motion.span
                  className="absolute inset-0 bg-blue-500 opacity-0"
                  whileHover={{ opacity: 1 }}
                  transition={{ duration: 0.3 }}
                />
                <span className="relative z-10">{sport}</span>
              </motion.button>
            ))}
          </motion.div>

          <div className="w-full text-center mt-6">
            <button
              onClick={() => setShowAll(!showAll)}
              className="px-6 py-2 text-gray-700 bg-white border border-gray-300 rounded-full hover:bg-gray-50 transition-colors shadow-sm"
            >
              {showAll ? "Show less ↑" : "Show more ↓"}
            </button>
          </div>
        </div>
      </section>

      <section
        id="about"
        className="w-full flex flex-col md:flex-row items-center justify-between bg-white px-10 py-20"
      >
        <div className="md:w-2/4 ml-20">
        <h2 className="text-4xl font-bold text-center text-gray-900 leading-tight relative after:content-[''] after:block after:w-24 after:h-1 after:bg-blue-500 after:mx-auto">
            About us
        </h2>

          <p className="text-gray-600 text-lg mt-4">
            GoSport is a community where athletes, trainers, and sports
            enthusiasts connect, grow, and inspire each other. Whether you are a
            professional or just starting, we provide the platform to showcase
            your skills, find trainers, and join sports events.
          </p>

          <ul className="mt-6 space-y-6">
            <li className="flex items-center space-x-4">
              <div className="w-12 h-12 flex items-center justify-center rounded-full border-2 border-blue-500 text-blue-500 text-xl hover:scale-120 duration-600">
                <FaUsers />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-gray-900">
                  Engaging Community
                </h3>
                <p className="text-gray-600 text-sm">
                  Connect with trainers, athletes, and sports enthusiasts from
                  all over the world.
                </p>
              </div>
            </li>

            <li className="flex items-center space-x-4">
              <div className="w-12 h-12 flex items-center justify-center rounded-full border-2 border-blue-500 text-blue-500 text-xl hover:scale-120 duration-600">
                <FaDumbbell />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-gray-900">
                  Expert Coaching
                </h3>
                <p className="text-gray-600 text-sm">
                  Get insights from experienced fitness professionals and
                  improve your skills.
                </p>
              </div>
            </li>

            <li className="flex items-center space-x-4">
              <div className="w-12 h-12 flex items-center justify-center rounded-full border-2 border-blue-500 text-blue-500 text-xl hover:scale-120 duration-600">
                <FaShieldAlt />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-gray-900">
                  Safe & Supportive
                </h3>
                <p className="text-gray-600 text-sm">
                  Join a positive space where everyone motivates each other to
                  achieve their goals.
                </p>
              </div>
            </li>
          </ul>
        </div>

        <div className="md:w-1/2 flex justify-center mt-10 md:mt-0">
          <img
            src={sports2Image}
            alt="GoSport Community"
            className="max-w-md"
          />
        </div>
      </section>
    </div>
  );
};

export default BasePage;
