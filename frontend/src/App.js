import MainPage from "./components/MainPage";
import LoginPage from "./components/LoginPage";
import AccountPage from "./components/AccountPage";
import ImportPage from "./components/ImportPage";
import ExportPage from "./components/ExportPage";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate
} from "react-router-dom";
import { Button, Center, Flex, Heading, Stack, useToast } from "@chakra-ui/react";
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
        <Route path="/importing" element={
          <CheckAuth>
            <ImportPage />
          </CheckAuth>
        } />
        <Route path="/exporting" element={
          <CheckAuth>
            <ExportPage />
          </CheckAuth>
        } />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={<Navigate to="/login" replace={true} />} />
        <Route path="*" element={<PageNotFound />} />
      </Routes>
    </Router>
  );
}

function PageNotFound() {
  const navigate = useNavigate();
  return (
    <>
      <Flex
        flexDirection="column"
        width="100wh"
        height="100vh"
        justifyContent="center"
        alignItems="center"
      >
        <Stack
          flexDir="column"
          mb="2"
          justifyContent="center"
          alignItems="center"
        >
            <Heading>
              404
            </Heading>
            <Heading>
              Page not found
            </Heading>
            <Button colorScheme="teal" onClick={() => navigate('/main')}>Back to main page</Button>
        </Stack>
      </Flex>
    </>
  );
}

function CheckAuth({ children }) {
  const navigate = useNavigate();
  const toast = useToast();
  const token = localStorage.getItem("IMTOKEN");
  useEffect(() => {
    async function check() {
      await axios.get("http://localhost:8080/api/account/token/" + token)
        .catch(function (error) {
          localStorage.removeItem('IMTOKEN');
          toast({
            title: 'Your session expired!',
            status: 'error',
            duration: 3000,
            isClosable: true,
          })
          navigate('/login')
        });
    }
    check();
  });

  return children
}
export default App;
