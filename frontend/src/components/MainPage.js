import LoginPage from "./LoginPage";
import check from "../util/CheckToken";

export default function MainPage() {
    if (!check(localStorage.getItem('IMTOKEN'))) {
        return (
            <LoginPage />
        );
    } else {
        return (
            <>
                Logged in.
            </>
        );
    }
}