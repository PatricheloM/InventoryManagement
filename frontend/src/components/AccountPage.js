import { useToast, InputGroup, ModalFooter, Avatar, Box, Button, Center, Container, FormControl, FormLabel, Grid, GridItem, Heading, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalHeader, ModalOverlay, useDisclosure, InputRightElement, Select } from '@chakra-ui/react';
import axios from 'axios';
import { useState, useEffect } from 'react';
import Header from '../layout/Header';



export default function AccountPage() {
    const { isOpen: isOpenU, onOpen: onOpenU, onClose: onCloseU } = useDisclosure()
    const { isOpen: isOpenReg, onOpen: onOpenReg, onClose: onCloseReg } = useDisclosure()
    const { isOpen: isOpenPw, onOpen: onOpenPw, onClose: onClosePw } = useDisclosure()
    const { isOpen: isOpenD, onOpen: onOpenD, onClose: onCloseD } = useDisclosure()
    const [showPassword, setShowPassword] = useState(false);
    const handleShowClick = () => setShowPassword(!showPassword);
    const sha1 = require('sha-1');
    const token = localStorage.getItem('IMTOKEN');
    const toast = useToast();
    const [ownUser, setOwnUser] = useState();

    const [user, setUser] = useState("");
    const [pass, setPass] = useState("");
    const [privilege, setPrivilege] = useState("");
    const [company, setCompany] = useState("");
    const [email, setEmail] = useState("");

    useEffect(() => {
        axios.get("http://localhost:8080/api/account/token/" + token)
            .then(function (response) {
                setOwnUser(response.data.username);
            })
    }, [])

    function setOwnAsValue(e) {
        setUser(ownUser);
        document.getElementById("pwUser").value = ownUser;
    }

    async function getRegistrationData(e) {
        e.preventDefault();
        axios.post("http://localhost:8080/api/account/register?token=" + token, {
            "username": user,
            "password": sha1(pass),
            "privilege": privilege,
            "companyName": company,
            "companyEmail": email
        })
            .then(function (response) {
                toast({
                    title: 'Registration successful!',
                    status: 'success',
                    duration: 3000,
                    isClosable: true,
                })
                setUser("");
                setPass("");
                setCompany("");
                setEmail("");
                onCloseReg();
            })
            .catch(function (error) {
                if (error.response.data.status === 401) {
                    toast({
                        title: 'You can\'t create accounts!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                }
                if (error.response.data.status === 409) {
                    toast({
                        title: 'Username already exists!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                }
                if (error.response.data.status === 400) {
                    toast({
                        title: 'Can\'t process request!',
                        description: 'Invalid user details',
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

    async function getPaswordChangeData(e) {
        e.preventDefault();
        axios.patch("http://localhost:8080/api/account/" + user + "?newPassword=" + sha1(pass) + '&token=' + token)
            .then(function (response) {
                toast({
                    title: 'Passsword change successful!',
                    status: 'success',
                    duration: 3000,
                    isClosable: true,
                })
                setUser("");
                setPass("");
                onClosePw();
            })
            .catch(function (error) {
                if (error.response.data.status === 401) {
                    toast({
                        title: 'You can only change your own password!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                }
                if (error.response.data.status === 400) {
                    toast({
                        title: 'Can\'t process request!',
                        description: 'Invalid user details',
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

    async function getSearchUserData(e) {
        e.preventDefault();
        axios.get("http://localhost:8080/api/account/" + user + '?token=' + token)
        .then(function (response) {
            document.getElementById('searchedU').innerText = "Username: " + response.data.username;
            document.getElementById('searchedPrivilege').innerText = "Privilege: " + response.data.privilege;
            document.getElementById('searchedComp').innerText = "Company: " + response.data.companyName;
            document.getElementById('searchedEmail').innerText = "Company Email: " + response.data.companyEmail;
            document.getElementById("searchUser").value = "";
            setUser("");
        })
        .catch(function (error) {
            if (error.response.data.status === 401) {
                toast({
                    title: 'You can\'t search for users!',
                    status: 'error',
                    duration: 3000,
                    isClosable: true,
                })
            }
            if (error.response.data.status === 404) {
                toast({
                    title: 'User not found!',
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

    async function getDeleteUserData(e) {
        e.preventDefault();
        axios.delete("http://localhost:8080/api/account/" + user + '?token=' + token)
        .then(function (response) {
            toast({
                title: 'User deleted!',
                status: 'success',
                duration: 3000,
                isClosable: true,
            })
            setUser("");
            onCloseD();
        })
        .catch(function (error) {
            if (error.response.data.status === 401) {
                toast({
                    title: 'You can\'t delete users!',
                    status: 'error',
                    duration: 3000,
                    isClosable: true,
                })
            }
            if (error.response.data.status === 404) {
                toast({
                    title: 'User not found!',
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
        <>

            <Modal
                isOpen={isOpenU}
                onClose={onCloseU}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Search for user</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl mb="4">
                            <FormLabel>Username</FormLabel>
                            <Input placeholder='Username' id="searchUser" onChange={(e) => setUser(e.target.value)} />
                        </FormControl>
                        <Box id="searchedU"></Box>
                        <Box id="searchedPrivilege"></Box>
                        <Box id="searchedComp"></Box>
                        <Box id="searchedEmail"></Box>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={(e) => getSearchUserData(e)}
                                variant="solid"
                                colorScheme="teal" mr={3}>
                            Search
                        </Button>
                        <Button onClick={onCloseU}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Modal
                isOpen={isOpenPw}
                onClose={onClosePw}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Change password</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl>
                            <FormLabel>Username</FormLabel>
                            <InputGroup>
                                <Input placeholder='Username' id="pwUser" onChange={(e) => setUser(e.target.value)} />
                                <InputRightElement width="4.5rem">
                                    <Button h="1.75rem" size="sm" onClick={e => setOwnAsValue(e)} borderRadius='xl'>
                                        Own
                                    </Button>
                                </InputRightElement>
                            </InputGroup>
                        </FormControl>

                        <FormControl mt={4}>
                            <FormLabel>New password</FormLabel>
                            <InputGroup>
                                <Input id="pass"
                                    type={showPassword ? "text" : "password"}
                                    placeholder="New password"
                                    onChange={(e) => setPass(e.target.value)}
                                />
                                <InputRightElement width="4.5rem">
                                    <Button h="1.75rem" size="sm" onClick={handleShowClick} borderRadius='xl'>
                                        {showPassword ? "Hide" : "Show"}
                                    </Button>
                                </InputRightElement>
                            </InputGroup>
                        </FormControl>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={(e) => getPaswordChangeData(e)}
                            variant="solid"
                            colorScheme="teal" mr={3}>
                            Change
                        </Button>
                        <Button onClick={onClosePw}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Modal
                isOpen={isOpenReg}
                onClose={onCloseReg}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Create new account</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl>
                            <FormLabel>Username</FormLabel>
                            <Input placeholder='Username' onChange={(e) => setUser(e.target.value)} />
                        </FormControl>

                        <FormControl mt={4}>
                            <FormLabel>Password</FormLabel>
                            <InputGroup>
                                <Input id="pass"
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

                        <FormControl mt={4}>
                            <FormLabel>Privilege</FormLabel>
                            <Select placeholder='Select privilege' onChange={(e) => setPrivilege(e.target.value)}>
                                <option value='ADMIN'>Admin</option>
                                <option value='IMPORTER'>Importer</option>
                                <option value='EXPORTER'>Exporter</option>
                                <option value='IMPORTER_EXPORTER'>Importer - Exporter</option>
                                <option value='MAINTENANCE'>Maintenance</option>
                            </Select>
                        </FormControl>

                        <FormControl mt={4}>
                            <FormLabel>Company name</FormLabel>
                            <Input placeholder='Company name' onChange={(e) => setCompany(e.target.value)} />
                        </FormControl>

                        <FormControl mt={4}>
                            <FormLabel>Company email</FormLabel>
                            <Input placeholder='example@example.com' onChange={(e) => setEmail(e.target.value)} />
                        </FormControl>


                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={(e) => getRegistrationData(e)}
                            variant="solid"
                            colorScheme="teal" mr={3}>
                            Register
                        </Button>
                        <Button onClick={onCloseReg}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Modal
                isOpen={isOpenD}
                onClose={onCloseD}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Delete user</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl>
                            <FormLabel>Username</FormLabel>
                            <Input placeholder='Username' onChange={(e) => setUser(e.target.value)} />
                        </FormControl>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={(e) => getDeleteUserData(e)}
                                variant="solid"
                                colorScheme="red" mr={3}>
                            Delete user
                        </Button>
                        <Button onClick={onCloseD}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Header>
                <Container p={4} backgroundColor="whiteAlpha.900" boxShadow="md" centerContent>
                    <Avatar bg="teal.500" w={16} h={16} />
                    <Heading color="teal.400">Account</Heading>
                    <Box marginTop="2vh" padding='4' bg="gray.100" color='black' maxW='md'>
                        <Grid templateColumns='repeat(1, 4fr)' gap={6} >
                            <GridItem><Center><Box as='button' onClick={onOpenU} minWidth="40vh" borderRadius='md' bg='teal.500' color='white' px={4} h={8}>Search for user</Box></Center></GridItem>
                            <GridItem><Center><Box as='button' onClick={onOpenReg} minWidth="40vh" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Create new</Box></Center></GridItem>
                            <GridItem><Center><Box as='button' onClick={onOpenPw} minWidth="40vh" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Change password</Box></Center></GridItem>
                            <GridItem><Center><Box as='button' onClick={onOpenD} minWidth="40vh" borderRadius='md' bg='tomato' color='white' px={4} h={8}>Delete user</Box></Center></GridItem>
                        </Grid>
                    </Box>
                </Container>
            </Header>
        </>
    );
}