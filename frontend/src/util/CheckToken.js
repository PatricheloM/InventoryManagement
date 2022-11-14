import axios from "axios";

export default async function check(token) {
    await axios.get("http://localhost:8080/api/account/token?token=" + token)
    .then(function (response) {
        return true;
    })
    .catch(function (error) {
        return false;
    });
}