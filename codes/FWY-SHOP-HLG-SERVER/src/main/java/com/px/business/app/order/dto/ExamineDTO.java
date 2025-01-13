package com.px.business.app.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("审核对象")
public class ExamineDTO {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("审核状态 1-未通过  3-通过")
    private Integer state;

    @ApiModelProperty("备注")
    private String note;
}
