package com.common.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginParam {

    @Schema(description = "账号")
    private String userAcc;

    @Schema(description = "密码")
    private String userPwd;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "角色")
    private String userIde;

}
