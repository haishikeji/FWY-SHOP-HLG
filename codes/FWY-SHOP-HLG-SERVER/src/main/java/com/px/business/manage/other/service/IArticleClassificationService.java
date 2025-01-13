package com.px.business.manage.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.business.manage.other.dto.ArticleClassificationDto;
import com.px.business.manage.other.entity.ArticleClassification;
import com.px.common.result.Result;
import com.px.sysuser.vo.SysUserDetailsVo;

/**
 * @@author 品讯科技
 * @Description: 文章分类 服务类
 * @Date: 2021-03-06
 **/
public interface IArticleClassificationService extends IService<ArticleClassification> {

    /**
     * 新增文章分类
     *
     * @param user
     * @param dto
     * @return
     */
    Result add(SysUserDetailsVo user, ArticleClassificationDto dto);


    /**
     * 修改文章分类
     *
     * @param user
     * @param dto
     * @return
     */
    Result update(SysUserDetailsVo user, ArticleClassificationDto dto);


    /**
     * 类型名称是否存在
     *
     * @param name
     * @return
     */
    Boolean isExistedTypeName(String name);


}
