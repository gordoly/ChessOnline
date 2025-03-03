import Header from "./Header";
import React, {useState} from "react";

// function creates the menu that is displayed when the user first signs in
export default function Menu() {
    const [userData, setUserData] = useState({"score": 0, "wins": 0, "losses": 0, "matches": 0});
    const [ranking, setRanking] = useState(-1);

    // function to indicate that the user intends to play chess online
    function offlineSubmit() {
        const offlineButton = document.getElementById("offline");
        offlineButton.value = "true";
    }

    // function to direct users to their intended destination
    function checkInput(event) {
        const offlineButton = document.getElementById("offline");
        if (offlineButton.value === "false") {
            const jwt = sessionStorage.getItem("token");
            fetch("/api/users/add", {
                method: 'POST',
                headers: {
                  "Authorization": `Bearer ${jwt}`
                }
            })
                .then(res => {
                    if (res["status"] === 200) {
                        document.location.href = "/connect";
                    } else {
                        alert("Your session has timed out, please login again.");
                        sessionStorage.removeItem("token");
                        document.location.href = "/login";
                    }
                });
        }
        else if (offlineButton.value === "true") {
            document.location.href = "/play";
        }
        event.preventDefault();
    }

    // effect hook to check if user already has a session on page load
    React.useEffect(() => {
        const jwt = sessionStorage.getItem("token");
        if (!jwt) {
            document.location.href = "/login";
        }
        else {
            // get the user's details from the webserver's REST API
            fetch("/api/users/get", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${jwt}`
                }
            })
            .then(async (data) => {
                if (data["status"] === 200) {
                    const resData = await data.json();
                    setUserData(resData);

                    // fetch the leader board from the API
                    fetch("/api/users/leaderboard")
                        .then(res => {
                            if (res.status === 200) {
                                return res.json();
                            }
                            else {
                                throw new Error('Server returned status: ' + res.status);
                            }
                        })
                        .then(data => {
                            data.forEach((entry, index) => {
                                if (entry["username"] === resData["username"]) {
                                    setRanking(index);
                                    return;
                                }
                            });
                        })
                        .catch(_ => {
                            alert("Error connecting to the server, please try again later.");
                        });
                }
                else {
                    alert("Your session has timed out, please login again.");
                    sessionStorage.removeItem("token");
                    document.location.href = "/login";
                }
            });
        }
    }, []);

    return (
        <React.Fragment>
            <Header />
            <form method="POST" id="form" onSubmit={checkInput}>
                <p id="error"></p>
                <button name="offline" value="false" style={{ marginRight: "10px" }} id="offline" onClick={offlineSubmit}>Play offline with a friend</button>
                <button type="submit" style={{ marginLeft: "10px" }}>Play online</button>
            </form>
            <table>
                <tr>
                    <td>Rank:</td>
                    <td>{ranking + 1}</td>
                </tr>
                <tr>
                    <td>Ranking points:</td>
                    <td>{Math.round(userData["score"])}</td>
                </tr>
                <tr>
                    <td>Win Rate:</td>
                    <td>{userData["matches"] === 0 ? "0%" : `${Math.round((userData["wins"] / userData["matches"]) * 100)}%`}</td>
                </tr>
                <tr>
                    <td>Loss Rate:</td>
                    <td>{userData["matches"] === 0 ? "0%" : `${Math.round((userData["losses"] / userData["matches"]) * 100)}%`}</td>
                </tr>
                <tr>
                    <td>Total matches played:</td>
                    <td>{userData["matches"]}</td>
                </tr>
            </table>
        </React.Fragment>
    )
}