package com.example.hfh.dto;

import lombok.Data;

/**
 * 欢迎邮件模板变量
 * <p>对应 templates/mail/welcome.html 模板中使用的变量</p>
 */
@Data
public class WelcomeTemplateVariables {

    /**
     * 用户姓名/昵称
     * <p>模板中通过 th:text="${username}" 引用</p>
     * <p>示例：张三、尊贵会员</p>
     */
    private String username;

    /**
     * 验证码/激活码
     * <p>模板中通过 th:text="${code}" 引用</p>
     * <p>示例：654321、VIP2026</p>
     */
    private String code;

    /**
     * 激活/跳转链接
     * <p>模板中通过 th:href="${link}" 引用</p>
     * <p>示例：https://example.com/activate</p>
     */
    private String link;

    /**
     * 验证码/链接有效期（分钟）
     * <p>模板中通过 th:text="${expireMinutes}" 引用</p>
     * <p>示例：30、60</p>
     */
    private Integer expireMinutes;
}