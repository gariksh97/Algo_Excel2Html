package member;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by garik on 01.03.17.
 */
public class Member {
    Map<String, String> map;

    public static String[] order = {
            "runkl", "party", "class", "school", "place", null, "solved", "winner"
    };

    public Member() {
        map = new HashMap<>();
    }

    public void set(String key, String value) {
        if (key.equals("school"))
            value = School.schoolName(value);
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public static void forOrder(
            Consumer<String> simple,
            Consumer<String> number,
            Function<String, Boolean> cond) {
        for (String s: order) {
            if (s != null)
                simple.accept(s);
            else {
                int count = 1;
                String countStr = String.valueOf(count);
                while (cond.apply(countStr)) {
                    number.accept(countStr);
                    count++;
                    countStr = String.valueOf(count);
                }
            }

        }
    }

}
