import React from 'react';
import '../App.css';

export default function Welcome() {
  const [data, setData] = React.useState({
    username: "",
    password: ""
  });

  function handleChange(event) {
    const { name, value } = event.target;
    setData({ ...data, [name]: value });
  }

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

    if (response["status"] === 200) {
      const responseData = await response.text();
      sessionStorage.setItem("token", responseData);
      window.location.href = "/menu";
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
      <a href="/register" className="register-login-link">Don't have an account?</a>
    </div>
  );
}