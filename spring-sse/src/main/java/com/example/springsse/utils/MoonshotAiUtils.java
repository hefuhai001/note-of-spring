package com.example.springsse.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.springsse.entity.Message;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.*;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;


public class MoonshotAiUtils {

    private static final String API_KEY = "sk-Qukizc0hNzQOXoBBG25cKwwVKk6DdFsqy774hX6buj2dtHkB";
    private static final String MODELS_URL = "https://api.moonshot.cn/v1/models";
    private static final String FILES_URL = "https://api.moonshot.cn/v1/files";
    private static final String ESTIMATE_TOKEN_COUNT_URL = "https://api.moonshot.cn/v1/tokenizers/estimate-token-count";
    private static final String CHAT_COMPLETION_URL = "https://api.moonshot.cn/v1/chat/completions";

    public static String getModelList() {
        return getCommonRequest(MODELS_URL)
                .execute()
                .body();
    }

    public static String uploadFile(@NonNull File file) {
        return getCommonRequest(FILES_URL)
                .method(Method.POST)
                .header("purpose", "file-extract")
                .form("file", file)
                .execute()
                .body();
    }

    public static String getFileList() {
        return getCommonRequest(FILES_URL)
                .execute()
                .body();
    }

    public static String deleteFile(@NonNull String fileId) {
        return getCommonRequest(FILES_URL + "/" + fileId)
                .method(Method.DELETE)
                .execute()
                .body();
    }

    public static String getFileDetail(@NonNull String fileId) {
        return getCommonRequest(FILES_URL + "/" + fileId)
                .execute()
                .body();
    }

    public static String getFileContent(@NonNull String fileId) {
        return getCommonRequest(FILES_URL + "/" + fileId + "/content")
                .execute()
                .body();
    }

    public static String estimateTokenCount(@NonNull String model, @NonNull List<Message> messages) {
        String requestBody = new JSONObject()
                .putOpt("model", model)
                .putOpt("messages", messages)
                .toString();
        return getCommonRequest(ESTIMATE_TOKEN_COUNT_URL)
                .method(Method.POST)
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .body(requestBody)
                .execute()
                .body();
    }

    @SneakyThrows
    public static void chat(@NonNull String model, @NonNull List<Message> messages) {
        String requestBody = new JSONObject()
                .putOpt("model", model)
                .putOpt("messages", messages)
                .putOpt("stream", true)
                .toString();
        Request okhttpRequest = new Request.Builder()
                .url(CHAT_COMPLETION_URL)
                .post(RequestBody.create(requestBody, MediaType.get(ContentType.JSON.getValue())))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();
        Call call = new OkHttpClient().newCall(okhttpRequest);
        Response okhttpResponse = call.execute();
        BufferedReader reader = new BufferedReader(okhttpResponse.body().charStream());
        String line;
        while ((line = reader.readLine()) != null) {
            if (StrUtil.isBlank(line)) {
                continue;
            }
            if (JSONUtil.isTypeJSON(line)) {
                Optional.of(JSONUtil.parseObj(line))
                        .map(x -> x.getJSONObject("error"))
                        .map(x -> x.getStr("message"))
                        .ifPresent(x -> System.out.println("error: " + x));
                return;
            }
            line = StrUtil.replace(line, "data: ", StrUtil.EMPTY);
            if (StrUtil.equals("[DONE]", line) || !JSONUtil.isTypeJSON(line)) {
                return;
            }
            Optional.of(JSONUtil.parseObj(line))
                    .map(x -> x.getJSONArray("choices"))
                    .filter(CollUtil::isNotEmpty)
                    .map(x -> (JSONObject) x.get(0))
                    .map(x -> x.getJSONObject("delta"))
                    .map(x -> x.getStr("content"))
                    .ifPresent(x -> {
                        System.out.println("rowData: " + x);
                    });
        }
    }

    private static HttpRequest getCommonRequest(@NonNull String url) {
        return HttpRequest.of(url).header(Header.AUTHORIZATION, "Bearer " + API_KEY);
    }


    /**
     * 改造后的 chat 方法，返回 Flux<String> 以支持流式响应
     *
     * @param model    模型名称
     * @param messages 消息列表
     * @return Flux<String> 流式响应数据
     */
    public static Flux<String> chatStream(@NonNull String model, @NonNull List<Message> messages) {
        return Flux.create(emitter -> {
            try {
                // 构建请求体
                String requestBody = new JSONObject()
                        .putOpt("model", model)
                        .putOpt("messages", messages)
                        .putOpt("stream", true)
                        .toString();

                // 创建 OkHttp 请求
                Request request = new Request.Builder()
                        .url(CHAT_COMPLETION_URL)
                        .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .build();

                // 异步执行请求
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.error(e); // 请求失败时发送错误
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if (StrUtil.isBlank(line)) {
                                    continue;
                                }

                                // 处理 SSE 数据
                                line = StrUtil.replace(line, "data: ", StrUtil.EMPTY);
                                if (StrUtil.equals("[DONE]", line)) {
                                    emitter.complete(); // 流结束时完成
                                    return;
                                }

                                if (JSONUtil.isTypeJSON(line)) {
                                    Optional.of(JSONUtil.parseObj(line))
                                            .map(x -> x.getJSONArray("choices"))
                                            .filter(CollUtil::isNotEmpty)
                                            .map(x -> (JSONObject) x.get(0))
                                            .map(x -> x.getJSONObject("delta"))
                                            .map(x -> x.getStr("content"))
                                            .ifPresent(emitter::next); // 发送数据到 Flux
                                }
                            }
                        } catch (Exception e) {
                            emitter.error(e); // 处理异常
                        } finally {
                            emitter.complete(); // 确保流结束
                        }
                    }
                });
            } catch (Exception e) {
                emitter.error(e); // 处理异常
            }
        });
    }

}
