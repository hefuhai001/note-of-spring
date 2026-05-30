/**
 * Vite 配置文件
 *
 * <p>项目构建和开发服务器配置</p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li><b>Vue 3 支持</b>：使用 @vitejs/plugin-vue 编译 .vue 文件</li>
 *   <li><b>反向代理</b>：开发环境自动将 /api 请求转发到后端 Spring Boot 服务（解决 CORS 问题）</li>
 *   <li><b>路径别名</b>：@ 指向 src 目录，简化导入路径</li>
 *   <li><b>开发服务器</b>：配置端口、自动打开浏览器等选项</li>
 * </ul>
 *
 * <h3>CORS 问题解决方案：</h3>
 * <pre>
 * 开发环境：
 *   前端 (http://localhost:5173)
 *     ↓ 发送请求到 /api/...
 *     ↓ Vite Dev Server 拦截
 *     ↓ 转发到 http://localhost:8081
 *   后端 Spring Boot (http://localhost:8081)
 *
 * 生产环境：
 *   前端 (nginx 静态资源)
 *     ↓ 发送请求到 /api/...
 *     ↓ nginx 反向代理
 *     ↓ 转发到 Spring Boot 后端服务
 * </pre>
 *
 * @author AI Assistant
 * @version 1.0.0
 * @since 2026-05-30
 */

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],

  /**
   * 开发服务器配置
   */
  server: {
    /** 前端开发服务器端口（默认 5173） */
    port: 5173,

    /** 启动时自动打开浏览器 */
    open: true,

    /** 允许访问的主机（0.0.0.0 表示允许所有，包括局域网访问） */
    host: '0.0.0.0',

    /**
     * 反向代理配置
     *
     * <p>核心作用：解决开发环境的 CORS（跨域资源共享）问题</p>
     *
     * <h4>工作原理：</h4>
     * <ol>
     *   <li>前端代码发送请求到 /api/* 路径</li>
     *   <li>Vite 开发服务器检测到 /api 前缀</li>
     *   <li>将请求转发到配置的目标地址（Spring Boot 后端）</li>
     *   <li>后端处理请求并返回响应</li>
     *   <li>Vite 将响应返回给前端（对前端透明）</li>
     * </ol>
     *
     * <h4>为什么需要反向代理？</h4>
     * <ul>
     *   <li><b>同源策略限制</b>：浏览器的安全策略禁止前端直接请求不同域名的后端 API</li>
     *   <li><b>开发环境便利性</b>：无需在后端配置 CORS 或使用 JSONP 等方案</li>
     *   <li><b>模拟生产环境</b>：生产环境通常使用 nginx 做同样的代理</li>
     * </ul>
     *
     * <h4>配置示例说明：</h4>
     * <pre>
     * // 前端发送: GET /api/file/upload
     * // 实际转发: GET http://localhost:8081/file/upload
     * // 注意：/api 前缀会被移除（rewrite 规则）
     * </pre>
     */
    proxy: {
      /**
       * /api 路径代理规则
       *
       * <p>匹配所有以 /api 开头的请求路径</p>
       */
      '/api': {
        /** 代理目标地址（Spring Boot 后端服务） */
        target: 'http://localhost:8080',

        /**
         * 是否修改请求头中的 Origin 字段
         *
         * <p>true: 将 Origin 改为目标地址（避免后端 CORS 检查失败）</p>
         * <p>false: 保持原始 Origin（如果后端已配置 CORS 可设为 false）</p>
         */
        changeOrigin: true,

        /**
         * 路径重写规则
         *
         * <p>将请求路径中的 /api 前缀移除</p>
         *
         * <p>示例：</p>
         * <ul>
         *   <li>前端请求：/api/file/upload</li>
         *   <li>重写后：/file/upload</li>
         *   <li>最终发送到：http://localhost:8081/file/upload</li>
         * </ul>
         *
         * <p>原因：</p>
         * <ul>
         *   <li>后端接口路径是 /file/*，不是 /api/file/*</li>
         *   <li>/api 只是前端的约定前缀，用于区分 API 请求和其他静态资源</li>
         *   <li>生产环境 nginx 也需要做相同的 rewrite</li>
         * </ul>
         */
        rewrite: (path) => path.replace(/^\/api/, ''),

        /**
         * 是否启用 WebSocket 代理（如需实时通信可开启）
         */
        ws: false,

        /**
         * 代理超时时间（毫秒）
         *
         * <p>适用于大文件上传场景，避免长时间上传被中断</p>
         * <p>建议设置为 5 分钟（300000ms）以上以支持大文件分片上传</p>
         */
        timeout: 300000, // 5 分钟

        /**
         * 错误处理回调（可选）
         *
         * <p>当代理请求失败时触发，可用于日志记录或错误提示</p>
         */
        // configure: (proxy, options) => {
        //   proxy.on('error', (err, req, res) => {
        //     console.log('proxy error', err);
        //   });
        //   proxy.on('proxyReq', (proxyReq, req, res) => {
        //     console.log('Proxying:', req.method, req.url);
        //   });
        // },
      },

      /**
       * MinIO 静态资源代理（可选）
       *
       * <p>如果需要在前端直接访问 MinIO 的文件预览 URL，可以添加此代理</p>
       * <p>注意：生产环境建议使用 Nginx 直接代理 MinIO，而非通过 Node 中转</p>
       */
      // '/minio': {
      //   target: 'http://localhost:9000',
      //   changeOrigin: true,
      // },
    },

    /**
     * CORS 预检请求配置（可选增强）
     *
     * <p>通常情况下使用 proxy 已足够，以下配置作为备用方案</p>
     */
    cors: true,
  },

  /**
   * 路径别名配置
   *
   * <p>简化模块导入路径，提高代码可读性和维护性</p>
   *
   * <h4>使用示例：</h4>
   * <pre>
   * // 配置前：
   * import request from '../../../utils/request'
   * import FileService from '../../services/FileService'
   *
   * // 配置后：
   * import request from '@/utils/request'
   * import FileService from '@/services/FileService'
   * </pre>
   */
  resolve: {
    alias: {
      /** @ 别名指向 src 目录 */
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },

  /**
   * 构建优化配置（可选）
   */
  build: {
    /** 输出目录 */
    outDir: 'dist',

    /** 静态资源目录 */
    assetsDir: 'assets',

    /** 是否生成 source map（生产环境建议关闭以保护源码） */
    sourcemap: false,

    /** 分包策略（优化首屏加载速度） */
    rollupOptions: {
      output: {
        manualChunks: {
          // 将第三方库单独打包
          vendor: ['vue', 'axios'],
          // Element Plus 单独打包（如果使用了的话）
          'element-plus': ['element-plus'],
        },
      },
    },

    /** chunk 大小警告阈值（KB） */
    chunkSizeWarningLimit: 1000,
  },

  /**
   * CSS 相关配置
   */
  css: {
    /** 是否使用 css 预处理器（如需使用 SCSS/Less 可在此配置） */
    preprocessorOptions: {},
  },

  /**
   * 依赖优化配置（Vite 预构建）
   */
  optimizeDeps: {
    /** 强制预构建的依赖（解决某些库的兼容性问题） */
    include: ['vue', 'axios'],
  },
})
