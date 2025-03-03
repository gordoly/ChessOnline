import React from 'react';
import '../App.css';

// Function for the Register component, used for rendering the registration form and
// handling the user's registration.
export default function Register() {
  const [data, setData] = React.useState({
      username: "",
      password: "",
      reEnteredPassword: "",
    });
    
    // function to update the React state data when the user inputs their details
    function handleChange(event) {
      const { name, value } = event.target;
      setData({ ...data, [name]: value });
    }

    // function to send the user details to the registration API endpoint
    async function register(event) {
      event.preventDefault();
      const response = await fetch("/api/users/auth/register", {
        method: "POST",
        cache: "no-cache",
        headers: {"Content-Type": "application/json"},
        redirect: "follow",
        referrerPolicy: "no-referrer",
        body: JSON.stringify(data),
        credentials: "same-origin",
      });

      // display any errors returned by the server if the response status code is not 201
      const errorMsg = document.getElementById("error");
      if (response["status"] !== 201) {
        const errText = await response.text()
        errorMsg.textContent = errText;
      }
      else {
        // if there are no registration errors redirect the user to the login page
        window.location.href = "/login";
      }
    }

  return (
      <div className="App">
          <h1 className="register-login-title">Register your details</h1>
          <form className="register-login">
              <p id="error"></p>
              <input placeholder="Username" name="username" type="text" onChange={handleChange} /><br />
              <input placeholder="Password" name="password" type="password" onChange={handleChange} /><br />
              <input placeholder="Re-enter Password" name="reEnteredPassword" type="password" onChange={handleChange} /><br />
              <button onClick={register}>Register</button>
          </form>
          <br />
          <a href="/login" className="register-login-link">Already have an account?</a>
      </div>
  );
}