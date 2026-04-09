import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
    plugins: [vue()],
    server: {
        port: 3000,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                configure: (proxy, options) => {
                    proxy.on('error', (err, req, res) => {
                        console.log('代理错误:', err);
                    });
                    proxy.on('proxyReq', (proxyReq, req, res) => {
                        console.log('代理请求:', req.method, req.url);
                    });
                }
            },
            '/actuator': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                headers: {
                    'Authorization': 'Basic ' + btoa('admin:admin123')
                },
                configure: (proxy, options) => {
                    proxy.on('error', (err, req, res) => {
                        console.log('代理错误:', err);
                    });
                    proxy.on('proxyReq', (proxyReq, req, res) => {
                        console.log('代理请求:', req.method, req.url);
                    });
                }
            }
        }
    }
})