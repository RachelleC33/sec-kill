package com.example.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckilldemo.pojo.Order;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.OrderDeatilVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2024-01-05
 */
public interface IOrderService extends IService<Order> {

    Order secKill(User user, GoodsVo goodsVo);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDeatilVo detail(Long orderId);
}
