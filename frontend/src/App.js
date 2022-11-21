import MainPage from "./components/MainPage";
import LoginPage from "./components/LoginPage";
import AccountPage from "./components/AccountPage";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate
} from "react-router-dom";
import { useEffect } from 'react'
import axios from "axios";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/main" element={
          <CheckAuth>
            <MainPage />
          </CheckAuth>
        } />
        <Route path="/account" element={
          <CheckAuth>
            <AccountPage />
          </CheckAuth>
        } />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={<Navigate to="/login" replace={true} />} />
      </Routes>
    </Router>
  );
}

function CheckAuth({ children }) {
  const navigate = useNavigate();
  const token = localStorage.getItem("IMTOKEN");
  useEffect(() => {
    async function check() {
      await axios.get("http://localhost:8080/api/account/token/" + token)
      .catch(function (error) {
          localStorage.removeItem('IMTOKEN');
          navigate('/login')
      });
    }
    check();
  });

  return children
}
export default App;
