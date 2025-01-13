package com.px.business.app.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.business.app.order.dto.ExamineDTO;
import com.px.business.app.order.dto.RefundDTO;
import com.px.business.app.order.dto.SaleOrderDTO;
import com.px.business.app.order.entity.SaleOrder;
import com.px.business.app.order.vo.AppSaleOrderInfoVO;
import com.px.business.app.order.vo.ManagerSaleVO;
import com.px.business.app.order.vo.SaleOrderInfoVO;
import com.px.business.app.order.vo.SaleOrderVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 品讯科技
 * @since 2021-10-13
 */
public interface SaleOrderService extends IService<SaleOrder> {

    Boolean addSaleOrder(SaleOrderDTO dto);

    Page<SaleOrderVO> getSaleOrderList(String userId, Page<SaleOrderVO> page1);

    Page<ManagerSaleVO> getManagerSaleOrder(Integer current, Integer size, String orderNo, String userName, Integer state);

    SaleOrderInfoVO getInfo(String id);

    Boolean examine(ExamineDTO dto);

    Boolean sureRefund(RefundDTO dto);

    AppSaleOrderInfoVO getSaleOrderById(String id);

    SaleOrderVO getSale(String id);

    Boolean refund(String id);

    /**
     * 获取售后订单
     *
     * @param orderId orderId
     * @return order
     */
    SaleOrder getAfterOrder(String orderId);
}
