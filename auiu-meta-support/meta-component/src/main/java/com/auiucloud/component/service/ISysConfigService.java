package com.auiucloud.component.service;

import com.auiucloud.component.domain.SysConfig;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.oss.props.OssProperties;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sys_config(配置表)】的数据库操作Service
* @createDate 2023-03-10 00:29:34
*/
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 获取系统默认配置（一级配置）
     *
     * @return List<SysConfig>
     */
    List<SysConfig>  selectDefaultConfigList();

    /**
     * 获取系统配置
     *
     * @return List<SysConfig>
     */
    List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 分页获取系统配置
     *
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysConfig config);

    /**
     * 获取默认主题
     *
     * @return OssProperties
     */
    OssProperties getOssProperties();

    /**
     * 根据code获取主题信息
     *
     * @param code code编码
     * @return OssProperties
     */
    OssProperties getConfigByCode(String code);

    /**
     * 保存配置信息
     *
     * @param ossProperties OssProperties
     * @return boolean
     */
    boolean saveConfigOss(OssProperties ossProperties);

    /**
     * 修改默认oss
     *
     * @param code 关键词
     * @return boolean
     */
    boolean saveDefaultOss(String code);

    /**
     * 获取默认oss的code
     *
     * @return code
     */
    String defaultOss();

    /**
     * 清理Oss缓存
     */
    void clearOss();

    OssProperties getDefaultOssProperties();

}
