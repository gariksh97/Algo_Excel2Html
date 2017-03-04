package html;

import javafx.util.Pair;
import member.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by garik on 28.02.17.
 */
public class HtmlTagCreator {
    private static final String school = "Полное наименование образовательного учреждения (по уставу)";

    public static List<Pair<String, String>> getHead(List<String> header) {
        List<Pair<String, String>> list = new ArrayList<>();
        boolean anySchool = false;
        for (String text : header) {
            String res = headTextToExcelTag(text);
            if (res != null) {
                if (text.equals(school) && anySchool) {
                    continue;
                }
                if (text.equals(school)) {
                    anySchool = true;
                    list.add(new Pair<>(res, "Школа"));
                    continue;
                }
                list.add(new Pair<>(res, text));
            }
        }
        list.add(new Pair<>("runkl", "Место"));
        List<Pair<String, String>> result = new ArrayList<>();
        Member.forOrder(
                s -> result.add(findByKey(list, s)),
                s -> result.add(new Pair<>("problem", findByKey(list, s).getValue())),
                s -> findByKey(list, s) != null
        );
        return result;
    }

    static Pair<String, String> findByKey(
            List<Pair<String, String>> list, String key) {
        for (Pair<String, String> pair : list) {
            if (pair == null) continue;
            if (pair.getKey().equals(key))
                return pair;
        }
        return null;
    }

    public static Map<String, Integer> getIndexMap(List<String> header) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < header.size(); i++) {
            String res = headTextToExcelTag(header.get(i));
            if (res != null) {
                map.putIfAbsent(res, i);
            }
        }
        return map;
    }


    private static String headTextToExcelTag(String text) {
        switch (text.trim()) {
            case "Участник":
                return "party";
            case "Балл":
            case "Solved":
            case "Сумма":
                return "solved";
            case "Полное наименование образовательного учреждения (по уставу)":
            case "Школа":
                return "school";
            case "Муниципалитет":
            case "Город/pайон":
                return "place";
            case "Диплом":
                return "winner";
            case "Класс":
                return "class";
        }
        int num = getNumber(text.trim());
        return num == -1 ? null : String.valueOf(num);
    }

    static int getNumber(String value) {
        try {
            Double num = Double.parseDouble(value);
            return num.intValue();
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

}
