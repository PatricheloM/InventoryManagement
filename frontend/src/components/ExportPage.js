import { Box, Grid, GridItem, useToast, Card, CardHeader, CardBody, Heading, Text, Stack, StackDivider, Center, useDisclosure, ModalOverlay, Modal, FormControl, FormLabel, Input, ModalContent, Button, ModalFooter, ModalCloseButton, ModalBody, ModalHeader, Select, NumberInput, NumberInputField, NumberInputStepper, NumberIncrementStepper, NumberDecrementStepper, Spinner } from '@chakra-ui/react';
import axios from 'axios';
import { useEffect, useState } from 'react';
import Header from '../layout/Header';
import { SingleDatepicker } from "chakra-dayzed-datepicker";
import { useNavigate } from 'react-router-dom';

export default function ExportPage() {
    const [exportButtonDisabled, setExportButtonDisabled] = useState(false);
    const navigate = useNavigate();

    async function getStoreItem(e, obj) {
        e.preventDefault();
        setExportButtonDisabled(true);
        axios.post("http://localhost:8080/api/item/exporting?token=" + token, obj)
            .then(function (response) {
                toast({
                    title: 'Exporting successful!',
                    status: 'success',
                    duration: 10000,
                    isClosable: true,
                })
                let pdfWindow = window.open("")
                pdfWindow.document.write(
                    "<object width='100%' height='100%' data='data:application/pdf;base64, " +
                    response.data.pdf + "'></object>"
                )
                navigate('/main');
            })
            .catch(function (error) {
                if (error.response.data.status === 401) {
                    toast({
                        title: 'You can\'t export!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                    setExportButtonDisabled(false);
                }
                if (error.response.data.status === 400) {
                    toast({
                        title: 'Can\'t process request!',
                        description: 'Invalid item details',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                    setExportButtonDisabled(false);
                }
                if (error.response.data.status === 500) {
                    toast({
                        title: 'Internal server error!',
                        status: 'error',
                        duration: 3000,
                        isClosable: true,
                    })
                    setExportButtonDisabled(false);
                }
            });
    }

    const [nonDate, setNonDate] = useState(true);

    const [queryType, setQueryType] = useState("All items: ");
    const [queryValue, setQueryValue] = useState("");

    const [query, setQuery] = useState("item");
    const [name, setName] = useState("");
    const [company, setCompany] = useState("");
    const [warehouse, setWarehouse] = useState("");
    const [cell, setCell] = useState("");
    const [startDate, setStartDate] = useState();
    const [endDate, setEndDate] = useState();

    const { isOpen: isOpenN, onOpen: onOpenN, onClose: onCloseN } = useDisclosure()
    const { isOpen: isOpenC, onOpen: onOpenC, onClose: onCloseC } = useDisclosure()
    const { isOpen: isOpenW, onOpen: onOpenW, onClose: onCloseW } = useDisclosure()
    const { isOpen: isOpenD, onOpen: onOpenD, onClose: onCloseD } = useDisclosure()
    const toast = useToast();
    const token = localStorage.getItem('IMTOKEN');

    let noItem = (<Center><Heading>No items found</Heading></Center>);

    function setNullState(openFunc) {
        setName("");
        setCompany("");
        setWarehouse("");
        setCell("");
        setStartDate();
        setEndDate();
        setNonDate(true);
        openFunc();
    }

    function defaultQuery() {
        setNonDate(true);
        setQuery("item");
        setQueryType("All items: ");
        setQueryValue("");
    }

    let itemList = [];
    const [card, setCard] = useState([]);
    card.forEach((item, key) => {
        itemList.push(
            <GridItem key={key}>
                <Card bg="whiteAlpha.900" boxShadow="md">
                    <CardHeader>
                        <Heading size='xl'>{item?.name}</Heading>
                    </CardHeader>

                    <CardBody>
                        <Stack divider={<StackDivider />} spacing='4'>
                            <Box>
                                <Heading size='xs' textTransform='uppercase'>
                                    Weight
                                </Heading>
                                <Text pt='2' fontSize='sm'>
                                    {item?.weight + " kg"}
                                </Text>
                            </Box>
                            <Box>
                                <Heading size='xs' textTransform='uppercase'>
                                    Manufacturer company
                                </Heading>
                                <Text pt='2' fontSize='sm'>
                                    {item?.company}
                                </Text>
                            </Box>
                            <Box>
                                <Heading size='xs' textTransform='uppercase'>
                                    Arrival
                                </Heading>
                                <Text pt='2' fontSize='sm'>
                                    {new Date(Date.parse(item?.arrival)).toUTCString()}
                                </Text>
                            </Box>
                            <Box>
                                <Heading size='xs' textTransform='uppercase'>
                                    Location
                                </Heading>
                                <Text pt='2' fontSize='sm'>
                                    {item?.location}
                                </Text>
                            </Box>
                            <Center>
                                <Spinner
                                    mt="1vh"
                                    thickness='4px'
                                    speed='0.65s'
                                    emptyColor='gray.200'
                                    color='teal.500'
                                    size='xl'
                                    display={exportButtonDisabled ? "block" : "none"} />
                                <Button width="100%" display={exportButtonDisabled ? "none" : "block"} disabled={exportButtonDisabled} onClick={(e) => getStoreItem(e, card.at(key))}>Export</Button>
                            </Center>
                        </Stack>
                    </CardBody>
                </Card>
            </GridItem>
        );
    });

    useEffect(() => {
        function search() {
            if (nonDate) {
                axios.get("http://localhost:8080/api/item/" + query + "?token=" + token)
                    .then(function (response) {
                        setCard(response.data);
                    })
                    .catch(function (error) {
                        if (error.response.data.status === 500) {
                            toast({
                                title: 'Internal server error!',
                                status: 'error',
                                duration: 3000,
                                isClosable: true,
                            })
                        }
                    })
            } else {
                axios.post("http://localhost:8080/api/item/date?token=" + token, {
                    "start": startDate,
                    "end": endDate
                })
                    .then(function (response) {
                        setCard(response.data);
                    })
                    .catch(function (error) {
                        if (error.response.data.status === 500) {
                            toast({
                                title: 'Internal server error!',
                                status: 'error',
                                duration: 3000,
                                isClosable: true,
                            })
                        }
                    })
            }
        }
        search();
    }, [query, nonDate])


    return (
        <>
            <Modal
                isOpen={isOpenN}
                onClose={onCloseN}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Sort by name</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl mb="4">
                            <FormLabel>Item name</FormLabel>
                            <Input placeholder='Item name' onChange={(e) => setName(e.target.value)} />
                        </FormControl>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={() => {
                            if (name !== "") {
                                setNonDate(true);
                                setQuery("name/" + name);
                                setQueryType("Search for item: ");
                                setQueryValue("'" + name + "'");
                                onCloseN();
                            }
                            if (name === "") {
                                toast({
                                    title: 'Give an item name!',
                                    status: 'error',
                                    duration: 3000,
                                    isClosable: true,
                                })
                            }
                        }}
                            variant="solid"
                            colorScheme="teal" mr={3}>
                            Search
                        </Button>
                        <Button onClick={onCloseN}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Modal
                isOpen={isOpenC}
                onClose={onCloseC}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Sort by company</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl mb="4">
                            <FormLabel>Manufacturer company</FormLabel>
                            <Input placeholder='Manufacturer company' onChange={(e) => setCompany(e.target.value)} />
                        </FormControl>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={() => {
                            if (company !== "") {
                                setNonDate(true);
                                setQuery("company/" + company);
                                setQueryType("Search for company: ");
                                setQueryValue("'" + company + "'");
                                onCloseC();
                            }
                            if (company === "") {
                                toast({
                                    title: 'Give a company!',
                                    status: 'error',
                                    duration: 3000,
                                    isClosable: true,
                                })
                            }
                        }}
                            variant="solid"
                            colorScheme="teal" mr={3}>
                            Search
                        </Button>
                        <Button onClick={onCloseC}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Modal
                isOpen={isOpenW}
                onClose={onCloseW}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Sort by location</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl>
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
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={() => {
                            if (warehouse !== "" && cell !== "") {
                                setNonDate(true);
                                setQuery("location/" + warehouse + "-" + cell);
                                setQueryType("Search for location: ");
                                setQueryValue(warehouse + "-" + cell);
                                onCloseW();
                            }
                            if (warehouse === "" && cell === "") {
                                toast({
                                    title: 'Give a warehouse and a cell!',
                                    status: 'error',
                                    duration: 3000,
                                    isClosable: true,
                                })
                            }
                        }}
                            variant="solid"
                            colorScheme="teal" mr={3}>
                            Search
                        </Button>
                        <Button onClick={onCloseW}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Modal
                isOpen={isOpenD}
                onClose={onCloseD}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Sort by date</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <FormControl mb="4">
                            <FormLabel>Start date</FormLabel>
                            <SingleDatepicker
                                date={startDate}
                                onDateChange={setStartDate}
                            />
                        </FormControl>
                        <FormControl mb="4">
                            <FormLabel>End date</FormLabel>
                            <SingleDatepicker
                                date={endDate}
                                onDateChange={setEndDate}
                            />
                        </FormControl>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={() => {
                            if (typeof startDate !== 'undefined' && typeof endDate !== 'undefined' && startDate < endDate) {
                                setNonDate(false);
                                setQuery("date");
                                setQueryType("Search date range: ");
                                setQueryValue(startDate.toLocaleDateString("en-US") + " - " + endDate.toLocaleDateString("en-US"));
                                onCloseD();

                            } else {
                                if (typeof startDate === 'undefined' && typeof endDate === 'undefined') {
                                    toast({
                                        title: 'Give a start and an end date!',
                                        status: 'error',
                                        duration: 3000,
                                        isClosable: true,
                                    })
                                } else {
                                    toast({
                                        title: 'Start date must be before end date!',
                                        status: 'error',
                                        duration: 3000,
                                        isClosable: true,
                                    })
                                }
                            }
                        }}
                            variant="solid"
                            colorScheme="teal" mr={3}>
                            Search
                        </Button>
                        <Button onClick={onCloseD}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Header>
                <Box p={4} marginBottom="1vw" backgroundColor="gray.100" boxShadow="md" minW="75vw">
                    <Grid templateColumns='repeat(5, 1fr)' gap={6} >
                        <GridItem><Center><Box as='button' onClick={() => setNullState(defaultQuery)} minWidth="10vw" borderRadius='md' bg='black' color='white' px={4} h={8}>All items</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' onClick={() => setNullState(onOpenN)} minWidth="10vw" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Sort by name</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' onClick={() => setNullState(onOpenC)} minWidth="10vw" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Sort by company</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' onClick={() => setNullState(onOpenW)} minWidth="10vw" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Sort by location</Box></Center></GridItem>
                        <GridItem><Center><Box as='button' onClick={() => setNullState(onOpenD)} minWidth="10vw" borderRadius='md' bg='teal.300' color='white' px={4} h={8}>Sort by date</Box></Center></GridItem>
                    </Grid>
                </Box>
                <Center mb="2vh"><Heading color="teal.500">{queryType + queryValue}</Heading></Center>
                {itemList.length === 0 ? noItem : <Grid templateColumns='repeat(4, 1fr)' gap={6}>{itemList}</Grid>}
            </Header>
        </>
    );
}
