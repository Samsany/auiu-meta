package com.auiucloud.component.oss.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.component.oss.domain.SysAttachment;
import com.auiucloud.component.oss.domain.SysAttachmentGroup;
import com.auiucloud.component.oss.mapper.SysAttachmentMapper;
import com.auiucloud.component.oss.service.ISysAttachmentGroupService;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.utils.FileUtil;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.oss.core.OssTemplate;
import com.auiucloud.core.oss.props.OssProperties;
import com.auiucloud.core.web.utils.OssUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    private final ISysAttachmentGroupService sysAttachmentGroupService;

    /**
     * 分页查询附件
     *
     * @param search     参数
     * @param attachment 参数
     * @return PageUtils
     */
    @Override
    public PageUtils listPage(Search search, SysAttachment attachment) {
        LambdaQueryWrapper<SysAttachment> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysAttachment::getFileType, attachment.getFileType());
        // 按关键词查询，查询名称
        boolean isKeyword = StringUtil.isNotBlank(search.getKeyword());
        queryWrapper.like(isKeyword, SysAttachment::getName, search.getKeyword());
        if (attachment.getAttachmentGroupId() != CommonConstant.ROOT_NODE_ID.intValue()) {
            queryWrapper.eq(SysAttachment::getAttachmentGroupId, attachment.getAttachmentGroupId());
        }
        queryWrapper.orderByDesc(SysAttachment::getCreateTime);
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
    public Map<String, Object> upload(MultipartFile file) {
        return this.upload(file, 0L, null);
    }

    @Transactional
    @Override
    public Map<String, Object> upload(MultipartFile file, Long groupId) {
        return this.upload(file, groupId, null);
    }

    /**
     * @param file     文件
     * @param groupId  文件分组 默认 0(根目录)
     * @param filename 自定义上传文件名
     * @return Map<String, Object>
     */
    @Transactional
    @Override
    public Map<String, Object> upload(MultipartFile file, Long groupId, String filename) {
        OssProperties ossProperties = getOssProperties();
        SysAttachmentGroup group = sysAttachmentGroupService.getUploadGroupById(groupId);
        if (StrUtil.isBlank(filename)) {
            filename = UUID.randomUUID().toString().replace("-", "")
                    + StringPool.DOT + FilenameUtils.getExtension(file.getOriginalFilename());
        }

        String bizPath = group.getBizPath();
        String filePath = StrUtil.isNotBlank(bizPath) ? bizPath.concat(StringPool.SLASH + filename) : filename;
        Map<String, Object> uMap = new HashMap<>();
        try {
            //上传文件
            assert ossProperties != null;
            ossTemplate.putObject(ossProperties.getBucketName(), filePath, file.getInputStream(), file.getSize(), file.getContentType());

            //生成URL
            String url = ossProperties.getCustomDomain() + StringPool.SLASH + filePath;

            //自定义返回报文
            uMap.put("bucketName", ossProperties.getBucketName());
            uMap.put("fileName", filename);
            uMap.put("url", url);
            // 文件大小 单位kb
            uMap.put("size", file.getSize() / 1024);
            if (FileUtil.getFileType(FileUtil.getExtensionName(filename)).equals("pic")) {
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                uMap.put("width", bufferedImage.getWidth());
                uMap.put("height", bufferedImage.getHeight());
            }
            //上传成功后记录入库
            this.attachmentLog(file, url, groupId, filename);
        } catch (Exception e) {
            log.error("上传失败", e);
            throw new ApiException(e.getMessage());
        }
        return uMap;
    }

    @Override
    public boolean checkAttachmentGroupHasChild(Long attachmentGroupId) {
        LambdaQueryWrapper<SysAttachment> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysAttachment::getAttachmentGroupId, attachmentGroupId);
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSysAttachmentById(Long id) {
        // 删除文件
        // OssProperties ossProperties = getOssProperties();
        // SysAttachment sysAttachment = this.getById(id);
        // String filePath = sysAttachment.getFileName();
        // Long groupId = sysAttachment.getAttachmentGroupId();
        // if (groupId != null && groupId.equals(CommonConstant.ROOT_NODE_ID)) {
        //     SysAttachmentGroup group = sysAttachmentGroupService.getById(groupId);
        //     filePath = group.getBizPath().concat(filePath);
        // }
        // ossTemplate.removeObject(ossProperties.getBucketName(), filePath);
        return this.removeById(id);
    }

    /**
     * 将上传成功的文件记录入库
     *
     * @param file     　文件
     * @param url      　返回的URL
     * @param groupId  附件分组
     * @param filename 新文件名
     * @return boolean
     */
    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public boolean attachmentLog(MultipartFile file, String url, Long groupId, String filename) {
        SysAttachment sysAttachment = new SysAttachment();

        String original = file.getOriginalFilename();

        sysAttachment.setAttachmentGroupId(groupId);
        sysAttachment.setName(FilenameUtils.getName(original));
        sysAttachment.setUrl(url);
        sysAttachment.setFileName(filename);
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




