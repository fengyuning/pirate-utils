package demo;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.io.*;

/**
 * 写Excel表格文件的demo poi
 * 貌似有个更好的工具叫easyExcel
 */
public class ExcelDemo {

    public static void main(String[] args){
        //1.一个workbook对应一个 excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();

        //2.字体,单元格样式
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 8);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //3.创建sheet
        HSSFSheet sheet = workbook.createSheet("testSheet");

        //4.创建row cell
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Excel IO 测试");

        //5.合并单元格
        CellRangeAddress addresses = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(addresses);
        RegionUtil.setBorderTop(BorderStyle.THIN, addresses, sheet);

        //6.填充数据...

        //7.写文件
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream("C:/tmp/111.csv"));
            workbook.write(bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
