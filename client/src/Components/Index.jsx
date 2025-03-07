import React from "react"
import Header from "./Header"
import { useNavigate } from "react-router-dom";

// function creates the Index component which is the first page users see when visiting the application
export default function Index() {
    const navigate = useNavigate();
    
    // if the user has a JWT token stored in session storage, redirect them to the menu page
    React.useEffect(() => {
        if (sessionStorage.getItem("token")) {
            navigate("/menu");
        }
    }, []);

    return (
        <React.Fragment>
            <Header online={true} />
            <main>
                Welcome to Chess online, a platform that allows you to play chess with anyone around the world!
                <br /><br />
                Alternatively, you can play offline with a friend on the same computer.
                <br /><br />
                To get started please create an account with us.
            </main>
        </React.Fragment>
    );
}