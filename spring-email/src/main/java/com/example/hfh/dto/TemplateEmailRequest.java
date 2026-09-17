package com.example.hfh.dto;

import com.example.hfh.validation.AtLeastOneRecipient;
import com.example.hfh.validation.ValidEmailList;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 模板邮件发送请求对象
 * <p>基于 Thymeleaf 模板引擎渲染 HTML 内容后发送</p>
 * <p>支持单收件人和多收件人（群发）两种模式，二者至少填一项</p>
 * <p>当前仅支持 welcome 模板（邮件欢迎/激活场景）</p>
 */
@Data
@AtLeastOneRecipient(message = "收件人或收件人列表至少填写一项")
public class TemplateEmailRequest implements Recipient {

    /**
     * 单收件人邮箱地址
     * <p>适用于发送给单个收件人的场景</p>
     * <p>格式要求：符合标准邮箱格式</p>
     */
    @Email(message = "收件人邮箱格式不正确")
    private String to;

    /**
     * 多收件人邮箱地址列表（群发）
     * <p>适用于批量发送给多个收件人的场景</p>
     * <p>每个邮箱地址均需符合标准邮箱格式</p>
     */
    @ValidEmailList(message = "收件人列表中存在格式不正确的邮箱")
    private List<String> toList;

    /**
     * 邮件主题/标题
     * <p>不能为空</p>
     */
    @NotBlank(message = "邮件主题不能为空")
    private String subject;

    /**
     * Thymeleaf 模板名称
     * <p>对应 templates 目录下的模板文件路径，不含后缀</p>
     * <p>当前支持：mail/welcome（欢迎/激活邮件）</p>
     */
    private String templateName;

    /**
     * 模板变量数据
     * <p>根据 templateName 选择对应的变量类型</p>
     * <p>templateName=mail/welcome 时使用 {@link WelcomeTemplateVariables}</p>
     */
    private WelcomeTemplateVariables variables;
}