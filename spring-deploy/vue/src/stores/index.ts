import {defineStore} from "pinia";

export const index = defineStore("index", {
    state: () => ({
        user: {}
    }),
    actions: {
        initStore() {
            //@ts-ignore
            this.user = JSON.parse(localStorage.getItem("userInfo"))
        }
    },
    getters: {},
});

