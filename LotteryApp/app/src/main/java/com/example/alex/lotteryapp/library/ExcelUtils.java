package com.example.alex.lotteryapp.library;

import android.content.Context;

import com.example.alex.lotteryapp.library.pojo.DataPojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtils {
    public static WritableFont titlefont = null;
    public static WritableCellFormat titleformat = null;
    public static WritableFont subtitlefont = null;
    public static WritableCellFormat subtitleformat = null;
    public static WritableFont arial12font = null;
    public static WritableCellFormat arial12format = null;

    public final static String UTF8_ENCODING = "UTF-8";
    public final static String GBK_ENCODING = "GBK";

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    public static void format() {
        try {
            titlefont = new WritableFont(WritableFont.ARIAL, 17, WritableFont.BOLD);
            titlefont.setColour(Colour.WHITE);
            titleformat = new WritableCellFormat(titlefont);
            titleformat.setAlignment(jxl.format.Alignment.CENTRE);
            titleformat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            titleformat.setBackground(Colour.BLUE_GREY);

            subtitlefont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            subtitleformat = new WritableCellFormat(subtitlefont);
            subtitleformat.setAlignment(jxl.format.Alignment.CENTRE);
            subtitleformat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            subtitleformat.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 13);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setAlignment(Alignment.CENTRE);
            arial12format.setVerticalAlignment(VerticalAlignment.CENTRE);
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); //设置边框
            arial12format.setWrap(true);//是否换行

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Excel
     */
    public static void initExcel(String fileName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("第一頁", 0);
            //   sheet.setRowView(1487, 2000); //设置行高
            sheet.setPageSetup(PageOrientation.PORTRAIT, PaperSize.A4, 0.5d, 0.5d);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void writeObjListToExcel(ArrayList<ArrayList<ArrayList<String>>> objList, String fileName) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                int row = 0;
                sheet.setColumnView(0, 14); //设置列宽
                sheet.setColumnView(1, 14); //设置列宽
                sheet.setColumnView(2, 14); //设置列宽
                sheet.setColumnView(3, 14); //设置列宽
                sheet.setColumnView(4, 14); //设置列宽
                sheet.setColumnView(5, 14); //设置列宽
                ArrayList<String> title = new ArrayList<>();
                title.add("獎項");
                title.add("六獎7位");
                title.add("五獎8位");
                title.add("四獎4位");
                title.add("三獎4位");
                title.add("二獎2位");
                ArrayList<String> subtitle = new ArrayList<>();
                subtitle.add("獎金");
                subtitle.add("1萬2");
                subtitle.add("2萬");
                subtitle.add("3萬");
                subtitle.add("5萬");
                subtitle.add("10萬");
                //创建标题栏
                sheet.mergeCells(0, row, 5, row);//(列，行，列，行)
                sheet.addCell(new Label(0, row++, "第一階段", titleformat));

                for (int col = 0; col < title.size(); col++) {
                    sheet.addCell(new Label(col, row, title.get(col), subtitleformat));
                }
                row++;
                for (int col = 0; col < subtitle.size(); col++) {
                    sheet.addCell(new Label(col, row, subtitle.get(col), subtitleformat));
                }
                row++;

                //11111111111111

                ArrayList<ArrayList<String>> typeList = objList.get(0);
                sheet.addCell(new Label(0, row, "中獎號碼", arial12format));
                for (int j = 0; j < typeList.size()-1; j++) {
                    sheet.addCell(new Label(j + 1, row, typeList.get(j) + "", arial12format));
                }
                row++;

                sheet.mergeCells(0, row, 5, row);//(列，行，列，行)
                sheet.addCell(new Label(0, row++, "第二階段", titleformat));
                title.clear();
                title.add("獎項");
                title.add("六獎6位");
                title.add("五獎8位");
                title.add("四獎4位");
                title.add("三獎3位");
                title.add("二獎1位");
                for (int col = 0; col < title.size(); col++) {
                    sheet.addCell(new Label(col, row, title.get(col), subtitleformat));
                }
                row++;
                for (int col = 0; col < subtitle.size(); col++) {
                    sheet.addCell(new Label(col, row, subtitle.get(col), subtitleformat));
                }
                row++;
                //222222222222222222
                typeList = objList.get(1);
                sheet.addCell(new Label(0, row, "中獎號碼", arial12format));
                for (int j = 0; j < typeList.size()-1; j++) {
                    sheet.addCell(new Label(j + 1, row, typeList.get(j) + "", arial12format));
                }
                row++;

                sheet.mergeCells(0, row, 5, row);//(列，行，列，行)
                sheet.addCell(new Label(0, row++, "第三階段", titleformat));
                title.clear();
                title.add("獎項");
                title.add("六獎6位");
                title.add("五獎7位");
                title.add("四獎4位");
                title.add("三獎2位");
                title.add("二獎1位");
                for (int col = 0; col < title.size(); col++) {
                    sheet.addCell(new Label(col, row, title.get(col), subtitleformat));
                }
                row++;
                for (int col = 0; col < subtitle.size(); col++) {
                    sheet.addCell(new Label(col, row, subtitle.get(col), subtitleformat));
                }
                row++;
                //333333333333333333333
                typeList = objList.get(2);
                sheet.addCell(new Label(0, row, "中獎號碼", arial12format));
                for (int j = 0; j < typeList.size()-1; j++) {
                    sheet.addCell(new Label(j + 1, row, typeList.get(j) + "", arial12format));
                }
                row++;

                sheet.mergeCells(0, row, 5, row);//(列，行，列，行)
                sheet.addCell(new Label(0, row++, "第四階段", titleformat));
                title.clear();
                title.add("獎項");
                title.add("六獎6位");
                title.add("五獎7位");
                title.add("四獎3位");
                title.add("三獎1位");
                title.add("二獎1位");
                for (int col = 0; col < title.size(); col++) {
                    sheet.addCell(new Label(col, row, title.get(col), subtitleformat));
                }
                row++;
                for (int col = 0; col < subtitle.size(); col++) {
                    sheet.addCell(new Label(col, row, subtitle.get(col), subtitleformat));
                }
                row++;
                //444444444444444444
                typeList = objList.get(3);
                sheet.addCell(new Label(0, row, "中獎號碼", arial12format));
                for (int j = 0; j < typeList.size()-1; j++) {
                    sheet.addCell(new Label(j + 1, row, typeList.get(j) + "", arial12format));
                }
                row++;

                sheet.mergeCells(0, row, 5, row);//(列，行，列，行)
                sheet.addCell(new Label(0, row++, "第五階段", titleformat));

                typeList = objList.get(4);
                sheet.addCell(new Label(0, row, "頭獎", arial12format));
                sheet.addCell(new Label(1, row, "30萬", arial12format));
                sheet.addCell(new Label(2, row, typeList.get(5) + "", arial12format));
                sheet.mergeCells(2, row, 5, row);//(列，行，列，行)

                writebook.write();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    //----------------------------------读------------------------------------

    public static List<DataPojo> read2DB(File f) {
        ArrayList<DataPojo> arrayList = new ArrayList<>();
        try {
            Workbook course = null;
            course = Workbook.getWorkbook(f);
            Sheet sheet = course.getSheet(0);

            Cell cell = null;
            for (int i = 1; i < sheet.getRows(); i++) {
                DataPojo dataPojo = new DataPojo();
                cell = sheet.getCell(0, i);
                dataPojo.classes = cell.getContents();
                cell = sheet.getCell(1, i);
                dataPojo.process = cell.getContents();
                cell = sheet.getCell(2, i);
                dataPojo.staff = cell.getContents();
                cell = sheet.getCell(3, i);
                dataPojo.id = cell.getContents();
                cell = sheet.getCell(4, i);
                dataPojo.number = Integer.parseInt(cell.getContents());
                cell = sheet.getCell(5, i);
                dataPojo.date = cell.getContents();
                cell = sheet.getCell(6, i);
                dataPojo.time = cell.getContents();
                arrayList.add(dataPojo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arrayList;
    }
//
//	public static Object getValueByRef(Class cls, String fieldName) {
//		Object value = null;
//		fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
//				.substring(0, 1).toUpperCase());
//		String getMethodName = "get" + fieldName;
//		try {
//			Method method = cls.getMethod(getMethodName);
//			value = method.invoke(cls);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return value;
//	}
}
