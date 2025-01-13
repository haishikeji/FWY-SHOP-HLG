package com.px.business.manage.feishu.model;

import lombok.Data;

import java.util.List;

@Data
public class MsgDTO {


    private ContentDTO content;

    private List<String> receiveId;
}
