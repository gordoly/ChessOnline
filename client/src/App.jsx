import './App.css';
import ChessGame from './Components/ChessGame';
import Menu from './Components/Menu';
import { HashRouter as Router, Route, Routes } from "react-router-dom";
import React from "react";
import Register from './Components/Register';
import Login from './Components/Login';
import Index from './Components/Index';
import Leaderboard from './Components/Leaderboard'

// function creates the main App component to route users to specific
// web pages depending on the url path
function App() {
  
  return (
    <Router>
      <Routes>

        <Route path='/' element={
          <Index />
        } />

        <Route path='/menu' element={
          <Menu />
        } />

        <Route path='/play' element={
          <ChessGame
            x="50"
            y="200"
            colour="white"
            connected={false} />
        } />

        <Route path='/connect' element={
          <ChessGame
            x="50"
            y="200"
            connected={true} />
        } />

        <Route path='/register' element={
          <Register />
        } />

        <Route path='/login' element={
          <Login />
        } />

        <Route path='/leaderboard' element={
          <Leaderboard />
        } />

      </Routes>
    </Router>
  )
}

export default App;