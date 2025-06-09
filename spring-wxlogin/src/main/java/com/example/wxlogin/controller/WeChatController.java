package com.example.wxlogin.controller;

import cn.hutool.core.util.IdUtil;
import com.example.wxlogin.utils.RespResult;
import com.example.wxlogin.utils.JedisUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    private WxMpService wxMpService;

    @GetMapping("/getLoginQrCode")
    public RespResult getLoginQrCode() throws WxErrorException {
        // 设置场景id，登录的时候需要根据这个获取redis中存在的openid
        String sceneStr = IdUtil.getSnowflake().nextIdStr();
        WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(sceneStr, 3600);
        String qrCodeUrl = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
        return RespResult.success(qrCodeUrl, sceneStr);
    }

    @GetMapping("/loginStatus")
    public RespResult loginStatus(@RequestParam String sceneId) {
        String openid = JedisUtil.getStr(sceneId);
        if (openid != null) {
            return RespResult.success("success", openid);
        } else {
            return RespResult.fail("fail");
        }
    }


}
