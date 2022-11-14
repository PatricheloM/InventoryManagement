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
  Avatar,
  FormControl,
  InputRightElement,
  useToast
} from "@chakra-ui/react";
import { useState } from "react";


export default function LoginPage() {
    const [showPassword, setShowPassword] = useState(false);
    const handleShowClick = () => setShowPassword(!showPassword);
    const [user, setUser] = useState("");
    const [pass, setPass] = useState("");
    const sha1 = require('sha-1');
    const toast = useToast();

    async function getStoreData(e) {
        e.preventDefault();
        axios.post("http://localhost:8080/api/account/login", {
            "username": user,
            "password": sha1(pass)
        })
        .then(function (response) {
            localStorage.setItem("IMTOKEN", response.data.token);
            localStorage.setItem("IMUSER", response.data.username);
            window.location.reload();
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
            <Avatar bg="teal.500" />
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
                            placeholder="Username" 
                            onChange={(e) => setUser(e.target.value)} />
                        </InputGroup>
                    </FormControl>
                    <FormControl>
                        <InputGroup>
                        <InputLeftElement
                            pointerEvents="none"
                            color="gray.300"
                        />
                        <Input
                            id="pass"
                            type={showPassword ? "text" : "password"}
                            placeholder="Password"
                            onChange={(e) => setPass(e.target.value)}
                        />
                        <InputRightElement width="4.5rem">
                            <Button h="1.75rem" size="sm" onClick={handleShowClick}>
                            {showPassword ? "Hide" : "Show"}
                            </Button>
                        </InputRightElement>
                        </InputGroup>
                    </FormControl>
                    <Button
                        borderRadius={0}
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
