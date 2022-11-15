import axios from "axios";

export default async function check(token) {
    await axios.get("http://localhost:8080/api/account/token/" + token)
    .then(function (response) {
        return response.data;
    })
    .catch(function (error) {
        return null;
    });
}