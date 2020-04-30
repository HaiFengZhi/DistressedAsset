package com.distressed.asset.common.utils;

import com.distressed.asset.common.annotation.ExcelAttribute;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel导入导出工具类
 *
 * @author yanhai
 * @version 1.0 2016-10-11 11:23:10
 * @since 1.1
 */
public class ExcelUtil<T> implements Serializable {

    private static Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    private static final long serialVersionUID = 551970754610248636L;

    private Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ExcelUtil(Class<T> clazz, HttpServletRequest request, HttpServletResponse response, String exportFileName) throws UnsupportedEncodingException {
        this.clazz = clazz;
        //处理中文文件名
        String excelFileName = MyStringUtils.encodeFileName(exportFileName, request);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment;filename=" + excelFileName);
    }




    /**
     * 将excel表单数据源的数据导入到list
     *
     * @param sheetName
     *            工作表的名称
     * @param input
     *            java输入流
     */
    public List<T> excelToList(String sheetName, InputStream input) throws Exception {
        List<T> list = new ArrayList<T>();
        try {
            HSSFWorkbook book = new HSSFWorkbook(input);
            HSSFSheet sheet = null;
            // 如果指定sheet名,则取指定sheet中的内容.
            if (MyStringUtils.isNotBlank(sheetName)) {
                sheet = book.getSheet(sheetName);
            }
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            if (sheet == null) {
                sheet = book.getSheetAt(0);
            }
            // 得到数据的行数
            int rows = sheet.getLastRowNum();
            // 有数据时才处理
            if (rows > 0) {
                // 得到类的所有field
                Field[] allFields = clazz.getDeclaredFields();
                // 定义一个map用于存放列的序号和field
                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
                for (int i = 0, index = 0; i < allFields.length; i++) {
                    Field field = allFields[i];
                    // 将有注解的field存放到map中
                    if (field.isAnnotationPresent(ExcelAttribute.class)) {
                        // 设置类的私有字段属性可访问
                        field.setAccessible(true);
                        fieldsMap.put(index, field);
                        index++;
                    }
                }
                // 从第2行开始取数据,默认第一行是表头
                for (int i = 1, len = rows; i <= len; i++) {
                    // 得到一行中的所有单元格对象.
                    HSSFRow row = sheet.getRow(i);
                    Iterator<Cell> cells = row.cellIterator();
                    T entity = null;
                    int index = 0;
                    while (cells.hasNext()) {
                        // 单元格中的内容.
                        HSSFCell cell = (HSSFCell) cells.next();
                        CellAddress cellAddress = cell.getAddress();
                        int cc = cellAddress.getColumn();
//                        cell.setCellType();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String c = cell.getStringCellValue();
                        c = c.replaceAll("[\b\r\n\t]*", "").trim();
                        if (!MyStringUtils.isNotBlank(c)) {
                            continue;
                        }
                        if (c.indexOf("合计：") != -1) {
                            continue;
                        }
                        // 如果不存在实例则新建
                        entity = (entity == null ? clazz.newInstance() : entity);
                        // 从map中得到对应列的field
                        Field field = fieldsMap.get(cc);
                        if (field == null) {
                            continue;
                        }
                        // 取得类型,并根据对象类型设置值.
                        Class<?> fieldType = field.getType();
                        if (fieldType == null) {
                            continue;
                        }
                        if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if (BigDecimal.class == fieldType) {
                            c = c.indexOf("%") != -1 ? c.replace("%", "") : c;
                            field.set(entity, BigDecimal.valueOf(Double.valueOf(c)));
                        } else if (Date.class == fieldType) {
                            field.set(entity, MyDateUtils.parseDate(c));
                        } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                            field.set(entity, Integer.parseInt(c));
                        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Character.TYPE == fieldType) {
                            if ((c != null) && (c.length() > 0)) {
                                field.set(entity, Character.valueOf(c.charAt(0)));
                            }
                        }
                        index++;

                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("将excel表单数据源的数据导入到list异常!", e);
        }
        return list;
    }

    /**
     * 将list数据源的数据导入到excel表单(少量数据)
     *
     * @param list
     *            数据源
     * @param sheetName
     *            工作表的名称
     * @param output
     *            java输出流
     */
    public boolean exportExcel(List<T> list, String sheetName, OutputStream output) throws Exception {
        try {
            // excel中每个sheet中最多有65535行
            int sheetSize = 65535;
            // 得到所有定义字段
            Field[] allFields = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<Field>();
            // 得到所有field并存放到一个list中
            for (Field field : allFields) {
                if (field.isAnnotationPresent(ExcelAttribute.class)) {
                    fields.add(field);
                }
            }
            // 产生工作薄对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 取出一共有多少个sheet
            int listSize = 0;
            if (list != null && list.size() >= 0) {
                listSize = list.size();
            }
            double sheetNo = Math.ceil(listSize / sheetSize);
            for (int index = 0; index <= sheetNo; index++) {
                // 产生工作表对象
                HSSFSheet sheet = workbook.createSheet();
                // 设置工作表的名称.
                workbook.setSheetName(index, sheetName + index);
                HSSFRow row;
                HSSFCell cell;// 产生单元格
                row = sheet.createRow(0);// 产生一行
                /* *********普通列样式********* */
                HSSFFont font = workbook.createFont();
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                font.setFontName("Arail narrow"); // 字体
                //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
                font.setBold(false);// 字体宽度
                /* *********金额样式********* */
                HSSFCellStyle moneyCellStyle = workbook.createCellStyle();
                HSSFDataFormat format= workbook.createDataFormat();
                moneyCellStyle.setDataFormat(format.getFormat("#,##0.00"));
                /* *********数字样式********* */
                HSSFCellStyle numberCellStyle = workbook.createCellStyle();
                HSSFDataFormat numberFormat= workbook.createDataFormat();
                numberCellStyle.setDataFormat(numberFormat.getFormat("0"));
                /* *********标红列样式********* */
                HSSFFont newFont = workbook.createFont();
                HSSFCellStyle newCellStyle = workbook.createCellStyle();
                newFont.setFontName("Arail narrow"); // 字体
                //newFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
                newFont.setBold(false);// 字体宽度
                /* *************创建列头名称*************** */
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                    int col = i;
                    // 根据指定的顺序获得列号
                    if (MyStringUtils.isNotBlank(attr.column())) {
                        col = getExcelCol(attr.column());
                    }
                    // 创建列
                    cell = row.createCell(col);
                    if (attr.isMark()) {
                        newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
                        newCellStyle.setFont(newFont);
                        cell.setCellStyle(newCellStyle);
                    } else {
                        font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                    }
                    //sheet.setColumnWidth(i, (int) ((attr.name().getBytes().length <= 4 ? 6 : attr.name().getBytes().length) * 1.5 * 256));
                    sheet.setColumnWidth(i, attr.width() * 256);
                    // 设置列中写入内容为String类型
                    //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellType(CellType.STRING);
                    // 写入列名
                    cell.setCellValue(attr.name());
                    // 如果设置了提示信息则鼠标放上去提示.
                    if (MyStringUtils.isNotBlank(attr.prompt())) {
                        setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);
                    }
                    // 如果设置了combo属性则本列只能选择不能输入
                    if (attr.combo().length > 0) {
                        setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);
                    }
                }
                /* *************创建内容列*************** */
                font = workbook.createFont();
                cellStyle = workbook.createCellStyle();
                int startNo = index * sheetSize;
                int endNo = Math.min(startNo + sheetSize, listSize);
                // 写入各条记录,每条记录对应excel表中的一行
                for (int i = startNo; i < endNo; i++) {
                    row = sheet.createRow(i + 1 - startNo);
                    T vo = (T) list.get(i); // 得到导出对象.
                    for (int j = 0; j < fields.size(); j++) {
                        // 获得field
                        Field field = fields.get(j);
                        // 设置实体类私有属性可访问
                        field.setAccessible(true);
                        ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                        int col = j;
                        // 根据指定的顺序获得列号
                        if (MyStringUtils.isNotBlank(attr.column())) {
                            col = getExcelCol(attr.column());
                        }
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            // 创建cell
                            cell = row.createCell(col);
                            if (attr.isMark()) {
                                newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
                                newCellStyle.setFont(newFont);
                                cell.setCellStyle(newCellStyle);
                            } else if(attr.isMoney()) {
                                cell.setCellStyle(moneyCellStyle);
                            }else if(attr.isNumber()) {
                                cell.setCellStyle(numberCellStyle);
                            }else {
                                font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
                                cellStyle.setFont(font);
                                cell.setCellStyle(cellStyle);
                            }
                            // 如果数据存在就填入,不存在填入空格
                            Class<?> classType = (Class<?>) field.getType();
                            String value = null;
                            if (field.get(vo) != null && classType.isAssignableFrom(Date.class)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                value = MyDateUtils.formatDate(sdf.parse(String.valueOf(field.get(vo))),attr.datePattern());
                            }

                            //是否是金额,如果是就格式化金额
                            if(attr.isMoney()) {
                                cell.setCellValue(field.get(vo) == null ? 0L : ((BigDecimal) field.get(vo)).doubleValue());
                            }else if(attr.isNumber()) {
                                cell.setCellValue(field.get(vo) == null ? 0L : ((Integer) field.get(vo)).intValue());
                            }else if(attr.isLong()) {
                                cell.setCellValue(field.get(vo) == null ? 0L : ((Long) field.get(vo)).intValue());
                            }else {
                                cell.setCellValue(field.get(vo) == null ? "" : value == null ? String.valueOf(field.get(vo)) : value);
                            }
                        }
                    }
                }

                /* *************创建合计列*************** */
                HSSFRow lastRow = sheet.createRow((int) (sheet.getLastRowNum() + 1));
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                    if (attr.isSum()) {
                        int col = i;
                        // 根据指定的顺序获得列号
                        if (MyStringUtils.isNotBlank(attr.column())) {
                            col = getExcelCol(attr.column());
                        }
                        BigDecimal totalNumber = BigDecimal.ZERO;
                        for (int j = 1, len = (sheet.getLastRowNum() - 1); j < len; j++) {
                            HSSFRow hssfRow = sheet.getRow(j);
                            if (hssfRow != null) {
                                Cell hssfCell = hssfRow.getCell(col);
                                if (hssfCell != null) {
                                    totalNumber = totalNumber.add(BigDecimal.valueOf(hssfCell.getNumericCellValue()));
                                }
                            }
                        }
                        HSSFCell sumCell = lastRow.createCell(col);
                        sumCell.setCellValue(new HSSFRichTextString("合计：" + totalNumber));
                    }
                }
            }
            output.flush();
            workbook.write(output);
            output.close();
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new Exception("将list数据源的数据导入到excel表单异常!", e);
        }
    }

    /**
     * 将list数据源的数据导入到excel表单(海量数据)
     * 海量数据导出不支持汇总.
     * @param list
     *            数据源
     * @param sheetName
     *            工作表的名称
     * @param output
     *            java输出流
     */
    public boolean exportLargeExcel(List<T> list, String sheetName, OutputStream output) throws Exception {
        try {
            // excel中每个sheet中最多有1048575行
            int sheetSize = 1048575;
            // 得到所有定义字段
            Field[] allFields = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<Field>();
            // 得到所有field并存放到一个list中
            for (Field field : allFields) {
                if (field.isAnnotationPresent(ExcelAttribute.class)) {
                    fields.add(field);
                }
            }
            // 产生工作薄对象
            SXSSFWorkbook workbook = new SXSSFWorkbook(5000);
            // 取出一共有多少个sheet
            int listSize = 0;
            if (list != null && list.size() >= 0) {
                listSize = list.size();
            }
            double sheetNo = Math.ceil(listSize / sheetSize);
            for (int index = 0; index <= sheetNo; index++) {
                // 产生工作表对象
                Sheet sheet = workbook.createSheet();
                // 设置工作表的名称.
                workbook.setSheetName(index, sheetName + index);
                Row row;
                Cell cell;// 产生单元格
                row = sheet.createRow(0);// 产生一行
                /* *********普通列样式********* */
                Font font = workbook.createFont();
                CellStyle cellStyle = workbook.createCellStyle();
                font.setFontName("Arail narrow"); // 字体
                //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
                font.setBold(false);// 字体宽度
                /* *********金额样式********* */
                CellStyle moneyCellStyle = workbook.createCellStyle();
                DataFormat format= workbook.createDataFormat();
                moneyCellStyle.setDataFormat(format.getFormat("#,##0.00"));
                /* *********数字样式********* */
                CellStyle numberCellStyle = workbook.createCellStyle();
                DataFormat numberFormat= workbook.createDataFormat();
                numberCellStyle.setDataFormat(numberFormat.getFormat("0"));
                /* *********利率百分比样式********* */
                CellStyle rateCellStyle = workbook.createCellStyle();
                DataFormat formata= workbook.createDataFormat();
                rateCellStyle.setDataFormat(formata.getFormat("0.00%"));
                /* *********标红列样式********* */
                Font newFont = workbook.createFont();
                CellStyle newCellStyle = workbook.createCellStyle();
                newFont.setFontName("Arail narrow"); // 字体
                //newFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
                newFont.setBold(false);// 字体宽度
                /* *************创建列头名称*************** */
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                    int col = i;
                    // 根据指定的顺序获得列号
                    if (MyStringUtils.isNotBlank(attr.column())) {
                        col = getExcelCol(attr.column());
                    }
                    // 创建列
                    cell = row.createCell(col);
                    if (attr.isMark()) {
                        newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
                        newCellStyle.setFont(newFont);
                        cell.setCellStyle(newCellStyle);
                    } else {
                        font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                    }
                    //sheet.setColumnWidth(i, (int) ((attr.name().getBytes().length <= 4 ? 6 : attr.name().getBytes().length) * 1.5 * 256));
                    sheet.setColumnWidth(i, attr.width() * 256);
                    // 设置列中写入内容为String类型
                    //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellType(1);
                    // 写入列名
                    cell.setCellValue(attr.name());
                    // 如果设置了提示信息则鼠标放上去提示.
                    if (MyStringUtils.isNotBlank(attr.prompt())) {
                        setPrompt(sheet, "", attr.prompt(), 1, 100, col, col);
                    }
                    // 如果设置了combo属性则本列只能选择不能输入
                    if (attr.combo().length > 0) {
                        setValidation(sheet, attr.combo(), 1, 100, col, col);
                    }
                }
                /* *************创建内容列*************** */
                font = workbook.createFont();
                cellStyle = workbook.createCellStyle();
                int startNo = index * sheetSize;
                int endNo = Math.min(startNo + sheetSize, listSize);
                // 写入各条记录,每条记录对应excel表中的一行
                for (int i = startNo; i < endNo; i++) {
                    row = sheet.createRow(i + 1 - startNo);
                    T vo = (T) list.get(i); // 得到导出对象.
                    for (int j = 0; j < fields.size(); j++) {
                        // 获得field
                        Field field = fields.get(j);
                        // 设置实体类私有属性可访问
                        field.setAccessible(true);
                        ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                        int col = j;
                        // 根据指定的顺序获得列号
                        if (MyStringUtils.isNotBlank(attr.column())) {
                            col = getExcelCol(attr.column());
                        }
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            // 创建cell
                            cell = row.createCell(col);
                            if (attr.isMark()) {
                                newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
                                newCellStyle.setFont(newFont);
                                cell.setCellStyle(newCellStyle);
                            } else if(attr.isMoney()) {
                                cell.setCellStyle(moneyCellStyle);
                            }else if(attr.isNumber())
                            {
                                cell.setCellStyle(numberCellStyle);
                            }else if (attr.isDateValue()){
                                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
                                cell.setCellStyle(cellStyle);
                            }else if(attr.isRate()) {
                                cell.setCellStyle(rateCellStyle);
                            }else {
                                font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
                                cellStyle.setFont(font);
                                cell.setCellStyle(cellStyle);
                            }
                            // 如果数据存在就填入,不存在填入空格
                            Class<?> classType = (Class<?>) field.getType();
                            String value = null;
                            if (field.get(vo) != null && classType.isAssignableFrom(Date.class)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                value = MyDateUtils.formatDate(sdf.parse(String.valueOf(field.get(vo))), attr.datePattern());
                            }

                            //是否是金额,如果是就格式化金额
                            if(attr.isMoney()) {
                                cell.setCellValue(field.get(vo) == null ? 0L : ((BigDecimal) field.get(vo)).doubleValue());
                            }else if(attr.isNumber()) {
                                cell.setCellValue(field.get(vo) == null ? 0L : ((Integer) field.get(vo)).intValue());
                            }else if(attr.isDateValue()) {
                                cell.setCellValue(field.get(vo) == null ? null :(Date)field.get(vo));
                            }else if(attr.isRate()) {
                                cell.setCellValue(field.get(vo) == null ? 0L :((BigDecimal) field.get(vo)).doubleValue());
                            }else {
                                cell.setCellValue(field.get(vo) == null ? "" : value == null ? String.valueOf(field.get(vo)) : value);
                            }
                        }
                    }
                }
            }
            output.flush();
            workbook.write(output);
            output.close();
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new Exception("将list数据源的数据导入到excel表单异常!", e);
        }
    }

    /**
     * 将list数据源的数据导入到excel表单(海量数据)
     * 海量数据导出不支持汇总.
     *
     *
     */
    public SXSSFWorkbook exportLargeExcelByHead(String sheetName) throws Exception {
        try {
            // excel中每个sheet中最多有1048575行
            int sheetSize = 1048575;
            // 得到所有定义字段
            Field[] allFields = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<Field>();
            // 得到所有field并存放到一个list中
            for (Field field : allFields) {
                if (field.isAnnotationPresent(ExcelAttribute.class)) {
                    fields.add(field);
                }
            }
            // 产生工作薄对象
            SXSSFWorkbook workbook = new SXSSFWorkbook(5000);
            // 取出一共有多少个sheet
            int listSize = 1;
//            if (list != null && list.size() >= 0) {
//                listSize = list.size();
//            }
            double sheetNo = Math.ceil(listSize / sheetSize);
            for (int index = 0; index <= sheetNo; index++) {
                // 产生工作表对象
                Sheet sheet = workbook.createSheet();
                // 设置工作表的名称.
                workbook.setSheetName(index, sheetName + index);
                Row row;
                Cell cell;// 产生单元格
                row = sheet.createRow(0);// 产生一行
                /* *********普通列样式********* */
                Font font = workbook.createFont();
                CellStyle cellStyle = workbook.createCellStyle();
                font.setFontName("Arail narrow"); // 字体
                //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
                font.setBold(false);// 字体宽度
                /* *********金额样式********* */
                CellStyle moneyCellStyle = workbook.createCellStyle();
                DataFormat format= workbook.createDataFormat();
                moneyCellStyle.setDataFormat(format.getFormat("#,##0.00"));
                /* *********数字样式********* */
                CellStyle numberCellStyle = workbook.createCellStyle();
                DataFormat numberFormat= workbook.createDataFormat();
                numberCellStyle.setDataFormat(numberFormat.getFormat("0"));
                /* *********标红列样式********* */
                Font newFont = workbook.createFont();
                CellStyle newCellStyle = workbook.createCellStyle();
                newFont.setFontName("Arail narrow"); // 字体
                //newFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
                newFont.setBold(false);// 字体宽度
                /* *************创建列头名称*************** */
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                    int col = i;
                    // 根据指定的顺序获得列号
                    if (MyStringUtils.isNotBlank(attr.column())) {
                        col = getExcelCol(attr.column());
                    }
                    // 创建列
                    cell = row.createCell(col);
                    if (attr.isMark()) {
                        newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
                        newCellStyle.setFont(newFont);
                        cell.setCellStyle(newCellStyle);
                    } else {
                        font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                    }
                    //sheet.setColumnWidth(i, (int) ((attr.name().getBytes().length <= 4 ? 6 : attr.name().getBytes().length) * 1.5 * 256));
                    sheet.setColumnWidth(i, attr.width() * 256);
                    // 设置列中写入内容为String类型
                    //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellType(1);
                    // 写入列名
                    cell.setCellValue(attr.name());
                    // 如果设置了提示信息则鼠标放上去提示.
                    if (MyStringUtils.isNotBlank(attr.prompt())) {
                        setPrompt(sheet, "", attr.prompt(), 1, 100, col, col);
                    }
                    // 如果设置了combo属性则本列只能选择不能输入
                    if (attr.combo().length > 0) {
                        setValidation(sheet, attr.combo(), 1, 100, col, col);
                    }
                }
                /* *************创建内容列*************** */
                font = workbook.createFont();
                cellStyle = workbook.createCellStyle();
                int startNo = index * sheetSize;
                int endNo = Math.min(startNo + sheetSize, listSize);
                // 写入各条记录,每条记录对应excel表中的一行

            }
            return workbook;
        } catch (Exception e) {
            throw new Exception("将list数据源的数据导入到excel表单异常!", e);
        }
    }

    /**
     * 将list数据源的数据导入到excel表单(海量数据)
     * 海量数据导出不支持汇总.
     * @param list
     *
     */
    public SXSSFWorkbook exportLargeExcelByPart(List<T> list,SXSSFWorkbook workbook ,int startNo,int endNo) throws Exception {

            Sheet sheet = workbook.getSheetAt(0);
            Field[] allFields = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<Field>();
            Row row;
            Cell cell;
            Font font = workbook.createFont();
            CellStyle cellStyle = workbook.createCellStyle();
            Font newFont = workbook.createFont();
            CellStyle newCellStyle = workbook.createCellStyle();
            CellStyle moneyCellStyle = workbook.createCellStyle();
            DataFormat format= workbook.createDataFormat();
            moneyCellStyle.setDataFormat(format.getFormat("#,##0.00"));
            CellStyle numberCellStyle = workbook.createCellStyle();
            DataFormat numberFormat= workbook.createDataFormat();
            numberCellStyle.setDataFormat(numberFormat.getFormat("0"));
            // 得到所有field并存放到一个list中
            for (Field field : allFields) {
                if (field.isAnnotationPresent(ExcelAttribute.class)) {
                    fields.add(field);
                }
            }
            startNo++;
            endNo++;
            Iterator<T> iterator = list.iterator();
                // 写入各条记录,每条记录对应excel表中的一行
                for (int i = startNo; i < endNo; i++) {
                    row = sheet.createRow(i);
                    T vo = null;
                    if (iterator.hasNext()) {
                        vo = iterator.next(); // 得到导出对象.
                    }else {
                        break;
                    }
                    for (int j = 0; j < fields.size(); j++) {
                        // 获得field
                        Field field = fields.get(j);
                        // 设置实体类私有属性可访问
                        field.setAccessible(true);
                        ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                        int col = j;
                        // 根据指定的顺序获得列号
                        if (MyStringUtils.isNotBlank(attr.column())) {
                            col = getExcelCol(attr.column());
                        }
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            // 创建cell
                            cell = row.createCell(col);
                            if (attr.isMark()) {
                                newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
                                newCellStyle.setFont(newFont);
                                cell.setCellStyle(newCellStyle);
                            } else if(attr.isMoney()) {
                                cell.setCellStyle(moneyCellStyle);
                            }else if(attr.isNumber())
                            {
                                cell.setCellStyle(numberCellStyle);
                            }else if (attr.isDateValue()){
                                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
                                cell.setCellStyle(cellStyle);
                            }else {
                                font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
                                cellStyle.setFont(font);
                                cell.setCellStyle(cellStyle);
                            }
                            // 如果数据存在就填入,不存在填入空格
                            Class<?> classType = (Class<?>) field.getType();
                            String value = null;
                            if (field.get(vo) != null && classType.isAssignableFrom(Date.class)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                value = MyDateUtils.formatDate(sdf.parse(String.valueOf(field.get(vo))), attr.datePattern());
                            }

                            //是否是金额,如果是就格式化金额
                            if(attr.isMoney()) {
                                cell.setCellValue(field.get(vo) == null ? 0L : ((BigDecimal) field.get(vo)).doubleValue());
                            }else if(attr.isNumber()) {
                                cell.setCellValue(field.get(vo) == null ? 0L : ((Integer) field.get(vo)).intValue());
                            }else if(attr.isDateValue()) {
                                cell.setCellValue(field.get(vo) == null ? null :(Date)field.get(vo));
                            }else {
                                cell.setCellValue(field.get(vo) == null ? "" : value == null ? String.valueOf(field.get(vo)) : value);
                            }
                        }
                    }
                }
            return workbook;
