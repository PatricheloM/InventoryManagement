import { Box, Center, Container, FormControl, FormLabel, Heading, Input, NumberDecrementStepper, NumberIncrementStepper, NumberInput, NumberInputField, NumberInputStepper, Select, useToast } from "@chakra-ui/react";
import Header from "../layout/Header";
import { ArrowDownIcon } from '@chakra-ui/icons'
import { useEffect, useState } from "react";
import axios from "axios";

export default function ImportPage() {

    const toast = useToast();
    const token = localStorage.getItem('IMTOKEN');
    const [ownUser, setOwnUser] = useState();

    const [name, setName] = useState("");
    const [weight, setWeight] = useState("");
    const [warehouse, setWarehouse] = useState("");
    const [cell, setCell] = useState("");

    useEffect(() => {
        axios.get("http://localhost:8080/api/account/token/" + token)
            .then(function (response) {
                setOwnUser(response.data.companyName);
            })
    }, [])

    async function getStoreItem(e) {
        e.preventDefault();

        axios.post("http://localhost:8080/api/item/importing?token=" + token, {
            "name": name,
            "weight": Number(weight),
            "company": ownUser,
            "arrival": new Date(),
            "location": warehouse + '-' + cell
        })
            .then(function (response) {
                toast({
                    title: 'Exporting successful!',
                    status: 'success',
                    duration: 3000,
                    isClosable: true,
                })
            })
            .catch(function (error) {
                if (error.response.data.status === 401) {
                    toast({
                        title: 'You can\'t export!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                }
                if (error.response.data.status === 400) {
                    toast({
                        title: 'Can\'t process request!',
                        description: 'Invalid item details',
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
            <Header>
                <Container p={4} backgroundColor="whiteAlpha.900" boxShadow="md" minW="75vh"  centerContent>
                    <ArrowDownIcon color="teal.500" w={16} h={16} />
                    <Heading color="teal.400">Importing</Heading>
                    <Box marginTop="2vh" padding='4' bg="gray.100" color='black' minW="70vh">
                        <FormControl>
                            <FormLabel>Item name</FormLabel>
                            <Input placeholder='Item name' onChange={(e) => setName(e.target.value)} />
                        </FormControl>
                        <FormControl mt={4}>
                            <FormLabel>Item weight (kg)</FormLabel>
                            <NumberInput min={0} onChange={(e) => setWeight(e)}>
                                <NumberInputField />
                                <NumberInputStepper>
                                    <NumberIncrementStepper />
                                    <NumberDecrementStepper />
                                </NumberInputStepper>
                            </NumberInput>
                        </FormControl>
                        <FormControl mt={4}>
                            <FormLabel>Warehouse</FormLabel>
                            <Select placeholder='Select warehouse' onChange={(e) => setWarehouse(e.target.value)}>
                                <option value='A'>A</option>
                                <option value='B'>B</option>
                                <option value='C'>C</option>
                                <option value='D'>D</option>
                                <option value='E'>E</option>
                            </Select>
                        </FormControl>
                        <FormControl mt={4}>
                            <FormLabel>Cell in warehouse</FormLabel>
                            <NumberInput min={1000} max={9999} onChange={(e) => setCell(e)}>
                                <NumberInputField />
                                <NumberInputStepper>
                                    <NumberIncrementStepper />
                                    <NumberDecrementStepper />
                                </NumberInputStepper>
                            </NumberInput>
                        </FormControl>
                    </Box>
                    <Center><Box as='button' onClick={(e) => getStoreItem(e)} borderRadius='md' bg='teal.500' color='white' mt="2vh" px={4} h={8} minW="70vh">Import</Box></Center>
                </Container>
            </Header>
        </>
    )
}
