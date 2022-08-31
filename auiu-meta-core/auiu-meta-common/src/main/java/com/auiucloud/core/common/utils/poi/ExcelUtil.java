package com.auiucloud.core.common.utils.poi;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.auiucloud.core.common.utils.StringPool;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel导入导出工具类
 *
 * @author dries
 */
public class ExcelUtil {

    /**
     * 导出工具类
     *
     * @param list      数据
     * @param sheetName sheet
     * @param pojoClass 类
     * @param fileName  文件名称
     * @param response  响应流
     */
    public static void exportExcel(List<?> list, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) {
        defaultExport(list, pojoClass, fileName, sheetName, response);
    }

    /**
     * simpleWriteExcel
     *
     * @param list      数据
     * @param pojoClass 类
     * @param fileName  文件名称
     * @param sheetName sheet
     * @param response  响应流
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, String sheetName, HttpServletResponse response) {
        try {
            response.setCharacterEncoding(StringPool.UTF_8);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            EasyExcel.write(response.getOutputStream(), pojoClass).sheet(sheetName).doWrite(list);
        } catch (IOException e) {
            // throw new NormalException(e.getMessage());
        }
    }

    /**
     * 根据模板写入Excel
     *
     * @param list             数据
     * @param pojoClass        类
     * @param fileName         文件名称
     * @param templateFileName 模版名称
     * @param response         响应流
     */
    private static void defaultTemplateExport(List<?> list, Class<?> pojoClass, String fileName, String templateFileName, HttpServletResponse response) {
        try {
            response.setCharacterEncoding(StringPool.UTF_8);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            EasyExcel.write(response.getOutputStream(), pojoClass)
                    .withTemplate(templateFileName)
                    .sheet().doWrite(list);
        } catch (IOException e) {
            // throw new NormalException(e.getMessage());
        }
    }

    /**
     * 根据模板填充Excel
     *
     * @param list             数据
     * @param fileName         文件名称
     * @param templateFileName 模版名称
     * @param response         响应流
     */
    private static void defaultFillExport(List<Map<String, Object>> list, String fileName, String templateFileName, HttpServletResponse response) {
        try {
            response.setCharacterEncoding(StringPool.UTF_8);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            EasyExcel.write(response.getOutputStream())
                    .withTemplate(templateFileName)
                    .sheet().doFill(list);
        } catch (IOException e) {
            // throw new NormalException(e.getMessage());
        }
    }

    /**
     * 导入Excel
     *
     * @param file      文件
     * @param pojoClass 类名
     * @return List<T>
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) {
        return importExcel(file, pojoClass, false);
    }

    /**
     * 同步导入Excel，不推荐使用，如果数据量大会把数据放到内存里面
     *
     * @param file      文件
     * @param pojoClass 类名
     * @return List<T>
     */
    public static <T> List<T> importExcelSync(MultipartFile file, Class<T> pojoClass) {
        return importExcel(file, pojoClass, true);
    }

    /**
     * 导入Excel
     *
     * @param file      文件
     * @param pojoClass 类名
     * @return List<T>
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass, boolean sync) {
        if (file == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        try {
            if (sync) {
                list = EasyExcel.read(file.getInputStream()).head(pojoClass).doReadAllSync();
            } else {
                EasyExcel.read(file.getInputStream(), pojoClass, new PageReadListener<T>(list::addAll)).sheet().doRead();
            }
        } catch (IOException e) {
            // throw new ApiException(e.getMessage());
        }
        return list;
    }

    // /**
    //  * 导出工具类
    //  *
    //  * @param list
    //  * @param title
    //  * @param sheetName
    //  * @param pojoClass
    //  * @param fileName
    //  * @param isCreateHeader
    //  * @param response
    //  */
    // public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,
    //                                String fileName, boolean isCreateHeader, HttpServletResponse response) {
    //     ExportParams exportParams = new ExportParams(title, sheetName);
    //     exportParams.setCreateHeadRows(isCreateHeader);
    //     defaultExport(list, pojoClass, fileName, response, exportParams);
    // }
    //
    // /**
    //  * 导出工具类
    //  *
    //  * @param list
    //  * @param title
    //  * @param sheetName
    //  * @param pojoClass
    //  * @param fileName
    //  * @param response
    //  */
    // public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
    //                                HttpServletResponse response) {
    //     defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    // }
    //
    // public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
    //     defaultExport(list, fileName, response);
    // }
    //
    // private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName,
    //                                   HttpServletResponse response, ExportParams exportParams) {
    //     Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
    //     downLoadExcel(fileName, response, workbook);
    // }
    //
    // private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
    //     try {
    //         response.setCharacterEncoding("UTF-8");
    //         response.setHeader("content-Type", "application/vnd.ms-excel");
    //         response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
    //         workbook.write(response.getOutputStream());
    //     } catch (IOException e) {
    //         //throw new NormalException(e.getMessage());
    //     }
    // }
    //
    // private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
    //     Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
    //     if (workbook != null) ;
    //     downLoadExcel(fileName, response, workbook);
    // }
    //
    // public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
    //     if (StringUtils.isBlank(filePath)) {
    //         return null;
    //     }
    //     ImportParams params = new ImportParams();
    //     params.setTitleRows(titleRows);
    //     params.setHeadRows(headerRows);
    //     List<T> list = null;
    //     try {
    //         list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
    //     } catch (NoSuchElementException e) {
    //         throw new ApiException("模板不能为空");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new ApiException(e.getMessage());
    //     }
    //     return list;
    // }
    //
    // public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
    //     if (file == null) {
    //         return null;
    //     }
    //     ImportParams params = new ImportParams();
    //     params.setTitleRows(titleRows);
    //     params.setHeadRows(headerRows);
    //     List<T> list = null;
    //     try {
    //         list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
    //     } catch (NoSuchElementException e) {
    //         throw new ApiException("excel文件不能为空");
    //     } catch (Exception e) {
    //         throw new ApiException(e.getMessage());
    //     }
    //     return list;
    // }

}
