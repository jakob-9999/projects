import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    server: {
        // Intercepts all requests from the client prefixed with /api and forwards it to localhost:8080. This ensures
        // is necessary since the frontend and backend runs on different ports. It also allows for hot reloading frontend changes
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
            }
        },
        port: 3000,
    }
})
