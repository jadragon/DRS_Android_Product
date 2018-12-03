package tw.com.lccnet.app.designateddriving.Utils;

public class MatchesUtils {
    private static String MATCHES_EMAIL = "[\\w-.]+@[\\w-]+(.[\\w_-]+)+";
    private static String MATCHES_PHONE = "09[0-9]{8}";

    public static boolean matchEmail(String email) {
        return email.matches(MATCHES_EMAIL);
    }

    public static boolean matchPhone(String phone) {
        return phone.matches(MATCHES_PHONE);
    }
}
