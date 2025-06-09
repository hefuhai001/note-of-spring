import request from "@/request";

// 响应接口
export interface LoginRes {
    message: string;
    success: boolean;
    code: number;
    data: Record<string, unknown>;
}

/**
 * login
 * @param {string} acc
 * @param {string} pwd
 * @returns
 */
export function login(acc: string, pwd: string): Promise<LoginRes> {
    return request.post(`/common/login?acc=${acc}&pwd=${pwd}`);
}

// 响应接口
export interface TestRes {
    message: string;
    success: boolean;
    code: number;
    data: Record<string, unknown>;
}

/**
 * test
 * @returns
 */
export function test(): Promise<TestRes> {
    return request.post(`/common/test`);
}

