import { Grid, GridItem, Box, Center, Flex, Text, Image, Button, Heading, useToast } from '@chakra-ui/react'
import axios from 'axios'
import { useState, useEffect } from 'react'
import { useNavigate } from "react-router-dom";

export default function Header({ children }) {
    const toast = useToast();
    const navigate = useNavigate();
    const token = localStorage.getItem("IMTOKEN");

    const [user, setUser] = useState({
        username: "Loading...",
        privilege: "Loading..."
    });

    async function redirect(page) {
        await axios.get("http://localhost:8080/api/account/token/" + token)
        .then(function (response) {
            navigate(page);
        })
        .catch(function (error) {
            localStorage.removeItem('IMTOKEN');
            toast({
              title: 'Your session expired!',
              status: 'error',
              duration: 3000,
              isClosable: true,
            })
            navigate('/login');
        })
    }

    async function logout() {
        await axios.get("http://localhost:8080/api/account/logout?token=" + token)
            .then(function (response) {
                localStorage.removeItem("IMTOKEN");
                navigate("/login");
            })
            .catch(function (error) {
                localStorage.removeItem("IMTOKEN");
                navigate("/login");
            })
    }

    useEffect(() => {
        axios.get("http://localhost:8080/api/account/token/" + token)
            .then(function (response) {
                setUser(response.data);
            })
            .catch(function (error) {
                setUser(
                    {
                        username: "Unknown user",
                        privilege: "(LOG IN)"
                    }
                );
            })
    }, [])

    return (
        <>
            <Center marginBottom="2vh">
                <Box marginRight="1vw">
                    <Flex
                        flexDirection="column"
                        justifyContent="center"
                        alignItems="center"
                        p="1vh"
                        marginTop="1vh"
                    >
                        <Button onClick={() => navigate('/main')} boxShadow="md" minH={24} minW={24} display="inline" textAlign="center">
                                <Image ml="2" boxSize={16} src={require('../img/logo.png')} />
                                <Heading size="l" color="teal.500">Main Page</Heading>
                        </Button>
                    </Flex>
                </Box>
                <Box p={4} marginTop="1vh" backgroundColor="whiteAlpha.900" boxShadow="md" >
                    <Grid templateColumns='repeat(4, 1fr)' gap={6} >
                        <GridItem><Center><Box as='button' onClick={() => redirect('/importing')} minWidth="20vh" borderRadius='md' bg='teal.500' color='white' px={4} h={8}>Import</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' onClick={() => redirect('/exporting')} minWidth="20vh" borderRadius='md' bg='teal.500' color='white' px={4} h={8}>Export</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' onClick={() => redirect('/account')} minWidth="20vh" borderRadius='md' bg='green.500' color='white' px={4} h={8}>Account</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' onClick={() => logout()} minWidth="20vh" borderRadius='md' bg='tomato' color='white' px={4} h={8}>Logout</Box></Center></GridItem>
                    </Grid>
                </Box>
                <Box marginLeft="2vw">
                    <Flex
                        flexDirection="column"
                        justifyContent="center"
                        alignItems="center"
                        bg="gray.100"
                        p="1vh"
                        marginTop="1vh"
                        minWidth="20vh"
                        boxShadow="md"
                    >
                        <Box >
                            Logged in as:
                        </Box>
                        <Box>
                            <Text as="u">{`${user?.username} (${user?.privilege})`}</Text>
                        </Box>
                    </Flex>
                </Box>
            </Center>
            <Flex
                flexDirection="column"
                justifyContent="center"
                alignItems="center"
                marginTop="1vh">
                <Box>
                    {children}
                </Box>
            </Flex>
        </>
    );
}