import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import PrivateRoute from './components/PrivateRoute'
import PublicRoute from './components/PublicRoute'
import BasePage from './pages/BasePage'


function App() {
  return (
    <Router>
      <Routes>
        <Route element={<PublicRoute />}>
          <Route path='/login' element={<LoginPage />}/>
          <Route path="/register" element={<RegisterPage />} />
        </Route>
      
        <Route path="/" element={<BasePage />} />
        
        <Route element={<PrivateRoute />}> 
          <Route path="/dashboard" element={<h1>Dashboard (do zmiany)</h1>} />
        </Route>
      </Routes>
    </Router>
  )
};

export default App
