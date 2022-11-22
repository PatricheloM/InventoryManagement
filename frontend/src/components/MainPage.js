import { Box, Grid, GridItem, useToast } from '@chakra-ui/react';
import axios from 'axios';
import { useEffect, useState } from 'react';
import Header from '../layout/Header';

export default function MainPage() {

    const toast = useToast();
    const token = localStorage.getItem('IMTOKEN');

    useEffect(() => {
        axios.get("http://localhost:8080/api/item/item?token=" + token)
            .then(function (response) {
                console.log(response.data);
            })
            .catch(function (error) {
                toast({
                    title: 'Internal server error!',
                    status: 'error',
                    duration: 3000,
                    isClosable: true,
                })
            })
    }, [])


    return (
        <>
            <Header>
                <Grid templateColumns='repeat(4, 1fr)' gap={6}>
                </Grid>
            </Header>
        </>
    );
}
