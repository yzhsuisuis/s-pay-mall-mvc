package cn.bugstack.service;

import cn.bugstack.domain.req.ShopCartReq;
import cn.bugstack.domain.res.PayOrderRes;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 订单服务接口
 * @create 2024-09-29 09:43
 */
public interface IOrderService {

    PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception;

    void changeOrderPaySuccess(String orderId);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrder();

    boolean changeOrderClose(String orderId);

//    void updateNoPayNotifyOrder(String orderId);
}
