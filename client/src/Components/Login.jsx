import React from 'react';
import '../App.css';
import { useNavigate } from 'react-router-dom';

// Function for the Login component, used for rendering the login form and
// handling the user's login including saving the user's JWT token.
export default function Login() {
  const navigate = useNavigate();
  const [data, setData] = React.useState({
    username: "",
    password: ""
  });

  // function to update the React state data when the user inputs their details
  function handleChange(event) {
    const { name, value } = event.target;
    setData({ ...data, [name]: value });
  }

  // function to send the user details to the login API endpoint
  async function login(event) {
    event.preventDefault();

    const response = await fetch("/api/users/auth/login", {
      method: "POST",
      cache: "no-cache",
      headers: {"Content-Type": "application/json"},
      redirect: "follow",
      referrerPolicy: "no-referrer",
      body: JSON.stringify(data),
      credentials: "same-origin",
    });

     // display any errors returned by the server if the response status code is not 200
    if (response["status"] === 200) {
      // if there are no login errors redirect the user to the menu page and save the
      // JWT token to session storage
      const responseData = await response.text();
      sessionStorage.setItem("token", responseData);
      navigate("/menu");
    }
    else {
      const errorMsg = document.getElementById("error");
      const errorText = await response.text();
      errorMsg.textContent = errorText;
    }
  }

  return (
    <div className="App">
      <h1 className="register-login-title">Login</h1>
      <form className="register-login">
        <p id="error"></p>
        <input placeholder="Username" name="username" type="text" onChange={handleChange} /><br />
        <input placeholder="Password" name="password" type="password" onChange={handleChange} /><br />
        <button onClick={login}>Login</button>
      </form>
      <br />
      <a href="#/register" className="register-login-link">Don't have an account?</a>
    </div>
  );
}