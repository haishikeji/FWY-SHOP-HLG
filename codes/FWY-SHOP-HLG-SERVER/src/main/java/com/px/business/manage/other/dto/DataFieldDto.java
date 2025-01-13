package com.px.business.manage.other.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: DataFieldDTO
 * @Date: 2021-03-04
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "数据字典表Dto", description = "数据字典表Dto")
public class DataFieldDto implements Serializable {

    @ApiModelProperty(value = "字典id-修改必传")
    private String fieldId;

    @ApiModelProperty(value = "字段名称", required = true)
    @NotBlank(message = "字段名称不能为空")
    private String fieldName;

    @ApiModelProperty(value = "编码", required = true)
    @NotBlank(message = "编码不能为空")
    private String fieldCode;

    @ApiModelProperty(value = "说明")
    private String fieldValue;

    @ApiModelProperty(value = "0-顶级 1-子级")
    private Integer level;

    @ApiModelProperty(value = "父级code 没有默认为0")
    private String parentCode;

    @ApiModelProperty(value = "备注")
    private String remark;
}