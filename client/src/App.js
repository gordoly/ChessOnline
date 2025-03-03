import './App.css';
import ChessGame from './Components/ChessGame';
import Menu from './Components/Menu';
import { Route, Routes, BrowserRouter } from "react-router-dom";
import React from "react";
import Register from './Components/Register';
import Login from './Components/Login';
import Index from './Components/Index';
import Leaderboard from './Components/Leaderboard'

// function will route users to specific pages depending on the path in the url
function App() {
  
  return (
    <BrowserRouter basename='/'>
      <Routes path="/">

        <Route index element={<Index />} />

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
    </BrowserRouter>
  )
}

export default App;