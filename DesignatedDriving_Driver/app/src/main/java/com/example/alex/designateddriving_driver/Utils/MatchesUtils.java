package com.example.alex.designateddriving_driver.Utils;

public class MatchesUtils {
    private static String MATCHES_EMAIL = "[\\w-.]+@[\\w-]+(.[\\w_-]+)+";
    private static String MATCHES_PHONE = "09[0-9]{8}";
    private static String MATCHES_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";

    public static boolean matchEmail(String email) {
        return email.matches(MATCHES_EMAIL);
    }

    public static boolean matchPhone(String phone) {
        return phone.matches(MATCHES_PHONE);
    }

    public static boolean matchPassword(String password) {
        return password.matches(MATCHES_PASSWORD);
    }
}
