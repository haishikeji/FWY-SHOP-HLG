package com.px.business.manage.feishu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChatDTO {
    @ApiModelProperty("群头像url")
    private String avatar;

    @JsonProperty("chat_id")
    @ApiModelProperty("群组id")
    private String chatId;

    @ApiModelProperty("群描述")
    private String description;

    private boolean external;

    @ApiModelProperty("群名称")
    private String name;

    @ApiModelProperty("群主id")
    @JsonProperty("owner_id")
    private String ownerId;

    @ApiModelProperty("群主id类型")
    @JsonProperty("owner_id_type")
    private String ownerIdType;


    @JsonProperty("tenant_key")
    private String tenantKey;
}
