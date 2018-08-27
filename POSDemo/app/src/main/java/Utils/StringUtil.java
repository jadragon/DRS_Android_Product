package Utils;

import java.text.DecimalFormat;

public class StringUtil {
    public static String getDeciamlString(int number) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(number + ""));
    }

    public static String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }

    public static String htmlFormat(String noamltext, String specialtext, String specialcolor, int insertPosition) {
        StringBuilder builder = new StringBuilder(noamltext);
        String text1 = "<big><font color=\"#" + specialcolor + "\">" + specialtext + "</font></big>";
        builder.insert(insertPosition, text1);
        return builder.toString();
    }

    public static String htmlFormat1(String noamltext, String specialcolor, int insertPosition) {
        StringBuilder builder = new StringBuilder(noamltext);
        String text1 = "<big><font color=\"" + specialcolor + "\">" + "</font></big>";
        builder.insert(insertPosition, text1);
        return builder.toString();
    }

    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
}
