import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import AuthRoute from './components/AuthRoute';
import { AuthProvider } from './contexts/AuthContext';
import BasePage from './pages/BasePage'
import Dashboard from './pages/DashboardPage';


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
              <Route path="/dashboard" element={<Dashboard />} />
          </Route>

        </Routes>
      </Router>
    </AuthProvider>
  )
};

export default App
