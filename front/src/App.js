import './App.css';
import React, { useState } from 'react'
import Locate from './components/Locate';

function App() {
  const [form] = useState();
  return (
      <div>
        <Locate handleChoice={form} display={"Form"}/>
      </div>
  )
}

export default App;
