package member;

import java.io.*;

/**
 * Created by garik on 01.03.17.
 */
public class School {
    public static String schoolName(String fullName)  {
        fullName = fullName.trim();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream("school.data"), "UTF-8")
        )) {
            while (in.ready()) {
                String full = in.readLine().trim().toLowerCase();
                String name = in.readLine().trim();
                if (fullName.toLowerCase().equals(full))
                    return name;
                in.readLine();
            }
        } catch (IOException | NullPointerException ignored){}
        /*if (fullName.contains("«")) {
            return fullName.substring(fullName.indexOf("«") + 1, fullName.indexOf("»"));
        }*/
        return fullName;
    }
}
