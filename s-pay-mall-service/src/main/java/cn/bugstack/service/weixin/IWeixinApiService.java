package cn.bugstack.service.weixin;

import cn.bugstack.domain.po.WeixinTemplateMessageVO;
import cn.bugstack.domain.req.WeixinQrCodeReq;
import cn.bugstack.domain.res.WeixinQrCodeRes;
import cn.bugstack.domain.res.WeixinTokenRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-11-29 20:09
 */
public interface IWeixinApiService {
   // https请求方式: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
   @GET("cgi-bin/token")
   Call<WeixinTokenRes> getToken(@Query("grant_type") String grantType,
                                 @Query("appid") String appId,
                                 @Query("secret") String appSecret);

   /**
    * 获取凭据 ticket
    * 文档：<a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">Generating_a_Parametric_QR_Code</a>
    * <a href="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET">前端根据凭证展示二维码</a>
    *
    * @param accessToken            getToken 获取的 token 信息
    * @param weixinQrCodeReq 入参对象
    * @return 应答结果
    */
   @POST("cgi-bin/qrcode/create")
   Call<WeixinQrCodeRes> createQrCode(@Query("access_token") String accessToken, @Body WeixinQrCodeReq weixinQrCodeReq);

   @POST("cgi-bin/message/template/send")
   Call<Void> sendMessage(@Query("access_token") String accessToken, @Body WeixinTemplateMessageVO weixinTemplateMessageVO);
}
