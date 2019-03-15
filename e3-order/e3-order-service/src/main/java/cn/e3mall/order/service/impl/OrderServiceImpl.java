package cn.e3mall.order.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_START_VAL}")
    private String ORDER_ID_START_VAL;
    @Value("${ORDER_ITEM_ID_KEY}")
    private String ORDER_ITEM_ID_KEY;

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private JedisClient jedisClient;


    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    @Override
    public E3Result creatOrder(OrderInfo orderInfo) {
        //生成订单ID，借用redis自增
        if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
            jedisClient.set(ORDER_ID_GEN_KEY,ORDER_ID_START_VAL);
        }
        String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();

        //向数据库插入订单信息、订单商品信息、订单物流信息
        orderInfo.setOrderId(orderId);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderMapper.insert(orderInfo);

        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            orderItem.setId(jedisClient.incr(ORDER_ITEM_ID_KEY).toString());
            orderItem.setItemId(orderId);
            orderItemMapper.insert(orderItem);
        }

        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);

        return E3Result.ok(orderId);
    }
}
