package com.auiucloud.core.douyin.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.hutool.core.collection.CollUtil;
import com.auiucloud.core.common.utils.YamlPropertyLoaderFactory;
import com.auiucloud.core.douyin.props.AppletsProperties;
import com.auiucloud.core.douyin.enums.AppletTypeEnum;
import com.auiucloud.core.douyin.model.AppletConfig;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.core.douyin.service.impl.DouyinAppletsServiceImpl;
import com.auiucloud.core.redis.core.RedisService;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抖音小程序配置
 *
 * @author dries
 **/
@AutoConfiguration
@EnableConfigurationProperties(AppletsProperties.class)
@PropertySource(factory = YamlPropertyLoaderFactory.class, value = "classpath:applet.yml")
public class AppletsConfiguration {

    @Resource
    private RedisService redisService;
    @Resource
    private AppletsProperties appletsProperties;

    private static final Map<String, DouyinAppletsService> DOUYIN_APPLETS_SERVICES = new HashMap<>();
    private static final Map<String, WxMaMessageRouter> ROUTERS = Maps.newHashMap();
    private static final Map<String, WxMaService> MA_SERVICES = Maps.newHashMap();

    public static DouyinAppletsService getDouyinAppletService(String appId) {
        DouyinAppletsService douyinAppletsService = DOUYIN_APPLETS_SERVICES.get(appId);

        if (douyinAppletsService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
        }

        return douyinAppletsService;
    }

    public static WxMaService getWechatAppletsService(String appId) {
        WxMaService wxMaService = MA_SERVICES.get(appId);

        if (wxMaService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
        }

        return wxMaService;
    }

    @PostConstruct
    public void init() {
        List<AppletConfig> configs = appletsProperties.getConfigs();
        if (CollUtil.isNotEmpty(configs)) {
            for (AppletConfig app : configs) {
                if (app.getProvider().equals(AppletTypeEnum.WECHAT.getValue())) {
                    WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                    config.setAppid(app.getAppId());
                    config.setSecret(app.getSecret());
                    config.setToken(app.getToken());
                    config.setAesKey(app.getAesKey());
                    config.setMsgDataFormat(app.getMsgDataFormat());

                    WxMaService service = new WxMaServiceImpl();
                    service.setWxMaConfig(config);
                    ROUTERS.put(app.getAppId(), this.newRouter(service));
                    MA_SERVICES.put(app.getAppId(), service);
                } else {
                    DouyinAppletsServiceImpl service = new DouyinAppletsServiceImpl(redisService, app);
                    DOUYIN_APPLETS_SERVICES.put(app.getAppId(), service);
                }
            }
        }
    }

    private WxMaMessageRouter newRouter(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router.rule().handler(logHandler).next().rule().async(false).content("模板").handler(templateMsgHandler).end().rule().async(false).content("文本").handler(textHandler).end().rule().async(false).content("图片").handler(picHandler).end().rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }

    private final WxMaMessageHandler templateMsgHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder().templateId("XtxPB0QAWcWZP2iLosThiM4L6ahcjriFSZ06c-0dvjc")
                // .data(Collections.singletonList(new WxMaSubscribeMessage.Data("", "")))
                // 点击模板跳转页面
                // .page()
                .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        System.out.println("收到消息：" + wxMessage.toString());
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson()).toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息").toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", "png", ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(WxMaKefuMessage.newImageBuilder().mediaId(uploadResult.getMediaId()).toUser(wxMessage.getFromUser()).build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            final File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(WxMaKefuMessage.newImageBuilder().mediaId(uploadResult.getMediaId()).toUser(wxMessage.getFromUser()).build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };


}
