package com.wcq.thang.bean;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wcq.thang.config.Constant;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

/**
 * @author wcq
 * @date 2019/12/5 15:40
 */
public class FormatConversion {
    /**
     * Excel转txt接口
     * @param filePath
     * @return
     */
    public static String xlsOrXlsxToTxt(String filePath) {
        //文本文件内容
        String buffer = null;
        //文本文件存储路径
        String txtPath = null;
        InputStream inputStream = null;
        HSSFWorkbook hssfWorkbook = null;
        XSSFWorkbook xssfWorkbook = null;
        try {
            //打开一个输入流
            inputStream = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {//如果是xls
                //加载xls文件到内存中
                hssfWorkbook = new HSSFWorkbook(inputStream);
                //得到sheet(一个工作表)
                HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
                //读取表中数据
                buffer = readExcel(sheet);
                //释放
                hssfWorkbook.close();
                //关闭输入流
                inputStream.close();
                //生成txt路径
                txtPath = filePath.replace(".xls", ".txt");
                //写文本文件
                if(Utils.writerTxtFile(txtPath, buffer)){
                    return txtPath;
                }else{
                    return null;
                }
            } else if (filePath.endsWith(".xlsx")) {//如果是xlsx
                //加载xlsx文件到内存中
                xssfWorkbook = new XSSFWorkbook(inputStream);
                //得到sheet(一个工作表)
                XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
                //读取表中数据
                buffer = readExcel(sheet);
                //释放
                xssfWorkbook.close();
                //关闭输入流
                inputStream.close();
                //生成txt路径
                txtPath = filePath.replace(".xlsx", ".txt");
                //写文本文件
                if(Utils.writerTxtFile(txtPath, buffer)){
                    return txtPath;
                }else{
                 return null;
                }
            } else {//都不是
                //关闭输入流
                inputStream.close();
                System.out.println("非Excel");
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (hssfWorkbook != null) {
                try {
                    hssfWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (xssfWorkbook != null) {
                try {
                    xssfWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取Excel数据
     * @param sheet
     * @return
     */
    public static String readExcel(Sheet sheet){
        String buffer = null;
        for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {//获取总行数
            // 取出第i行 getRow(index) 获取第(j)行
            Row row = sheet.getRow(j);
            for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) { // getPhysicalNumberOfCells() 获取当前行的总列数
                //取出第j行第k列的值
                String value1 = getCellFormatValue(row.getCell(k));
                buffer += value1+",";
            }
            buffer+="\n";
        }
        return buffer;
    }

    /**
     * 这个方法对cell进行处理，传入cell对象，返回cell中内容，String类型。
     * @param cell
     * @return
     */
    public static String getCellFormatValue(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case NUMERIC: {//数字类型
                    if(DateUtil.isCellDateFormatted(cell)) {//时间
                        SimpleDateFormat sdf = null;
                        if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                                .getBuiltinFormat("h:mm")) {
                            sdf = new SimpleDateFormat("HH:mm");
                        } else {// 日期
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                        }
                        Date date = cell.getDateCellValue();
                        cellValue=sdf.format(date);
                    }else {//不是时间
                        String temp = String.valueOf(cell.getNumericCellValue());
                        if(temp.indexOf(".") >1) {//小数
                            cellValue = String.valueOf(new Double(temp)).trim();
                        }else {
                            cellValue = temp;
                        }
                    }
                    break;
                }
                case STRING: {//字符
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                case FORMULA:{//公式
                    String temp = String.valueOf(cell.getNumericCellValue());
                    if (temp.indexOf(".") > -1) {//小数
                        temp = String.valueOf(new Double(temp)).trim();
                        Double cny = Double.parseDouble(temp);
                        DecimalFormat df = new DecimalFormat("0.00");//格式化为保留两位的
                        cellValue=df.format(cny);
                    }else {
                        cellValue = temp;
                    }

                }
                case BOOLEAN:{//布尔类型
                    cellValue = Boolean.toString(cell.getBooleanCellValue());
                    break;
                }
                case BLANK:{//空格
                    if(cell==null||cell.equals("") || cell.getCellType() == CellType.BLANK){
                        cellValue = "";
                    }else {
                        cellValue = "**";
                    }
                }
                default:
                    cellValue = "**";
            }
        }
        return cellValue;
    }

    /**
     * world转txt接口
     * @param filePath
     * @return
     */
    public static String docOrDocxToTxt(String filePath) {
        //txt文件内容
        String buffer = null;
        //txt文件路径
        String txtPath = null;
        InputStream inputStream = null;
        WordExtractor wordExtractor = null;
        POIXMLTextExtractor poixmlTextExtractor = null;
        try {
            if (filePath.endsWith(".doc")) {//如果是doc
                //打开文件输入流
                inputStream = new FileInputStream(filePath);
                //doc加载如内存
                wordExtractor = new WordExtractor(inputStream);
                //获取文本内容
                buffer = wordExtractor.getText();
                //释放
                wordExtractor.close();
                //关闭输入流
                inputStream.close();
                //生成txt存储路径
                txtPath = filePath.replace(".doc", ".txt");
                //写文件
                if(Utils.writerTxtFile(txtPath, buffer)){
                    return txtPath;
                }else{
                    return null;
                }
            } else if (filePath.endsWith(".docx")) {
                //将docx文档加载到内存中
                OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
                poixmlTextExtractor = new XWPFWordExtractor(opcPackage);
                //获取文档文本内容
                buffer = poixmlTextExtractor.getText();
                //释放
                poixmlTextExtractor.close();
                //生成txt存储路径
                txtPath = filePath.replace(".docx", ".txt");
                //写文件
                if(Utils.writerTxtFile(txtPath, buffer)){
                    return txtPath;
                }else{
                    return null;
                }
            } else {
                System.out.println("非word文档");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlException e) {
            e.printStackTrace();
            return null;
        } catch (OpenXML4JException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (wordExtractor != null) {
                try {
                    wordExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (poixmlTextExtractor != null) {
                try {
                    poixmlTextExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * pdf转txt接口
     * @param filePath
     * @return
     */
    public static String pdfToTxt(String filePath){
        // 开始提取页数
        int startPage = 1;
        //结束提取页数
        int endPage = Integer.MAX_VALUE;
        PDDocument pdDocument = null;
        InputStream inputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            //打开文件输入流
            inputStream = new FileInputStream(filePath);
            //加载pdf文件
            pdDocument = PDDocument.load(inputStream);
            // PDFTextStripper来提取文本
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            // 设置是否排序
            pdfTextStripper.setSortByPosition(false);
            // 设置起始页
            pdfTextStripper.setStartPage(startPage);
            // 设置结束页
            pdfTextStripper.setEndPage(endPage);
            //生成要存储的txt文件的路径
            String txtPath = filePath.replace(".pdf", ".txt");
            //打开文件输出流
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(txtPath), Constant.ENCODING);
            // 调用PDFTextStripper的writeText提取并输出文本
            pdfTextStripper.writeText(pdDocument, outputStreamWriter);
            //释放
            pdDocument.close();
            //关闭文件输入流
            inputStream.close();
            //关闭文件输出流
            outputStreamWriter.close();
            return txtPath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (pdDocument != null) {
                try {
                    pdDocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
