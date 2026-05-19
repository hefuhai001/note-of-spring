package asia.hfh.code.bo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "CreateTaskRequest", description = "创建任务请求参数")
public class CreateTaskRequest {

    @Schema(description = "C语言源代码")
    private String code;

    @Schema(description = "程序运行参数")
    private JsonNode args;

}