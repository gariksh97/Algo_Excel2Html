import excel.WorkbookParser;
import html.HtmlCreator;
import html.HtmlTagCreator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.tidy.Tidy;

import java.io.*;

/**
 * Created by garik on 01.03.17.
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        if (args.length < 5) {
            System.out.println("usage: [Name of excel file] [Name of sheet] [Html out] [Name] [Time]");
            return;
        }

        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(args[2]), "windows-1251");
        String split[] = args[0].split("\\.");
        String type = split[split.length - 1];
        Workbook workbook = type.equals("xlsx") ?
                new XSSFWorkbook(args[0]) :
                new HSSFWorkbook(new FileInputStream(args[0]));
        Sheet sheet = workbook.getSheet(args[1]);
        String render = HtmlCreator.htmlCreator(
                HtmlTagCreator.getHead(
                        WorkbookParser.getHead(sheet)
                ),
                WorkbookParser.getBody(
                        sheet, HtmlTagCreator.getIndexMap(WorkbookParser.getHead(sheet))
                ), args[3], args[4]
        );
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("windows-1251");
        tidy.setOutputEncoding("windows-1251");
        tidy.setWraplen(Integer.MAX_VALUE);
        tidy.setXmlOut(true);
        tidy.setForceOutput(true);
        tidy.setTidyMark(false);
        tidy.setShowWarnings(false);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(render.getBytes("windows-1251"));
        tidy.parse(inputStream, out);
        out.close();
    }
}
