package com.feiyu.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 作者：liuxiaowen
 * 创建时间： 2017/4/18
 * email：liuxiaowen@teacher.com.cn
 */
public class ExcelUtils {

    /**
     * excle2003之前的类型
     */
    private static final String EXCEL_2003L = "xls";

    /**
     * excle2007及之后的类型
     */
    private static final String EXCEL_2007U = "xlsx";

    /**
     * 默认的格式
     */
    private static SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 根据上传文件获取实体类的list
     *
     * @param file
     * @param entityClass
     * @param titles
     * @return
     */
    public static <E> List<E> getEntityList(MultipartFile file, Class<E> entityClass, String[] titles) {
        InputStream input = null;
        try {
            input = file.getInputStream();
            List<E> list = getEntityList(input, file.getOriginalFilename(), entityClass, titles);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(input);
        }
        return null;
    }

    /**
     * 根据文件File获取实体类的list
     *
     * @param file
     * @param entityClass
     * @param titles
     * @return
     */
    public static <E> List<E> getEntityList(File file, Class<E> entityClass, String[] titles) {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            List<E> list = getEntityList(input, file.getName(), entityClass, titles);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(input);
        }
        return null;
    }

    /**
     * 关闭输入流，可以调用CloseUtil里的close方法
     *
     * @param input
     */
    private static void closeInputStream(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据输入流以及文件名称获取实体类List
     *
     * @param input
     * @param fileName
     * @param entityClass
     * @param titles
     * @return
     */
    public static <E> List<E> getEntityList(InputStream input, String fileName, Class<E> entityClass, String[] titles) {
        Workbook hsswork = getWorkbook(input, fileName);
        Sheet sheet = hsswork.getSheetAt(0);
        List<E> list = getEntityList(sheet, entityClass, titles);
        return list;
    }

    /**
     * 从sheet页里获取数据，然后转换成对应的list<Bean>
     *
     * @param sheet
     * @param clazz
     * @param titles
     * @return
     */
    private static <E> List<E> getEntityList(Sheet sheet, Class<E> clazz, String[] titles) {
        List<E> tlist = null;
        int titleLength = titles.length;
        int columnlength = sheet.getPhysicalNumberOfRows();
        int lastRowNum = sheet.getLastRowNum();
        if (columnlength > 0) {
            tlist = new ArrayList<E>();
            Map<String, Object> entityMap = null;
            for (int i = 1; i <= lastRowNum; i++) {
                entityMap = new HashMap<>();
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                int rowlength = row.getLastCellNum();
                int minLength = Math.min(rowlength, titleLength);
                for (int j = 0; j < minLength; j++) {
                    Cell cell = row.getCell(j);
                    String key = titles[j];
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case HSSFCell.CELL_TYPE_STRING:
                                entityMap.put(key, cell.getStringCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    entityMap.put(key, cell.getDateCellValue());
                                } else {
                                    entityMap.put(key, cell.getNumericCellValue());
                                }
                                break;
                            case HSSFCell.CELL_TYPE_FORMULA:
                                entityMap.put(key, cell.getCellFormula());
                                break;
                            default:
                                entityMap.put(key, cell.getStringCellValue());
                                break;
                        }
                    }
                }
                tlist.add(convertMapToEntity(entityMap, clazz));
            }
        } else {
            return null;
        }
        return tlist;

    }

    /**
     * 根据文件名后缀获取不同的workbook
     *
     * @param input
     * @param fileName
     * @return
     */
    private static Workbook getWorkbook(InputStream input, String fileName) {
        Workbook wb = null;
        try {
            if (fileName == null) {

            } else if (fileName.toLowerCase().endsWith(EXCEL_2003L)) {
                wb = new HSSFWorkbook(input);
            } else if (fileName.toLowerCase().endsWith(EXCEL_2007U)) {
                wb = new XSSFWorkbook(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    private static Workbook getWorkbook(File file) {
        if (file == null || !file.exists()) {
            throw getCustomRuntimeException("导出文件模板不存在");
        }
        try (InputStream fileInputStream = new FileInputStream(file)) {
            return getWorkbook(fileInputStream, file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw getCustomRuntimeException("导出文件模板不存在");
        } catch (IOException e) {
            e.printStackTrace();
            throw getCustomRuntimeException("导出文件模板出错");
        }
    }

    /**
     * 将map转换为实体
     *
     * @param map
     * @param clazz
     * @return
     */
    private static <E> E convertMapToEntity(Map<String, Object> map, Class<E> clazz) {
        return JSON.parseObject(JSON.toJSONString(map), clazz);
    }

    /**
     * 导出到excel
     *
     * @param titleMap     导出的标题 <propertyName, 中文名>
     * @param dataList     数据列
     * @param outputStream 输出流
     * @param <E>
     */
    public static <E> void exportDataToExcel(Map<String, String> titleMap, List<E> dataList,
                                             OutputStream outputStream) {
        Workbook wb = null;
        try {
            wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();
            int startRowNum = 0;

            Map<Integer, Integer> maxColumnLength = new HashMap<>();

            if (titleMap != null && titleMap.size() > 0) {
                writeTitle(wb, titleMap, sheet, maxColumnLength);
                String[] dataProperties = getDataPropertiesFromMap(titleMap);
                // 写入数据
                writeDataToSheet(dataList, sheet, startRowNum, maxColumnLength, dataProperties);
                wb.write(outputStream);
                outputStream.flush();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
    }

    private static String[] getDataPropertiesFromMap(Map<String, String> titleMap) {
        return titleMap.keySet().toArray(new String[titleMap.size()]);
    }

    private static int writeTitle(Workbook wb, Map<String, String> titleMap, Sheet sheet, Map<Integer, Integer> maxColumnLength) {
        CellStyle titleStyle = getTitleStyle(wb);
        int startRowNum = 0;
        Row row = null;
        Cell cell = null;
        sheet.autoSizeColumn(startRowNum);
        row = sheet.createRow(startRowNum++);
        String[] dataProperties = new String[titleMap.size()];
        int index = 0;
        // 写入标题
        for (Entry<String, String> entry : titleMap.entrySet()) {
            cell = row.createCell(index);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(entry.getValue());
            if (maxColumnLength != null) {
                maxColumnLength.put(index, cell.getStringCellValue().getBytes().length);
            }
            dataProperties[index] = entry.getKey();
            index++;
        }
        return startRowNum;
    }

    /**
     * 根据模板来导处文件
     *
     * @param templatePath   模板路径
     * @param dataList       数据list
     * @param startRow       数据开始行数
     * @param outputStream   输出流
     * @param dataProperties 属性列
     * @param <E>
     */
    public static <E> void exportDataToExcel(String templatePath, List<E> dataList, int startRow,
                                             OutputStream outputStream, String... dataProperties) {
        Workbook wb = null;
        try {
            File file = new File(templatePath);
            wb = getWorkbook(file);
            // 写入数据
            writeDataToSheet(dataList, wb.getSheetAt(0), startRow, null, dataProperties);
            wb.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据模板来导出文件
     *
     * @param templatePath 模板路径
     * @param dataList     数据list
     * @param startRow     数据开始行数
     * @param outputStream 输出流
     * @param titleMap     导出的标题 <propertyName, 中文名>
     * @param <E>
     */
    public static <E> void exportDataToExcelFromTemplate(String templatePath, List<E> dataList, int startRow,
                                                         OutputStream outputStream, Map<String, String> titleMap) {
        Workbook wb = null;
        try {
            String[] dataProperties = new String[titleMap.size()];
            int index = 0;
            for (String titleKey : titleMap.keySet()) {
                dataProperties[index++] = titleKey;
            }

            File file = new File(templatePath);
            wb = getWorkbook(file);
            // 写入数据
            writeDataToSheet(dataList, wb.getSheetAt(0), startRow, null, dataProperties);
            wb.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据模板来导出文件
     *
     * @param templatePath 模板路径
     * @param dataList     数据list
     * @param titleMap     导出的标题 <propertyName, 中文名>
     */
    public static void exportDataToExcelSheetFromTemplate(String templatePath, String sheetName, List<?> dataList,
                                                              Map<String, String> titleMap) {
        Workbook wb = null;
        String[] dataProperties = getDataPropertiesFromMap(titleMap);
        File file = new File(templatePath);
        wb = getWorkbook(file);
        Sheet sheet = wb.getSheet(sheetName);
        if (sheet != null) {
            return;
        } else {
            sheet = wb.createSheet(sheetName);
        }
        int startRow = writeTitle(wb, titleMap, sheet, null);
        // 写入数据
        writeDataToSheet(dataList, sheet, startRow, null, dataProperties);
        try (OutputStream outputStream = new FileOutputStream(templatePath)) {
            wb.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将 数据 写入sheet页， 不包含标题
     *
     * @param dataList        数据list
     * @param sheet           sheet页
     * @param startRow        开始行数
     * @param maxColumnLength 设定最大列的宽度
     * @param dataProperties  数据顺序
     * @param <E>
     */
    private static <E> void writeDataToSheet(List<E> dataList, Sheet sheet, int startRow,
                                             Map<Integer, Integer> maxColumnLength, String... dataProperties) {
        Row row = null;
        Cell cell = null;
        int startRowNum = startRow;
        int dataColNums = dataProperties.length;
        Workbook wb = sheet.getWorkbook();
        boolean isDesignColumnLength = maxColumnLength != null;
        for (E e : dataList) {
            row = sheet.createRow(startRowNum++);
            for (int i = 0; i < dataColNums; i++) {
                cell = row.createCell(i);
                Object value = ReflectUtils.getValue(e, dataProperties[i]);

                if (value instanceof BigDecimal) {
                    DecimalFormat df2 = new DecimalFormat("¥#,##0");
                    value = df2.format(value);
                    cell.setCellValue(value != null ? value.toString() : "");
                    cell.setCellStyle(getBodyStyle(wb));
                } else {
                    cell.setCellValue(value != null ? value.toString() : "");
                    cell.setCellStyle(getBodyStyle(wb));
                }
                if (isDesignColumnLength && cell.getStringCellValue().getBytes().length > maxColumnLength.getOrDefault(i, 0)) {
                    maxColumnLength.put(i, cell.getStringCellValue().getBytes().length);
                }
            }
        }

        if (isDesignColumnLength) {
            for (int i = 0; i < dataColNums; i++) {
                sheet.setColumnWidth(i, maxColumnLength.get(i) * 2 * 144);
            }
        }
    }


    /**
     * 设置标题头加粗
     *
     * @param wb
     * @return
     */
    private static CellStyle getTitleStyle(Workbook wb) {

        CellStyle style = wb.createCellStyle();
        // 设置单元格的背景颜色为淡蓝色
//            style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
//            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 设置单元格居中对齐
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
//            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        style.setWrapText(true);

        // 设置单元格边框为细线条
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);

        // 设置单元格字体样式
        Font font = wb.createFont();
        // 设置字体加粗
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeight((short) 200);
        style.setFont(font);

        return style;
    }

    /**
     * 设置标题头加粗
     *
     * @param wb
     * @return
     */
    private static CellStyle getBodyStyle(Workbook wb) {

        CellStyle style = wb.createCellStyle();
        // 设置单元格居中对齐
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
//            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);

        // 设置单元格边框为细线条
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);

        // 设置单元格字体样式
        Font font = wb.createFont();
        // 设置字体加粗
        font.setFontHeight((short) 200);
        style.setFont(font);
        return style;
    }


    public static <E> void exportDataToExcel(List<String> titleList, List<E> dataList,
                                             OutputStream outputStream, String[] dataProperties, Map<Integer, String[]> validationInfo,
                                             List<Integer> numberColumn) {
        Workbook wb = null;
        try {
            wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();
            int startRowNum = 0;
            Row row = null;
            Cell cell = null;
            Map<Integer, Integer> maxColumnLength = new HashMap<>();
            CellStyle titleStyle = getTitleStyle(wb);
            if (titleList != null && titleList.size() > 0) {
                sheet.autoSizeColumn(startRowNum);
                row = sheet.createRow(startRowNum++);
                for (int i = 0; i < titleList.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(titleStyle);
                    cell.setCellValue(titleList.get(i));
                    maxColumnLength.put(i, cell.getStringCellValue().getBytes().length);
                }
            }

            int dataColNums = dataProperties.length;
            for (E e : dataList) {
                row = sheet.createRow(startRowNum++);
                for (int i = 0; i < dataColNums; i++) {
                    cell = row.createCell(i);
                    Object value = ReflectUtils.getValue(e, dataProperties[i]);
                    if (null == value) {
                        value = ReflectUtils.getValueByMethod(e, dataProperties[i]);
                    }
                    cell.setCellValue(value != null ? value.toString() : "");
                    cell.setCellStyle(getBodyStyle(wb));
                    if (cell.getStringCellValue().getBytes().length > maxColumnLength.get(i)) {
                        maxColumnLength.put(i, cell.getStringCellValue().getBytes().length);
                    }
                }
            }
            for (int i = 0; i < titleList.size(); i++) {
                if (null != numberColumn && numberColumn.contains(i)) {
                    sheet.setColumnWidth(i, maxColumnLength.get(i) * 2 * 144);
                } else {
                    sheet.setColumnWidth(i, maxColumnLength.get(i) * 2 * 100);
                }

            }
            if (null != validationInfo && !validationInfo.isEmpty()) {
                DataValidationHelper helper = sheet.getDataValidationHelper();
                for (Integer colunm : validationInfo.keySet()) {
                    CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, colunm, colunm);
                    String[] values = validationInfo.get(colunm);
                    DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
                    DataValidation dataValidation = helper.createValidation(constraint, addressList);
                    if (dataValidation instanceof XSSFDataValidation) {
                        dataValidation.setSuppressDropDownArrow(true);
                        dataValidation.setShowErrorBox(true);
                        dataValidation.createErrorBox("错误", "数据无效，会导致导入失败！");
                    } else {
                        dataValidation.setSuppressDropDownArrow(false);
                    }
                    sheet.addValidationData(dataValidation);
                }
            }
            wb.write(outputStream);
            outputStream.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
    }

    private static RuntimeException getCustomRuntimeException(String message) {
        return new RuntimeException(message);
    }
}
