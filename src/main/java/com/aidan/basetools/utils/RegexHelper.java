package com.aidan.basetools.utils;

import android.util.Patterns;

public class RegexHelper {
    public static Boolean checkEmailFormat(String checkedString) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(checkedString).matches();
    }
    public static Boolean checkPhoneFormat(String checkedString) {
        return Patterns.PHONE.matcher(checkedString).matches();
    }
}
