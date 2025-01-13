package com.px.sysuser.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @@author 品讯科技
 * @Description: vo
 * @Date: 2020-05-29
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoleMenuVo implements Serializable {

    private String roleId;

    private String menuId;


}