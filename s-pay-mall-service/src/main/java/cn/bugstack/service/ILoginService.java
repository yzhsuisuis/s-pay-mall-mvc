package cn.bugstack.service;

import java.io.IOException;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-11-29 20:12
 */
public interface ILoginService {

    String createQrCodeTicket() throws IOException;

    String checkLogin(String ticket);

    void saveLoginState(String ticket, String openid) throws IOException;
}
