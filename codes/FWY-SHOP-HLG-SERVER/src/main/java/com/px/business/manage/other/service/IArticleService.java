package com.px.business.manage.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.business.manage.appuser.vo.AppletUserInfoVo;
import com.px.business.manage.other.dto.ArticleDto;
import com.px.business.manage.other.entity.Article;
import com.px.business.manage.other.vo.ArticleVo;
import com.px.common.result.Result;
import com.px.sysuser.vo.SysUserDetailsVo;

/**
 * @@author 品讯科技
 * @Description: 文章表 服务类
 * @Date: 2021-03-06
 **/
public interface IArticleService extends IService<Article> {

    /**
     * 添加文章
     *
     * @param user
     * @param dto
     * @return
     */
    Result add(SysUserDetailsVo user, ArticleDto dto);


    /**
     * 编辑文章
     *
     * @param user
     * @param dto
     * @return
     */
    Result update(SysUserDetailsVo user, ArticleDto dto);


    /**
     * 查询文章详情
     *
     * @param articleId
     * @param user
     * @return
     */
    Result<ArticleVo> getAppDetails(String articleId, AppletUserInfoVo user);

}
