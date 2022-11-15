import { Grid, GridItem, Box, Center, Flex, Text } from '@chakra-ui/react'
import axios from 'axios'
import { useState, useEffect } from 'react'
import { useNavigate } from "react-router-dom";

export default function Header({ children }) {

    const navigate = useNavigate();
    const token = localStorage.getItem("IMTOKEN");

    const [user, setUser] = useState(null);

    async function logout() {
        await axios.get("http://localhost:8080/api/account/logout?token=" + token)
            .then(function (response) {
                localStorage.removeItem("IMTOKEN");
                navigate("/login");
            })
    }

    useEffect(() => {
        axios.get("http://localhost:8080/api/account/token/" + token)
            .then(function (response) {
                setUser(response.data);
            })
    }, [])

    return (
        <>
            <Center>
                <Box bg='white' p={4} marginTop="1vh" backgroundColor="whiteAlpha.900" boxShadow="md" >
                    <Grid templateColumns='repeat(4, 1fr)' gap={6} >
                        <GridItem><Center><Box as='button' minWidth="20vh" borderRadius='md' bg='teal.500' color='white' px={4} h={8}>Button</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' minWidth="20vh" borderRadius='md' bg='teal.500' color='white' px={4} h={8}>Button</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' minWidth="20vh" borderRadius='md' bg='teal.500' color='white' px={4} h={8}>Button</Box></Center></GridItem>
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