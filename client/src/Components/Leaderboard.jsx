import Header from "./Header";
import React, {useState} from "react";

// function that creates the Leader board component which provides a ranking of chess players
export default function Leaderboard() {
    const [users, setUsers] = useState([]);

    // fetch the leaderboard from the leaderboard API
    React.useEffect(() => {
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
                setUsers(data);
            })
            .catch(_ => {
                alert("Error connecting to the server, please try again later.");
            });
    }, []);

    return (
        <React.Fragment>
            <Header />
            <table>
                <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Username</th>
                        <th>Ranking points</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        users.map((item, index) => (
                            <tr>
                                <td>{index + 1}</td>
                                <td>{item["username"]}</td>
                                <td>{Math.round(item["score"])}</td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>
        </React.Fragment>
    )
}