package com.auiucloud.component.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.component.domain.SysAttachment;
import com.auiucloud.component.mapper.SysAttachmentMapper;
import com.auiucloud.component.service.ISysAttachmentService;
import com.auiucloud.component.service.ISysConfigService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ComponentConstant;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.oss.core.OssTemplate;
import com.auiucloud.core.oss.props.OssProperties;
import com.auiucloud.core.redis.core.RedisService;
import com.auiucloud.core.web.utils.OssUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author dries
 * @description 针对表【sys_attachment(附件表)】的数据库操作Service实现
 * @createDate 2023-03-14 12:47:40
 */
@Service
@RequiredArgsConstructor
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment>
        implements ISysAttachmentService {

    private final OssTemplate ossTemplate;
    private final ISysConfigService configService;
    private final RedisService redisService;

    /**
     * 分页查询附件
     *
     * @param search 参数
     * @return PageUtils
     */
    @Override
    public PageUtils listPage(Search search) {
        LambdaQueryWrapper<SysAttachment> queryWrapper = Wrappers.lambdaQuery();

        // 按时间段查询
        queryWrapper.between(StringUtil.isNotBlank(search.getStartDate()), SysAttachment::getCreateTime, search.getStartDate(), search.getEndDate());

        // 按关键词查询，查询名称或者ID
        boolean isKeyword = StringUtil.isNotBlank(search.getKeyword());
        queryWrapper.like(isKeyword, SysAttachment::getName, search.getKeyword())
                .or(isKeyword)
                .like(isKeyword, SysAttachment::getId, search.getKeyword());
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    /**
     * 附件上传
     *
     * @param file 上传文件
     * @return ApiResult<?>
     */
    @Transactional
    @Override
    public ApiResult<?> upload(MultipartFile file) {
        OssProperties ossProperties = getOssProperties();
        String fileName = UUID.randomUUID().toString().replace("-", "")
                + StringPool.DOT + FilenameUtils.getExtension(file.getOriginalFilename());
        Map<String, String> uMap = new HashMap<>(4);
        try {
            //上传文件
            assert ossProperties != null;
            ossTemplate.putObject(ossProperties.getBucketName(), fileName, file.getInputStream(), file.getSize(), file.getContentType());

            //生成URL
            String url = ossProperties.getCustomDomain() + StringPool.SLASH + fileName;

            //自定义返回报文
            uMap.put("bucketName", ossProperties.getBucketName());
            uMap.put("fileName", fileName);
            uMap.put("url", url);
            //上传成功后记录入库
            this.attachmentLog(file, url, fileName);
        } catch (Exception e) {
            log.error("上传失败", e);
            return ApiResult.fail(e.getMessage());
        }
        return ApiResult.data(uMap);
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSysAttachmentById(Long id) {
        OssProperties ossProperties = getOssProperties();
        SysAttachment sysAttachment = this.getById(id);
        ossTemplate.removeObject(ossProperties.getBucketName(), sysAttachment.getFileName());
        return this.removeById(id);
    }

    /**
     * 将上传成功的文件记录入库
     *
     * @param file 　文件
     * @param url  　返回的URL
     * @return boolean
     */
    public boolean attachmentLog(MultipartFile file, String url, String fileName) {
        SysAttachment sysAttachment = new SysAttachment();
        String original = file.getOriginalFilename();
        String originalName = FilenameUtils.getName(original);
        sysAttachment.setName(originalName);
        sysAttachment.setUrl(url);
        sysAttachment.setFileName(fileName);
        sysAttachment.setSize(file.getSize());
        sysAttachment.setFileType(OssUtil.getFileType(original));
        return this.save(sysAttachment);
    }

    /**
     * 从redis里读取OssProperties
     *
     * @return OssProperties
     */
    public OssProperties getOssProperties() {
        return configService.getDefaultOssProperties();
    }
}




