package com.px.business.manage.other.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.px.common.api.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @@author 品讯科技
 * @Description: entity
 * @Date: 2021-02-24
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Banner", description = "banner表")
@TableName("tb_banner")
public class Banner extends BaseEntity {

    @ApiModelProperty(value = "banner标题")
    @TableField("banner_title")
    private String bannerTitle;

    @ApiModelProperty(value = "banner图片")
    @TableField("banner_picture")
    private String bannerPicture;

    @ApiModelProperty(value = "位置")
    @TableField("position")
    private String position;

    @ApiModelProperty(value = "是否有链接的标识：0-有 1-无")
    @TableField("link_identifier")
    private Integer linkIdentifier;

    @ApiModelProperty(value = "链接类型")
    @TableField("link_type")
    private String linkType;

    @ApiModelProperty(value = "状态 0：正常 1：禁用")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value = "链接地址")
    @TableField("link_url")
    private String linkUrl;

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "创建者id")
    @TableField("create_id")
    private String createId;

    @ApiModelProperty(value = "修改用户id")
    @TableField("update_id")
    private String updateId;


}