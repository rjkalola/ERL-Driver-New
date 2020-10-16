package com.app.utilities.utils;

import android.view.View;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * @AutherBy Dhaval Jivani
 */

public class ValidationUtil {

    public static boolean isEmptyEditText(String s) {
        if (StringHelper.isEmpty(s)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        if (StringHelper.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean isValidFullName(String name){
        if(!StringHelper.isEmpty(name) && name.trim().contains(" ")){
            return true;
        }else {
            return false;
        }
    }

    public static boolean isValidConfirmPasswrod(String confirmPassword, String password) {
        if (!confirmPassword.equals(password)) {
            return false;
        } else {
            return true;
        }
    }

    public static void setErrorIntoEditext(View view, String message) {
        if (view instanceof EditText && !StringHelper.isEmpty(message)) {
            ((EditText) view).setError(message);
            (view).requestFocus();
        }
    }

    public static void setErrorIntoInputTextLayout(View viewEditText, View viewInputTextLout, String message) {
        if (!StringHelper.isEmpty(message)) {
            ((TextInputLayout) viewInputTextLout).setError(message);
            (viewEditText).requestFocus();
        }
    }

    public static boolean isValidField(String text, String pattern) {
        if (StringHelper.isEmpty(text)) {
            return false;
        } else {
            return text.trim().matches(pattern);
        }
    }

    public static boolean checkMinTextValidation(String text, int textLength) {
        if (StringHelper.isEmpty(text)) {
            return false;
        } else return text.trim().length() >= textLength;
    }

    public static boolean checkMaxTextValidation(String text, int textLength) {
        if (StringHelper.isEmpty(text)) {
            return false;
        } else return text.trim().length() <= textLength;
    }

    public static boolean checkEnglishLanguagesValidation(String text) {
        if (StringHelper.isEmpty(text)) {
            return false;
        }

        if (text.matches("[a-zA-Z0-9.? !\"#$%&'()*+,-./:;<=>?@^_`~[|]]*")) {
            return true;
        } else {
            return false;
        }
    }

}
