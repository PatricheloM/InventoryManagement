import { Box, Grid, GridItem, useToast } from '@chakra-ui/react';
import axios from 'axios';
import { useEffect, useState } from 'react';
import Header from '../layout/Header';

export default function MainPage() {

    const toast = useToast();
    const token = localStorage.getItem('IMTOKEN');

    let itemList = [];
    const [title, setTitle] = useState([]);
    title.forEach((item, key) => {
        itemList.push(
            <GridItem>
                <Box id={key}>
                    {item.name}
                </Box>
            </GridItem>
        );
    });
    useEffect(() => {
        async function getStoreData() {
            const response = await axios.get("http://localhost:8080/api/item/item?token=" + token);
            setTitle(response.data);
        }
        getStoreData();
    }, []);


    return (
        <>
            <Header>
                <Grid templateColumns='repeat(4, 1fr)' gap={6}>
                    {itemList}
                </Grid>
            </Header>
        </>
    );
}
