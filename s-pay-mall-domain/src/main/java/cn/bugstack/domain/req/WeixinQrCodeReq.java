package cn.bugstack.domain.req;

import lombok.*;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-11-29 20:32
 */

/*http请求方式: POST URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
 POST数据格式：json POST数据例子：
 {"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}} */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeixinQrCodeReq {

   private int expire_seconds;

   private String action_name;

   private ActionInfo action_info;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActionInfo{

        Scene scene;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Scene{
            int scene_id;
            String scene_str;
        }


    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ActionNameTypeVO {
        QR_SCENE("QR_SCENE", "临时的整型参数值"),
        QR_STR_SCENE("QR_STR_SCENE", "临时的字符串参数值"),
        QR_LIMIT_SCENE("QR_LIMIT_SCENE", "永久的整型参数值"),
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE", "永久的字符串参数值");

        private String code;
        private String info;
    }

}
