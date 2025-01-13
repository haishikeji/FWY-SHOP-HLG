package com.px.business.app.appuser.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 商品浏览记录
 * <p>
 *  ::本代码由[兮家小二]提供的代码生成器生成,如有问题,请手动修改 ::作者CSDN:https://blog.csdn.net/qq_41463655
 * </p>
 * @author 品讯科技
 * @email zly@hnpinxun.cn
 * @date 2024-08
 */
@Data
@ToString(callSuper = true)
@TableName("tb_commodity_browsing_history")
@ApiModel(value = "CommodityBrowsingHistory 对象", description = "商品浏览记录")
public class CommodityBrowsingHistory extends BaseEntity {

    private static final long serialVersionUID = -539312190377824274L;

    /**
     * 小程序用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 浏览商品id
     */
    @TableField(value = "commodity_id")
    private String commodityId;

    /**
     * 商品名称
     */
    @TableField(value = "commodity_name")
    private String commodityName;
    /**
     * 商品名称
     */
    @TableField(value = "cover_url")
    private String coverUrl;

    /**
     * 商品限量
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 价格
     */
    @TableField(value = "sell_price")
    private BigDecimal sellPrice;

    /**
     * 原价
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 商品类型：1-普通商品 2-秒杀商品 3-预售商品 4-拼团商品
     */
    @TableField(value = "commodity_type")
    private Integer commodityType;


    @TableField(value = "shop_type")
    private Integer shopType;

}
