const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        proxy: {
            '/api': {//'/api'请求前缀
                target: 'http://localhost:9090',
                pathRewrite: {'^/api': ''}, // *匹配 /api 改为空，然后代理服务器会直接去 3000/students 请求
                // *下面不写，都是默认为true
                ws: true, // 用于支持websocket
                changeOrigin: true // 用于控制请求头中的host值
            }
        }
    }
})
