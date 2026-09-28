import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { AuthProvider } from './context/AuthContext'
import './index.css'
import logo from '../images/Logo_Adopet.png'

const favicon = document.createElement('link')
favicon.rel = 'icon'
favicon.type = 'image/png'
document.head.appendChild(favicon)

const faviconImage = new Image()
faviconImage.src = logo
faviconImage.onload = () => {
  const canvas = document.createElement('canvas')
  canvas.width = 500
  canvas.height = 500
  const context = canvas.getContext('2d')
  const scale = 1.3
  const size = canvas.width * scale
  const offset = (canvas.width - size) / 2
  context.drawImage(faviconImage, offset, offset, size, size)
  favicon.href = canvas.toDataURL('image/png')
}
document.title = 'Adopet'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <App />
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
)
