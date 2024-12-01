package cn.bugstack.job;

import cn.bugstack.service.IOrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-12-01 21:29
 */
@Slf4j
@Component
public class NoPayNotifyOrderJob {

    @Resource
    private IOrderService orderService;
    @Resource
    private AlipayClient alipayClient;

    @Scheduled(cron = "0/3 * * * * ?")
    public void exec()
    {
        try {
            log.info("任务；检测未接收到或未正确处理的支付回调通知");
            List<String> orderIds = orderService.queryNoPayNotifyOrder();

            if(orderIds == null || orderIds.isEmpty()) return;
            for (String orderId : orderIds) {
//                AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//                AlipayTradePayModel bizmodel = new AlipayTradePayModel();
//                bizmodel.setOutTradeNo(orderId);
//                request.setBizModel(bizmodel);
//                AlipayTradePagePayResponse alipayTradePagePayResponse = alipayClient.execute(request);
//                String code = alipayTradePagePayResponse.getCode();
                //这里是根据单号,去查询哪些本地数据库显示
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
                AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
                bizModel.setOutTradeNo(orderId);
                request.setBizModel(bizModel);

                AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(request);
                String code = alipayTradeQueryResponse.getCode();

                if("1000".equals(code))
                {
                    //订单确实已经支付完了,但是本地的订单却没有完成,南无现在就是直接更新订单
                    orderService.changeOrderPaySuccess(orderId);

                }

            }
        } catch (Exception e) {
            log.error("检测未接收到或未正确处理的支付回调通知失败", e);
        }


    }
}
