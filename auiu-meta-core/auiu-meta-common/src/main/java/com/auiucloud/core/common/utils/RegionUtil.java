package com.auiucloud.core.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author dries
 **/
@Slf4j
public class RegionUtil {

    private static final String JAVA_TEMP_DIR = "java.io.tmpdir";

    static Searcher searcher = null;

    /*
      初始化IP库
     */
    static {
        try {
            String dbPath = Objects.requireNonNull(RegionUtil.class.getResource("/ip2region/ip2region.xdb")).getPath();
            File file = new File(dbPath);
            // 1、从 dbPath 加载整个 xdb 到内存。
            byte[] cBuff = new byte[(int) file.length()];
            if (!file.exists()) {
                String tmpDir = System.getProperties().getProperty(JAVA_TEMP_DIR);
                dbPath = tmpDir + "ip2region.xdb";
                file = new File(dbPath);
                ClassPathResource cpr = new ClassPathResource("ip2region" + File.separator + "ip2region.xdb");
                InputStream resourceAsStream = cpr.getInputStream();
                if (resourceAsStream != null) {
                    FileUtils.copyInputStreamToFile(resourceAsStream, file);
                }
                cBuff = Searcher.loadContentFromFile(dbPath);

            }

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
