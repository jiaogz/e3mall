package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_CART_NAME}")
    private String REDIS_CART_NAME;
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 向redis中插入购物车列表
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        //查询redis是否存在改商品列表
        TbItem tbitem = getTbitemByRedis(userId, itemId);
        //存在修改数量
        if (tbitem !=null ) {
            tbitem.setNum(tbitem.getNum()+num);
            jedisClient.hset(REDIS_CART_NAME + ":" + userId, itemId + "",JsonUtils.objectToJson(tbitem));
            return E3Result.ok();
        }
        //不存在根据商品ID查询商品信息，写入redis
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        tbItem.setNum(num);
        String image = StringUtils.isNotBlank(tbItem.getImage()) ? tbItem.getImage().split(",")[0] : null;
        tbItem.setImage(image);
        jedisClient.hset(REDIS_CART_NAME + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    /**
     * 合并购物车列表
     * @param userId
     * @param cartList
     * @return
     */
    @Override
    public E3Result margeCartList(long userId, List<TbItem> cartList) {
        //遍历cookie中的购物车列表
        for (TbItem tbItem : cartList) {
            addCart(userId,tbItem.getId(),tbItem.getNum());
        }
        return E3Result.ok();
    }

    /**
     * 查询购物车列表
     * @param userId
     * @return
     */
    @Override
    public List<TbItem> showCartList(long userId) {
        List<String> jsonList = jedisClient.hvals(REDIS_CART_NAME + ":" + userId);
        List<TbItem> cartList = new ArrayList<>();
        for (String json : jsonList) {
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            cartList.add(tbItem);
        }
        return cartList;
    }

    /**
     * 更新购物车商品数量
     * @param userId
     * @param num
     * @return
     */
    @Override
    public E3Result updateCartNum(long userId, long itemId,int num) {
        TbItem tbItem = getTbitemByRedis(userId, itemId);
        tbItem.setNum(num);
        jedisClient.hset(REDIS_CART_NAME + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    /**
     * 删除购物车商品信息
     * @param userId
     * @param ItemId
     * @return
     */
    @Override
    public E3Result deletCart(long userId, long ItemId) {
        jedisClient.hdel(REDIS_CART_NAME + ":" + userId);
        return E3Result.ok();
    }

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    @Override
    public E3Result cleanCartAll(long userId) {
        jedisClient.del(REDIS_CART_NAME + ":" + userId);
        return E3Result.ok();
    }

    /**
     * 从redis中获取购物车商品信息
     * @param userId
     * @param itemId
     * @return
     */
    private TbItem getTbitemByRedis(long userId,long itemId) {
        Boolean hexists = jedisClient.hexists(REDIS_CART_NAME + ":" + userId, itemId + "");
        //存在修改数量
        if (hexists) {
            String json = jedisClient.hget(REDIS_CART_NAME + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            return tbItem;
        }
        return null;
    }
}
