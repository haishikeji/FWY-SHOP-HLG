package com.px.business.app.appuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.app.appuser.entity.CommodityBrowsingHistory;

/**
 * 商品浏览记录
 * <p>
 *  ::本代码由[兮家小二]提供的代码生成器生成,如有问题,请手动修改 ::作者CSDN:https://blog.csdn.net/qq_41463655
 * </p>
 * @author 品讯科技
 * @email zly@hnpinxun.cn
 * @date 2024-08
 */
public interface CommodityBrowsingHistoryMapper extends BaseMapper<CommodityBrowsingHistory> {

    Page<CommodityBrowsingHistory> select(Page<CommodityBrowsingHistory> page, String userId);

}
