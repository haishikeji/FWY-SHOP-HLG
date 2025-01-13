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
 * @Date: 2021-03-04
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DataField", description = "数据字典表")
@TableName("tb_data_field")
public class DataField extends BaseEntity {

    @ApiModelProperty(value = "创建人id")
    @TableField("create_id")
    private String createId;

    @ApiModelProperty(value = "更新人id")
    @TableField("update_id")
    private String updateId;

    @ApiModelProperty(value = "字段名称")
    @TableField("field_name")
    private String fieldName;

    @ApiModelProperty(value = "编码")
    @TableField("field_code")
    private String fieldCode;

    @ApiModelProperty(value = "说明")
    @TableField("field_value")
    private String fieldValue;

    @ApiModelProperty(value = "0-顶级 1-子级")
    @TableField("level")
    private Integer level;

    @ApiModelProperty(value = "父级code 没有默认为0")
    @TableField("parent_code")
    private String parentCode;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "删除状态 0- 正常 1-已删除")
    @TableField("deleted")
    private Integer deleted;


}