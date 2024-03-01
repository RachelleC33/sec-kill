package com.example.seckilldemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckilldemo.pojo.Order;
import com.example.seckilldemo.pojo.SeckillOrder;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.service.IGoodsService;
import com.example.seckilldemo.service.IOrderService;
import com.example.seckilldemo.service.ISeckillOrderService;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.RespBean;
import com.example.seckilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/seckill")
public class SeKillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * windows 优化前QPS : 785.9
     * Linux 优化前QPS :  170
     * windows 缓存（页面静态化，页面缓存，对象缓存）QPS : 1356
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId); //判断库存
//        if (goods.getStockCount() < 1) {
//            //model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
//        //判断是否重复抢购
////        SeckillOrder seckillOrder = seckillOrderService.getOne(new
////                QueryWrapper<SeckillOrder>().eq("user_id",
////                user.getId()).eq(
////                "goods_id",
////                goodsId));
//
//        //判断是否重复抢购
//        SeckillOrder tSeckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
//        if (tSeckillOrder != null) {
//            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
//        }
////        if (seckillOrder != null) {
////            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
////        }
//        Order order = orderService.secKill(user, goods);
//        return RespBean.success(order);

        //优化后代码
        ValueOperations valueOperations = redisTemplate.opsForValue();
//        boolean check = orderService.checkPath(user, goodsId, path);
//        if (!check) {
//            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
//        }

        //判断是否重复抢购
        SeckillOrder tSeckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (tSeckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记，减少Redis的访问
//        if (EmptyStockMap.get(goodsId)) {
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
        //预减库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (stock < 0) {
//            EmptyStockMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        Order order = orderService.secKill(user, goods);
        return RespBean.success(order);
//        SeckillMessage seckillMessag = new SeckillMessage(user, goodsId);
//        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessag));
 //       return RespBean.success(0);


    }














@RequestMapping( "/doSeckill2")
    public String doSeckill2(Model model, User user, Long goodsId){
        if (user == null) {
            return "login";
        }


        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
           // return RespBean.error(RespBeanEnum.EMPTY_STOCK);
            return "secKillFail";
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        //TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            //return RespBean.error(RespBeanEnum.REPEATE_ERROR);
            return "secKillFail";
        }
        Order order = orderService.secKill(user, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
       // return RespBean.success(tOrder);
        return "orderDetail";

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
           // EmptyStockMap.put(goodsVo.getId(), false);
        });

    }
}
