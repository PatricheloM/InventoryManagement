import MainPage from "./components/MainPage";
import LoginPage from "./components/LoginPage";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate
} from "react-router-dom";
import check from "./util/CheckToken";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/main" element={
          <CheckAuth>
            <MainPage />
          </CheckAuth>
        } />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={<Navigate to="/login" replace={true} />} />
      </Routes>
    </Router>
  );
}

function CheckAuth({ children }) {
  const token = localStorage.getItem("IMTOKEN");
  if (!token) {
    return (<Navigate to="/login" replace={true} />);
  }

  const user = check(token);

  if (!user) { // TODO: bug when token expires
    return (<Navigate to="/login" replace={true} />);
  }

  return children
}
export default App;
