import React from "react"
import Header from "./Header"

export default function Wait() {
    React.useEffect(() => {
        if (sessionStorage.getItem("token")) {
            document.location.href = "/menu";
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