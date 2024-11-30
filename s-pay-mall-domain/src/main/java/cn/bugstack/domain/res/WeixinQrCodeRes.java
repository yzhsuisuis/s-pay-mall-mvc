package cn.bugstack.domain.res;

import lombok.Data;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 获取微信登录二维码响应对象
 * @create 2024-09-28 13:36
 */
@Data
public class WeixinQrCodeRes {

    private String ticket;
    private Long expire_seconds;
    private String url;

}
