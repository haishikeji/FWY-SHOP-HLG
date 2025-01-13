package com.px.business.manage.statistics.service;

import com.px.business.manage.statistics.vo.DayTotalVO;
import com.px.business.manage.statistics.vo.OrderLineChartVO;
import com.px.business.manage.statistics.vo.SalesLineChartVO;
import com.px.business.manage.statistics.vo.TotalStatisVO;
import com.px.common.result.Result;

/**
 *
 * @author 品讯科技
 * @date 2024-08
 * @return
 * @version 1.0.0
 */

public interface StatisticsService {


    public Result<TotalStatisVO> total();

    public Result<SalesLineChartVO> salesLineChart();

    public Result<OrderLineChartVO> orderLineChart();

    public Result<DayTotalVO> dayTotal();



}
