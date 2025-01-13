package com.px.business.manage.other.controller;


import com.px.common.exception.ErrorException;
import com.px.common.result.Result;
import com.px.common.utils.AliYunFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file")
@Api(value = "/file", tags = "文件上传")
@Slf4j
public class FileController {

    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    public Result uploadFile(@RequestParam MultipartFile multipartFile) {
        log.info(" uploadFile -- name : " + multipartFile.getOriginalFilename());
        String url = AliYunFileUtils.uploadFile(multipartFile);
        if (StringUtils.isBlank(url)) {
            throw new ErrorException(500, "上传文件失败");
        }
        return Result.success(url);
    }

}
