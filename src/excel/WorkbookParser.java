package excel;

import member.Member;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by garik on 28.02.17.
 */
public class WorkbookParser {
    public static List<String> getHead(Sheet sheet) {
        List<String> list = new ArrayList<>();
        Row row = sheet.getRow(0);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            list.add(cellToString(row.getCell(i)));
        }
        return list;
    }

    private static String cellToString(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return String.valueOf(
                        (int) cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().
                                evaluate(cell).getNumberValue()
                );
            default:
                return cell.getStringCellValue();
        }
    }

    public static List<Member> getBody(
            Sheet sheet,
            Map<String, Integer> map) {
        List<Member> result = new ArrayList<>();
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            result.add(new Member());
            final int _i = i;
            Member.forOrder(
                    s -> {
                        if (map.get(s) != null)
                            result.get(_i - 1).set(s, cellToString(sheet.getRow(_i).getCell(map.get(s))));
                    },
                    s -> result.get(_i - 1).set(s, cellToString(sheet.getRow(_i).getCell(map.get(s)))),
                    s -> map.get(s) != null
            );
        }
        result.sort((o1, o2) ->
                Integer.compare(
                        Integer.parseInt(o2.get("solved")),
                        Integer.parseInt(o1.get("solved"))
                )
        );
        for (int i = 0; i < result.size(); i++) {
            result.get(i).set("runkl", String.valueOf(i + 1));
        }
        return result;
    }
}
