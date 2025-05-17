import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import AuthRoute from './components/AuthRoute';
import { AuthProvider } from './contexts/AuthContext';
import BasePage from './pages/BasePage'
import Dashboard from './pages/DashboardPage';
import ProfilePage from './pages/ProfilePage';
import NotificationsPage from './pages/NotificationsPage';
import PublicProfilePage from './pages/PublicProfilePage';
import MessagePage from './pages/MessagePage';
import DashboardHome from './pages/DashBoardHome';
import TrainersPage from './pages/TrainersPage';


function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>

          <Route element={<AuthRoute isPrivate={false} />}>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
          </Route>
        
          <Route path="/" element={<BasePage />} />
          
          <Route element={<AuthRoute isPrivate={true} />}>
            <Route path="/dashboard" element={<Dashboard />}>
              <Route index element={<DashboardHome />} />
              <Route path='messages' element={<MessagePage />} />
              <Route path="profile" element={<ProfilePage />} />
              <Route path="trainers" element={<TrainersPage />} />
              <Route path="notifications" element={<NotificationsPage />} />
              <Route path="profile/:userId" element={<PublicProfilePage />} />
            </Route>
          </Route>

        </Routes>
      </Router>
    </AuthProvider>
  )
};

export default App
