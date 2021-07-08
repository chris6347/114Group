package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestPOI {

    @Test
    public void testRead() throws IOException {

        //1. 创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook("D:\\上课\\114期\\day52_传智健康\\04_资料\\read.xlsx");

        //2. 获取工作表
        XSSFSheet sheet = wb.getSheetAt(0);

        //3. 表其实可以看成是行的集合，所以直接遍历这个表即可
        for (Row row : sheet) {

            //4. 行可以看成是格子的集合，所以直接遍历行得到格子
            for (Cell cell : row) {
                String value = cell.getStringCellValue();
                System.out.println("value=" + value);
            }
        }
    }


    @Test
    public void testWrite() throws Exception {

        //1. 定义工作簿
        XSSFWorkbook wb = new XSSFWorkbook();

        //2. 创建工作表
        XSSFSheet sheet = wb.createSheet("黑马114学员表");

        //3. 创建第一行
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue(1);
        row0.createCell(1).setCellValue("汪渊");
        row0.createCell(2).setCellValue(17);


        //第二行
        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue(2);
        row1.createCell(1).setCellValue("李文豪");
        row1.createCell(2).setCellValue(19);

        //第三行
        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue(3);
        row2.createCell(1).setCellValue("李亮锋");
        row2.createCell(2).setCellValue(18);


        //保存到文件中
        wb.write(new FileOutputStream("D:/heima114.xlsx"));
    }
}
