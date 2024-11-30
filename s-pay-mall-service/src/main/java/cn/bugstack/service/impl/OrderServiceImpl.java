package cn.bugstack.service.impl;

import cn.bugstack.common.constants.Constants;
import cn.bugstack.dao.IOrderDao;
import cn.bugstack.domain.po.PayOrder;
import cn.bugstack.domain.req.ShopCartReq;
import cn.bugstack.domain.res.PayOrderRes;
import cn.bugstack.domain.vo.ProductVO;
import cn.bugstack.service.IOrderService;
import cn.bugstack.service.rpc.ProductRPC;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-11-30 16:41
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    
    @Resource
    private ProductRPC productRPC;
    @Override
    public PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception {
        //1. 查询是否有相同订单,如果有再分情况判断

        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setUserId(shopCartReq.getUserId());
        payOrderReq.setProductId(shopCartReq.getProductId());
        //这里其实非常的狗,因为这里只会查询得到1个订单账户

        PayOrder unpaidOrder = orderDao.queryUnPayOrder(payOrderReq);

        if(null != unpaidOrder && Constants.OrderStatusEnum.PAY_WAIT.getCode().equals(unpaidOrder.getStatus()))
        {
            //查询得到未支付的订单
            log.info("创建订单 - 存在未支付订单 userId :{} productId :{} orderId :{}",shopCartReq.getUserId(),shopCartReq.getProductId(),unpaidOrder.getOrderId());
            return PayOrderRes.builder()
                    .orderId(unpaidOrder.getOrderId())
                    .payUrl(unpaidOrder.getPayUrl())
                    .build();
        }else if(null != unpaidOrder && Constants.OrderStatusEnum.CREATE.getCode().equals(unpaidOrder.getStatus()))
        {
            //todo 调用微信支付
        }

        //2. 查询商品并插入订单
        ProductVO productVO = productRPC.queryProductByProductId(shopCartReq.getProductId());
        String orderId = RandomStringUtils.randomNumeric(16);
        orderDao.insert(PayOrder.builder()
                .userId(shopCartReq.getUserId())
                .productId(shopCartReq.getProductId())
                .productName(productVO.getProductName())
                .orderId(orderId)
                .totalAmount(productVO.getPrice())
                .orderTime(new Date())
                .status(Constants.OrderStatusEnum.CREATE.getCode())
                .build());
        //3. todo 支付订单



        return PayOrderRes.builder()
                .orderId(orderId)
                .payUrl("暂无")
                .build();



    }
}
