import axios from "axios";
import {baseURL, timeout, headers} from "@/config";
import {index} from "@/store";
import {computed, reactive} from "vue";

const state = reactive({
    token: computed(() => index().token),
});

//@ts-ignore
const request = axios.create({
    baseURL,
    timeout,
    headers,
});

// 添加请求拦截器
request.interceptors.request.use(
    async (config) => {
        // 在发送请求之前做些什么
        if (state.token) {
            // let each request carry token
            config.headers["token"] = state.token;
        }
        return config;
    },
    function (error) {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

// 添加响应拦截器
request.interceptors.response.use(
    function (response) {
        // 2xx 范围内的状态码都会触发该函数。
        // 对响应数据做点什么
        return response.data;
    },
    function (error) {
        // 超出 2xx 范围的状态码都会触发该函数。
        // 对响应错误做点什么
        return Promise.reject(error);
    }
);

export default request;
