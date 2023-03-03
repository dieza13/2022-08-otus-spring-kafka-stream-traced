import './css/App.css';
import Main from './components/Main';
import React, {Component} from 'react';

import {
  BrowserRouter as Router,
  Routes,
  Route,
} from 'react-router-dom';

import Header from './components/header/Header';

function App() {
  return ( 
    <Router>
      <div>
        <Header />

        <Routes>
          <Route path="/" element={<Main />} ></Route>
        </Routes>

      </div>
    </Router>
  );
}

export default App;