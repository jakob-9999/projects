import { useState } from 'react'
import './App.css'
import IDFChart from './components/IDFChart';

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <IDFChart/>
    </>
  )
}

export default App
