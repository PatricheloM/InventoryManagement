import axios from "axios";
import {
    Flex,
    Heading,
    Input,
    Button,
    InputGroup,
    Stack,
    InputLeftElement,
    Box,
    Image,
    FormControl,
    InputRightElement,
    useToast
} from "@chakra-ui/react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";


export default function LoginPage() {
    const [showPassword, setShowPassword] = useState(false);
    const handleShowClick = () => setShowPassword(!showPassword);
    const [user, setUser] = useState("");
    const [pass, setPass] = useState("");
    const sha1 = require('sha-1');
    const toast = useToast();
    const navigate = useNavigate();

    const token = localStorage.getItem("IMTOKEN");
    useEffect(() => {
      async function check() {
        await axios.get("http://localhost:8080/api/account/token/" + token)
        .then(function (response) {
            navigate('/main');
        })
      }
      if (token) {
        check();
      }
    }, []);

    async function getStoreData(e) {
        e.preventDefault();
        axios.post("http://localhost:8080/api/account/login", {
            "username": user,
            "password": sha1(pass)
        })
            .then(function (response) {
                localStorage.setItem("IMTOKEN", response.data.token);
                navigate("/main");
            })
            .catch(function (error) {
                if (error.response.data.status === 403) {
                    toast({
                        title: 'Wrong credentials!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                }
                if (error.response.data.status === 500) {
                    toast({
                        title: 'Internal server error!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                }
            });
    }

    return (
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
                <Image boxSize={32} src={require('../img/logo.png')} />
                <Heading color="teal.400">Inventory Management</Heading>
                <Box minW={{ base: "90%", md: "468px" }}>
                    <form>
                        <Stack
                            spacing={4}
                            p="1rem"
                            backgroundColor="whiteAlpha.900"
                            boxShadow="md"
                        >
                            <FormControl>
                                <InputGroup>
                                    <InputLeftElement
                                        pointerEvents="none"
                                    />
                                    <Input
                                        type="username"
                                        borderRadius='xl'
                                        placeholder="Username"
                                        onChange={(e) => setUser(e.target.value)} />
                                </InputGroup>
                            </FormControl>
                            <FormControl>
                                <InputGroup>
                                    <InputLeftElement
                                        pointerEvents="none"
                                    />
                                    <Input
                                        id="pass"
                                        borderRadius='xl'
                                        type={showPassword ? "text" : "password"}
                                        placeholder="Password"
                                        onChange={(e) => setPass(e.target.value)}
                                    />
                                    <InputRightElement width="4.5rem">
                                        <Button h="1.75rem" size="sm" onClick={handleShowClick} borderRadius='xl'>
                                            {showPassword ? "Hide" : "Show"}
                                        </Button>
                                    </InputRightElement>
                                </InputGroup>
                            </FormControl>
                            <Button
                                borderRadius='xl'
                                type="submit"
                                variant="solid"
                                colorScheme="teal"
                                width="full"
                                onClick={(e) => getStoreData(e)}
                            >
                                Login
                            </Button>
                        </Stack>
                    </form>
                </Box>
            </Stack>
        </Flex>
    );
}
