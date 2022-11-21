import { useToast, InputGroup, ModalFooter, Avatar, Box, Button, Center, Container, FormControl, FormLabel, Grid, GridItem, Heading, Input, Modal, ModalBody, ModalCloseButton, ModalContent, ModalHeader, ModalOverlay, useDisclosure, InputRightElement, Select } from '@chakra-ui/react';
import axios from 'axios';
import { useState, useEffect } from 'react';
import Header from '../layout/Header';



export default function AccountPage() {

    const { isOpen: isOpenReg, onOpen: onOpenReg, onClose: onCloseReg } = useDisclosure()
    const { isOpen: isOpenPw, onOpen: onOpenPw, onClose: onClosePw } = useDisclosure()
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

    return (
        <>

            <Modal
                isOpen={isOpenPw}
                onClose={onClosePw}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Create your account</ModalHeader>
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
                        <Button onClick={(e) => getPaswordChangeData(e)} bg='teal.500' color='white' mr={3}>
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
                        <Button onClick={(e) => getRegistrationData(e)} bg='teal.500' color='white' mr={3}>
                            Register
                        </Button>
                        <Button onClick={onCloseReg}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Header>
                <Container p={4} backgroundColor="whiteAlpha.900" boxShadow="md" centerContent>
                    <Avatar bg="teal.500" />
                    <Heading color="teal.400">Account</Heading>
                    <Box marginTop="2vh" padding='4' bg="gray.100" color='black' maxW='md'>
                        <Grid templateColumns='repeat(1, 3fr)' gap={6} >
                            <GridItem><Center><Box as='button' onClick={onOpenReg} minWidth="40vh" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Create new</Box></Center></GridItem>
                            <GridItem><Center><Box as='button' onClick={onOpenPw} minWidth="40vh" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Change password</Box></Center></GridItem>
                            <GridItem><Center><Box as='button' minWidth="40vh" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Delete user</Box></Center></GridItem>
                        </Grid>
                    </Box>
                </Container>
            </Header>
        </>
    );
}