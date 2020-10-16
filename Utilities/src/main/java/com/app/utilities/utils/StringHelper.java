package com.app.utilities.utils;

import android.content.Context;
import androidx.annotation.Nullable;

/**
 * @Auther Dhaval Jivani
 */

public final class StringHelper {

    /**
     * Check String is Empty OR Null
     */
    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(@Nullable String str) {
        return !isEmpty(str);
    }

    /**
     * Convert any object to string
     */
    public static String convertToString(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }


    /**
     * @param inputString
     * @return $returnType$
     */
    public static String firstAsUpperCase(String inputString) {
        if (!isEmpty(inputString)) {
            return inputString.substring(0, 1).toUpperCase() + inputString.substring(1, inputString.length());
        } else {
            return inputString;
        }
    }


    /**
     * @param inputString
     * @return $returnType$
     */
    public static String firstAsLowerCase(String inputString) {
        if (!isEmpty(inputString)) {
            return inputString.substring(0, 1).toLowerCase() + inputString.substring(1, inputString.length());
        } else {
            return inputString;
        }
    }

    /**
     * @param value1
     * @param value2
     * @return $returnType$
     */
    public static boolean equals(String value1, String value2) {
        return ((value1 != null) && (value2 != null) && value1.equals(value2)) || ((value1 == null) && (value2 == null));
    }

    /**
     * @param value1
     * @param value2
     * @return $returnType$
     */
    public static boolean equalsTrim(String value1, String value2) {
        return ((value1 != null) && (value2 != null) && value1.trim().equals(value2.trim())) || ((value1 == null) && (value2 == null));
    }

    /**
     * @param value1
     * @param value2
     * @return $returnType$
     */
    public static boolean equalsNoCase(String value1, String value2) {
        return ((value1 != null) && (value2 != null) && value1.toLowerCase().trim().equals(value2.toLowerCase().trim())) || ((value1 == null) && (value2 == null));
    }

    /**
     *
     * @param str
     * @return
     */
    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }

    /**
     *
     * @param inString
     * @param oldPattern
     * @param newPattern
     * @return
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!isEmpty(inString) || !isEmpty(oldPattern) || !isEmpty(newPattern )) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    /**
     *
      *@param toSplit
     * @param delimiter
     * @return
     */
    public static String[] split(String toSplit, String delimiter) {
        if (isEmpty(toSplit) || isEmpty(delimiter)) {
            return null;
        }
        return toSplit.split(delimiter);
    }

    /**
     * @param value
     * @return
     */
    public static boolean isAlpha(String value) {
        if (isEmpty(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);

            if (!Character.isLetter(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param val
     * @return
     */
    public static boolean isAlphaNumeric(String val) {
        if (isEmpty(val)) {
            return false;
        }
        for (int i = 0; i < val.length(); i++) {
            char ch = val.charAt(i);

            if (!Character.isLetterOrDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param input
     * @return
     */
    public static boolean containsOnlyDigits(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param mContext
     * @param aString
     * @return
     */
    public static String getStringResourceByName(Context mContext, String aString) {
        String packageName = mContext.getPackageName();
        return mContext.getString(mContext.getResources().getIdentifier(aString, "string", packageName));
    }
}
