package cn.bugstack.service.impl;

import cn.bugstack.domain.vo.WeixinTemplateMessageVO;
import cn.bugstack.domain.req.WeixinQrCodeReq;
import cn.bugstack.domain.res.WeixinQrCodeRes;
import cn.bugstack.domain.res.WeixinTokenRes;
import cn.bugstack.service.ILoginService;
import cn.bugstack.service.weixin.IWeixinApiService;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-11-29 20:13
 */
@Service
public class WeixinLoginServiceImpl implements ILoginService {
    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template_id}")
    private String template_id;

    @Resource
    //就这里需要提前在配置类里面注册一下
    private Cache<String, String> weixinAccessToken;

    @Resource
    private IWeixinApiService weixinApiService;

    @Resource
    private Cache<String, String> openidToken;


    @Override
    public String createQrCodeTicket() throws IOException {
        //target: 获取ticket

        //1. 获取token,注意思考一下,为撒谎这个tokenRes对象会有4个参数
        String token = weixinAccessToken.getIfPresent(appid);

        if(null == token)
        {
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            //注意这个call的结构,call主要是一个包下面的
            WeixinTokenRes weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            token = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid,token);

        }
        //2. 生成ticket
        //  http请求方式: POST URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
        // POST数据格式：json POST数据例子：{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
        // 或者也可以使用以下POST数据创建字符串形式的二维码参数： {"action_name": "QR_LIMIT_STR_SCENE",
        // "action_info": {"scene": {"scene_str": "test"}}}
        WeixinQrCodeReq weixinQrCodeReq = WeixinQrCodeReq.builder()
                .expire_seconds(2592000)
                .action_name(WeixinQrCodeReq.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeReq.ActionInfo.builder()
                        .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                .scene_id(100601)
                                .build())
                        .build())
                .build();

        Call<WeixinQrCodeRes> call = weixinApiService.createQrCode(token, weixinQrCodeReq);
        WeixinQrCodeRes weixinQrCodeRes = call.execute().body();
        assert null != weixinQrCodeRes;
//        Ticket.put("Ticket",weixinQrCodeRes.getTicket());
        return weixinQrCodeRes.getTicket();
    }

    @Override
    public String checkLogin(String ticket) {
//        openId.getIfPresent(ticket);
        //注意是通过appid获得
        return openidToken.getIfPresent(ticket);

    }

    @Override
    public void saveLoginState(String ticket, String openid) throws IOException {
        //步骤

        //当微信返回登录成功的消息后,会返回一个openId
        //1. 存入openId
        openidToken.put(ticket,openid);


        //2. 获取accessToken
        String token = weixinAccessToken.getIfPresent(appid);
        if(null == token)
        {
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            //注意这个call的结构,call主要是一个包下面的
            WeixinTokenRes weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            token = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid,token);
        }
        //3. 发送模版信息
        // 2. 发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageVO.put(data, WeixinTemplateMessageVO.TemplateKey.USER, openid);

        WeixinTemplateMessageVO templateMessageDTO = new WeixinTemplateMessageVO(openid, template_id);
        templateMessageDTO.setUrl("https://gaga.plus");
        templateMessageDTO.setData(data);

        Call<Void> call = weixinApiService.sendMessage(token, templateMessageDTO);
        
        call.execute();


    }
}
