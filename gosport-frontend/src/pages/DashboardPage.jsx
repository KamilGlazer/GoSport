import { Link } from "react-router-dom";
import NavBar from "../components/NavBar/NavBar"

const Dashboard = () => {
    return (
        <div className="pt-16"> {/* Dodaj padding-top równy wysokości NavBar */}
            <NavBar />
            <main className="max-w-7xl mx-auto px-4 py-6">
            </main>
        </div>
    );
};

export default Dashboard;