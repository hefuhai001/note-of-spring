package com.example.common.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdParam {
    @Schema(description = "ID，记录唯一标识")
    private Integer id;
}
