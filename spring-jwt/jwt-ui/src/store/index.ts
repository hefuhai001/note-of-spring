import {defineStore} from "pinia";

/**
 * 其内部可以包含，
 * 状态（state），
 * 操作（actions）,
 * getter（getters）
 */
export const index = defineStore("index", {
    state: () => ({
        token: JSON.parse(localStorage.getItem("token")),
    }),
    actions: {
        setToken(token: any) {
            //@ts-ignore
            this.token = token;
        },
    },
});
