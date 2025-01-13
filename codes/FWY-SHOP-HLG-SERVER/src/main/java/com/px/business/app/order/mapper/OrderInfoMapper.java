package com.px.business.app.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.business.app.order.dto.OrderInfoDTO;
import com.px.business.app.order.entity.OrderInfo;

import java.util.List;

/**
 * <p>
 * 普通商品订单 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2021-03-30
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    OrderInfoDTO selectOrderInfo(String orderInfoId);

    List<OrderInfoDTO> selectOrderList(Page<OrderInfoDTO> page, String keywords, String aboutId,Integer state,Integer deliveryMode);

    List<OrderInfoDTO> selectOrderListUser(Page<OrderInfoDTO> page, String keywords, String userId, Integer state);
}
