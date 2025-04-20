package lol.aabss.skuishy.other;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class Text {
    private static final List<String> normal = Arrays.stream("abcdefghijklmnopqrstuvwxyz".split("")).toList();
    private static final List<String> tiny = Arrays.stream("\u1D00\u0299\u1D04\u1D05\u1D07\u0493\u0262\u029C\u026A\u1D0A\u1D0B\u029F\u1D0D\u0274\u1D0F\u1D18\u01EB\u0280s\u1D1B\u1D1C\u1D20\u1D21x\u028F\u1D22".split("")).toList();
    private static final List<String> supertiny = Arrays.stream("\u1D43\u1D47\u1D9C\u1D48\u1D49\u1DA0\u1D4D\u02B0\u1DA6\u02B2\u1D4F\u02E1\u1D50\u207F\u1D52\u1D56\u1D60\u02B3\u02E2\u1D57\u1D58\u1D5B\u02B7\u02E3\u02B8\u1DBB".split("")).toList();

    @SuppressWarnings("deprecation")
    public static String tinyText(String s, boolean superTiny){
        boolean b = false;
        StringBuilder string = new StringBuilder();
        for (String st : s.split("")) {
            if (b){
                b = false;
                if (ChatColor.getByChar(st) != null) {
                    string.append(st);
                    continue;
                }
            }
            int index = normal.indexOf(st);
            if (index != -1){
                string.append(superTiny ? supertiny.get(index) : tiny.get(index));
            } else {
                if (st.equals("&")){
                    b = true;
                }
                string.append(st);
            }
        }
        return string.toString();
    }

}