//        } catch (Exception e) {
//            throw new Exception("将list数据源的数据导入到excel表单异常!", e);
//        }
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 设置单元格上提示(Excel2003)
     *
     * @param sheet
     *            要设置的sheet.
     * @param promptTitle
     *            标题
     * @param promptContent
     *            内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                                          int firstCol, int endCol) {
        // 构造constraint对象
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

    /**
     * 设置单元格上提示(Excel2007)
     *
     * @param sheet
     *            要设置的sheet.
     * @param promptTitle
     *            标题
     * @param promptContent
     *            内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static Sheet setPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.(Excel2003)
     *
     * @param sheet
     *            要设置的sheet.
     * @param textlist
     *            下拉框显示的内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框(Excel2007)
     *
     * @param sheet
     *            要设置的sheet.
     * @param textlist
     *            下拉框显示的内容
     * @param firstRow
     *            开始行
     * @param endRow
     *            结束行
     * @param firstCol
     *            开始列
     * @param endCol
     *            结束列
     * @return 设置好的sheet.
     */
    public static Sheet setValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        //处理Excel兼容性问题
        if(dataValidation instanceof XSSFDataValidation){
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }else{
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
        return sheet;
    }

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    /**
     * 判断文件是否是excel文件。
     *
     * @param file 上传文件对象。
     * @throws IOException IO异常。
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (fileName != null && !fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException(fileName + "不是excel文件");
        }
    }

    /**
     * 根据上传文件后缀，返回相应的工作薄（Workbook）对象。
     *
     * @param file 上传文件对象。
     * @return 工作薄（Workbook）对象。
     */
    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return null;
        }
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return workbook;
    }

    /**
     * 根据坐标精确获取单元格数据，统一转成字符串返回。
     *
     * @param sheet 要获取的sheet对象。
     * @param rowIndex 行坐标，从0开始。
     * @param colIndex 列坐标，从0开始。
     * @return 单元格数据字符串。
     */
    public static String getCellValue(Sheet sheet, int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(colIndex);
        CellType cellType = cell.getCellTypeEnum();
        String value;
        if (cellType == CellType.STRING) {
            value = cell.getRichStringCellValue().getString().trim();
        } else if (cellType == CellType.NUMERIC) {
            //格式化number String字符
            DecimalFormat df = new DecimalFormat("0");
            //日期格式化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            //格式化数字
            DecimalFormat df2 = new DecimalFormat("0.00");
            String dataFormat = cell.getCellStyle().getDataFormatString();
            if ("General".equals(dataFormat)) {
                value = df.format(cell.getNumericCellValue());
            } else if ("m/d/yy".equals(dataFormat) || dataFormat.contains("yy")) {
                value = sdf.format(cell.getDateCellValue());
            } else {
                value = df2.format(cell.getNumericCellValue());
            }
        } else {
            cell.setCellType(CellType.STRING);
            value = cell.getRichStringCellValue().getString().trim();
        }
        return value;
    }
}
