package com.auiucloud.core.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.util.Objects;

/**
 * @author dries
 **/
@Slf4j
public class RegionUtil {

    static Searcher searcher = null;

    /*
      初始化IP库
     */
    static {
        try {
            // 因为jar无法读取文件,复制创建临时文件
            String dbPath = Objects.requireNonNull(RegionUtil.class.getResource("/ip2region/ip2region.xdb")).getPath();
            // 1、从 dbPath 加载整个 xdb 到内存。
            byte[] cBuff = Searcher.loadContentFromFile(dbPath);
            searcher = Searcher.newWithBuffer(cBuff);
            log.info("bean [{}]", searcher);
        } catch (Exception e) {
            log.error("init ip region error: {}", e.getMessage());
        }
    }

    /**
     * 解析IP
     *
     * @param ip ip
     * @return region
     */
    public static String getRegion(String ip) {
        try {
            // db
            if (searcher == null || StringUtils.isEmpty(ip)) {
                log.error("DbSearcher is null");
                return StringUtils.EMPTY;
            }
            long startTime = System.currentTimeMillis();
            String result = searcher.search(ip);
            long endTime = System.currentTimeMillis();
            log.debug("region use time[{}] result[{}]", endTime - startTime, result);
            return result;
        } catch (Exception e) {
            log.error("error:{}", e.getMessage());
        }
        return StringUtils.EMPTY;
    }

}
