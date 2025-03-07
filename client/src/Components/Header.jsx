import { useNavigate } from 'react-router-dom';
import logo from "../assets/pawnW.png";

// function deals with drawing the header of the website on the top of the screen which includes
// text and an image on the left hand side which when clicked will take the user to the home page
export default function Header(props) {
    const navigate = useNavigate();

    function returnHome() {
        navigate("/");
    }

    return (
        <header className="App-header">
            <div>
                <img src={logo} className="App-logo" alt="logo" onClick={returnHome} style={{ width: "50px", height: "50px" }} />
                <a href="#/leaderboard">Leader board</a>
            </div>
            <p className="title" style={{ display: "inline-block", marginLeft: "40%" }}>
                Play Chess
            </p>
            { props.online && <aside className="login-links"><a href="#/login">Login</a> <a href="#/register">Signup</a></aside>}
        </header>
    )
}