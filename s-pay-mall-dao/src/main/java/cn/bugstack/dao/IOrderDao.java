package cn.bugstack.dao;

import cn.bugstack.domain.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-11-30 16:48
 */
@Mapper
public interface IOrderDao {
    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);

    void updateOrderPayInfo(PayOrder payOrder);

    void changeOrderPaySuccess(PayOrder payOrderReq);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrder();

    boolean changeOrderClose(String orderId);
}
