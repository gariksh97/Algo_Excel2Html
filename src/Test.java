import excel.WorkbookParser;
import html.HtmlCreator;
import html.HtmlTagCreator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.tidy.Tidy;

import java.io.*;


/**
 * Created by garik on 28.02.17.
 */
public class Test {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("test.html"), "windows-1251");
        XSSFWorkbook workbook = new XSSFWorkbook(
                new File("test.xlsx")
        );
        XSSFSheet sheet = workbook.getSheet("7-8");



        String render = HtmlCreator.htmlCreator(
                HtmlTagCreator.getHead(
                        WorkbookParser.getHead(sheet)
                ),
                WorkbookParser.getBody(
                        sheet, HtmlTagCreator.getIndexMap(WorkbookParser.getHead(sheet))
                ), "Олимпиада", "5:00:00"
        );
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("windows-1251");
        tidy.setOutputEncoding("windows-1251");
        tidy.setWraplen(Integer.MAX_VALUE);
        tidy.setXmlOut(true);
        tidy.setForceOutput(true);
        tidy.setTidyMark(false);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(render.getBytes("windows-1251"));
        tidy.parse(inputStream, out);
        out.close();
    }
}
