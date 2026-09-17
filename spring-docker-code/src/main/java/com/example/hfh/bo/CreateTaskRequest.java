package com.example.hfh.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "CreateTaskRequest", description = "创建任务请求参数")
public class CreateTaskRequest {

    @Schema(description = "编程语言（c/java/python/javascript/rust/dotnet/go/cpp）")
    private String language;

    @Schema(description = "源代码")
    private String code;

    @Schema(description = "程序运行参数列表，如 [\"--verbose\", \"100\"]")
    private List<String> args;

}
