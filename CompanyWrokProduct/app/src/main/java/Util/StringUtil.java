package Util;

import java.text.DecimalFormat;

public class StringUtil {
    public static String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }
}
