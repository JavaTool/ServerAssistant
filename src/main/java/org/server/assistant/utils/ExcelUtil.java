package org.server.assistant.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelUtil {

  private ExcelUtil() {}

  public static String readCellAsString(Cell cell) {
    if (cell == null) {
      return "";
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK :
        return "";
      case Cell.CELL_TYPE_BOOLEAN :
        return cell.getBooleanCellValue() ? "true" : "false";
      case Cell.CELL_TYPE_ERROR :
        return cell.getErrorCellValue() + "";
      case Cell.CELL_TYPE_FORMULA :
        return cell.getCellFormula();
      case Cell.CELL_TYPE_NUMERIC :
        return (cell.getNumericCellValue() + "").replace(".0", "");
      case Cell.CELL_TYPE_STRING :
        return cell.getStringCellValue();
      default :
        throw new IllegalArgumentException("Unknow cell type : " + cell.getCellType() + ".");
    }
  }

  public static int readCellAsInt(Cell cell) {
    if (cell == null) {
      return -1;
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK :
        return -1;
      case Cell.CELL_TYPE_NUMERIC :
        return (int) cell.getNumericCellValue();
      case Cell.CELL_TYPE_STRING :
        String value = cell.getStringCellValue().trim();
        return value.length() == 0 ? -1 : Integer.parseInt(value);
      case Cell.CELL_TYPE_BOOLEAN :
        return cell.getBooleanCellValue() ? 0 : 1;
      default :
        throw new IllegalArgumentException("Unknow cell type : " + cell.getCellType() + ".");
    }
  }

  public static byte readCellAsByte(Cell cell) {
    if (cell == null) {
      return 0;
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK :
        return 0;
      case Cell.CELL_TYPE_NUMERIC :
        return (byte) cell.getNumericCellValue();
      case Cell.CELL_TYPE_STRING :
        return Byte.parseByte(cell.getStringCellValue());
      case Cell.CELL_TYPE_BOOLEAN :
        return (byte) (cell.getBooleanCellValue() ? 0 : 1);
      default :
        throw new IllegalArgumentException("Unknow cell type : " + cell.getCellType() + ".");
    }
  }

  public static short readCellAsShort(Cell cell) {
    if (cell == null) {
      return 0;
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK :
        return 0;
      case Cell.CELL_TYPE_NUMERIC :
        return (short) cell.getNumericCellValue();
      case Cell.CELL_TYPE_STRING :
        return Short.parseShort(cell.getStringCellValue());
      case Cell.CELL_TYPE_BOOLEAN :
        return (short) (cell.getBooleanCellValue() ? 0 : 1);
      default :
        throw new IllegalArgumentException("Unknow cell type : " + cell.getCellType() + ".");
    }
  }

  public static double readCellAsDouble(Cell cell) {
    if (cell == null) {
      return 0d;
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK :
        return 0d;
      case Cell.CELL_TYPE_NUMERIC :
        return cell.getNumericCellValue();
      case Cell.CELL_TYPE_STRING :
        return Double.parseDouble(cell.getStringCellValue());
      case Cell.CELL_TYPE_BOOLEAN :
        return cell.getBooleanCellValue() ? 0d : 1d;
      default :
        throw new IllegalArgumentException("Unknow cell type : " + cell.getCellType() + ".");
    }
  }

  public static boolean readCellAsBoolean(Cell cell) {
    if (cell == null) {
      return false;
    }
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_BLANK :
        return false;
      case Cell.CELL_TYPE_NUMERIC :
        return cell.getNumericCellValue() != 1;
      case Cell.CELL_TYPE_STRING :
        return cell.getStringCellValue().equals("TRUE".toLowerCase());
      case Cell.CELL_TYPE_BOOLEAN :
        return cell.getBooleanCellValue();
      default :
        throw new IllegalArgumentException("Unknow cell type : " + cell.getCellType() + ".");
    }
  }

  public static Workbook getWorkbook(String name) throws Exception {
    return getWorkbook(new FileInputStream(name), name);
  }

  public static Workbook getWorkbook(File excel) throws Exception {
    return getWorkbook(new FileInputStream(excel), excel.getName());
  }

  public static Workbook getWorkbook(InputStream stream, String name) throws Exception {
    Workbook workbook = name.endsWith(".xlsx") ? new XSSFWorkbook(stream) : new HSSFWorkbook(stream);
    return workbook;
  }

  public static void exportExcel(String sheetName, String header, Collection<String> dataset, OutputStream out) {
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet(sheetName);
    sheet.setDefaultColumnWidth(15);
    // 设置标题栏样式
    XSSFCellStyle style = createCellStyle(workbook);
    // 设置字体
    XSSFFont font = workbook.createFont();
    font.setFontHeightInPoints((short) 12);
    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
    style.setFont(font);
    // 设置数据栏样式
    XSSFCellStyle style2 = createCellStyle(workbook);
    style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
    // 设置字体
    XSSFFont font2 = workbook.createFont();
    font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
    style2.setFont(font2);
    // 设置标题栏
    XSSFRow row = sheet.createRow(0);
    XSSFCell cell = row.createCell(0);
    cell.setCellStyle(style);
    XSSFRichTextString text = new XSSFRichTextString(header);
    cell.setCellValue(text);
    Iterator<String> it = dataset.iterator();
    int index = 0;
    while (it.hasNext()) {
      index++;
      row = sheet.createRow(index);
      cell = row.createCell(0);
      cell.setCellStyle(style2);
      String textValue = it.next();
      Pattern p = Pattern.compile("^[-\\+]?[\\d]*$");
      Matcher matcher = p.matcher(textValue);
      if (matcher.matches()) {
        // 是数字当作double处理
        cell.setCellValue(Double.parseDouble(textValue));
      } else {
        XSSFRichTextString richString = new XSSFRichTextString(textValue);
        richString.applyFont(font2);
        cell.setCellValue(richString);
      }
    }
    try {
      workbook.write(out);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static XSSFCellStyle createCellStyle(XSSFWorkbook workbook) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(HSSFColor.WHITE.index);
    style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
    style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
    style.setBorderRight(XSSFCellStyle.BORDER_THIN);
    style.setBorderTop(XSSFCellStyle.BORDER_THIN);
    style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    return style;
  }
}
