package cn.bugstack.dao;

import cn.bugstack.domain.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

/*
 *@auther:yangzihe @洋纸盒
 *@discription:
 *@create 2024-11-30 16:48
 */
@Mapper
public interface IOrderDao {
    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);
}
