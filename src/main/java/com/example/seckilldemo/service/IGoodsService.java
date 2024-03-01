package com.example.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckilldemo.pojo.Goods;
import com.example.seckilldemo.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2024-01-05
 */
public interface IGoodsService extends IService<Goods> {

   // List<GoodsVo> findGoodsVo();
   List<GoodsVo> findGoodsVo();

   GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
