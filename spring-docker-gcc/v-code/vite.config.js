import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())
  const proxyTarget = env.VITE_API_PROXY_TARGET || 'http://localhost:8080'
  
  console.log(`[Vite Proxy] Target: ${proxyTarget}`)
  
  return {
    plugins: [
      vue(),
      tailwindcss(),
    ],
    server: {
      port: 5173,
      proxy: {
        '/api': {
          target: proxyTarget,
          changeOrigin: true,
          configure: (proxy) => {
            proxy.on('proxyReq', (proxyReq, req) => {
              console.log(`[Proxy] ${req.method} ${req.url} -> ${proxyTarget}${req.url}`)
            })
            proxy.on('proxyRes', (proxyRes, req) => {
              console.log(`[Proxy] ${req.method} ${req.url} <- ${proxyRes.statusCode}`)
            })
            proxy.on('error', (err, req) => {
              console.error(`[Proxy Error] ${req.method} ${req.url}:`, err.message)
            })
          }
        }
      }
    }
  }
})
