package com.auiucloud.component.oss.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.component.oss.mapper.SysAttachmentMapper;
import com.auiucloud.component.oss.service.ISysAttachmentGroupService;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.component.sysconfig.domain.SysAttachment;
import com.auiucloud.component.sysconfig.domain.SysAttachmentGroup;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.FileEnums;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.utils.FileUtil;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.common.utils.ThumbUtil;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.core.oss.core.OssTemplate;
import com.auiucloud.core.oss.props.OssProperties;
import com.auiucloud.core.web.utils.OssUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author dries
 * @description 针对表【sys_attachment(附件表)】的数据库操作Service实现
 * @createDate 2023-03-14 12:47:40
 */
@Slf4j
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
        queryWrapper.orderByDesc(SysAttachment::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public List<SysAttachment> selectAttachmentListByName(List<String> names) {
        if (CollUtil.isNotEmpty(names)) {
            LambdaQueryWrapper<SysAttachment> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(SysAttachment::getFileName, names);
            return this.list(queryWrapper);
        }

        return Collections.emptyList();

    }

    /**
     * 附件上传
     *
     * @param file 上传文件
     * @return Map<String, Object>
     */
    @Transactional
    @Override
    public Map<String, Object> upload(MultipartFile file) {
        return this.upload(file, 0L, null, false, false);
    }

    @Transactional
    @Override
    public Map<String, Object> upload(MultipartFile file, Long groupId) {
        return this.upload(file, groupId, null, false, false);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> upload(MultipartFile file, Long groupId, boolean thumb, boolean checkImg) {
        return this.upload(file, groupId, null, thumb, checkImg);
    }

    /**
     * @param file     文件
     * @param groupId  文件分组 默认 0(根目录)
     * @param filename 自定义上传文件名
     * @param thumb    是否压缩原图
     * @param checkImg 是否进行图片内容安全检测
     * @return Map<String, Object>
     */
    @Transactional
    @Override
    public Map<String, Object> upload(MultipartFile file, Long groupId, String filename, boolean thumb, boolean checkImg) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileType = FileUtil.getFileType(extension);
        try {
            if (checkImg) {
                if (!fileType.equals("pic")) {
                    throw new ApiException("文件类型不匹配");
                }
                // 图片内容安全检测
                uploadImageCheck(file);
            }

            OssProperties ossProperties = getOssProperties();
            SysAttachmentGroup group = sysAttachmentGroupService.getUploadGroupById(groupId);

            if (StrUtil.isBlank(filename)) {
                filename = UUID.randomUUID().toString().replace("-", "")
                        + StringPool.DOT + extension;
            }

            String bizPath = group.getBizPath();
            String filePath = StrUtil.isNotBlank(bizPath) ? bizPath.concat(StringPool.SLASH + filename) : filename;
            Map<String, Object> uMap = new HashMap<>();
            // 上传文件
            assert ossProperties != null;
            ossTemplate.putObject(ossProperties.getBucketName(), filePath, file.getInputStream(), file.getSize(), file.getContentType());
            String url = ossProperties.getCustomDomain() + StringPool.SLASH + filePath;
            // 自定义返回报文
            uMap.put("bucketName", ossProperties.getBucketName());
            uMap.put("fileName", filename);
            uMap.put("url", url);
            // 文件大小 单位kb
            uMap.put("size", file.getSize() / 1024);

            String thumbUrl = null;
            // 上传缩略图
            if (thumb) {
                String thumbFileName = FileUtil.getFileNameNoEx(filename) + StringPool.UNDERSCORE + "thumb" + StringPool.DOT + extension;
                String thumbFilePath = "thumb" + StringPool.SLASH + thumbFileName;
                byte[] thumbBytes = ThumbUtil.compressPicForScale(file.getBytes(), 500);
                ossTemplate.putObject(ossProperties.getBucketName(), thumbFilePath, thumbBytes, file.getContentType());
                thumbUrl = ossProperties.getCustomDomain() + StringPool.SLASH + thumbFilePath;
                uMap.put("thumbUrl", thumbUrl);
            }

            Integer width = null;
            Integer height = null;
            if (FileUtil.getFileType(FileUtil.getExtensionName(filename)).equals("pic")) {
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                width = bufferedImage.getWidth();
                height = bufferedImage.getHeight();
                uMap.put("width", width);
                uMap.put("height", height);
            }
            // 上传成功后记录入库
            this.attachmentLog(file, url, thumbUrl, groupId, filename, width, height);
            return uMap;
        } catch (Exception e) {
            log.error("上传失败", e);
            throw new ApiException(e.getMessage());
        }
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
     * @param thumbUrl 　返回的缩略图URL
     * @param groupId  附件分组
     * @param filename 新文件名
     * @return boolean
     */
    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public boolean attachmentLog(MultipartFile file, String url, String thumbUrl, Long groupId, String filename) {
        return this.attachmentLog(file, url, thumbUrl, groupId, filename, null, null);
    }

    /**
     * 将上传成功的文件记录入库
     *
     * @param file     　文件
     * @param url      　返回的URL
     * @param thumbUrl 　返回的缩略图URL
     * @param groupId  附件分组
     * @param filename 新文件名
     * @return boolean
     */
    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public boolean attachmentLog(MultipartFile file, String url, String thumbUrl, Long groupId, String filename, Integer width, Integer height) {
        SysAttachment sysAttachment = new SysAttachment();
        String original = file.getOriginalFilename();

        sysAttachment.setAttachmentGroupId(groupId);
        sysAttachment.setName(FilenameUtils.getName(original));
        sysAttachment.setUrl(url);
        // 缩略图
        sysAttachment.setThumbUrl(thumbUrl);
        sysAttachment.setFileName(filename);
        sysAttachment.setSize(file.getSize());
        sysAttachment.setWidth(width);
        sysAttachment.setHeight(height);
        sysAttachment.setFileType(OssUtil.getFileType(original));
        return this.save(sysAttachment);
    }

    /**
     * 图片安全检测
     *
     * @param file 上传文件
     */
    private void uploadImageCheck(MultipartFile file) {
        byte[] bytes;
        try {
            bytes = ThumbUtil.compressPicForScale(file.getBytes(), 50);
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
        String base64Img = Base64Encoder.encode(bytes);
        log.info("图片长度：{}", base64Img.length());
        String appId = RequestHolder.getHttpServletRequestHeader("appId");
        Integer loginType = SecurityUtil.getUser().getLoginType();
        // 抖音内容安全检测
        if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
            DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
            String result = douyinAppletService.checkImageData(base64Img);
            if (StrUtil.isNotBlank(result)) {
                log.error("内容安全检测: {}", result);
                // return ApiResult.fail("图片违规,请重新上传");
                throw new ApiException(ResultCode.USER_ERROR_A0432);
            }
        }
    }

    @Override
    public void asyncAttachmentWidth2Height() {
        // 同步附件文件宽高
        List<SysAttachment> list = this.list(Wrappers.<SysAttachment>lambdaQuery().eq(SysAttachment::getFileType, FileEnums.FileTypeEnum.PIC.getValue()));
        for (SysAttachment sysAttachment : list) {
            try {
                String url = sysAttachment.getUrl();
                InputStream inputStream = new URL(url).openStream();
                BufferedImage read = ImageIO.read(inputStream);
                int width = read.getWidth();
                int height = read.getHeight();

                sysAttachment.setWidth(width);
                sysAttachment.setHeight(height);
                this.updateById(sysAttachment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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




