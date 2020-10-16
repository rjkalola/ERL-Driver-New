package com.app.utilities.utils;

import java.util.ArrayList;
import java.util.List;

public class MonthUtil {

    private static String[][] monthArray =
            {
                    {
                            "January", "Jan"
                    },
                    {
                            "February", "Feb"
                    },
                    {
                            "March", "Mar"
                    },
                    {
                            "April", "April"
                    },
                    {
                            "May", "May"
                    },
                    {
                            "June", "June"
                    },
                    {
                            "July", "July"
                    },
                    {
                            "August", "Aug"
                    },
                    {
                            "September", "Sept"
                    },
                    {
                            "October", "Oct"
                    },
                    {
                            "November", "Nov"
                    },
                    {
                            "December", "Dec"
                    }
            };

    public static String getShortMonthName(String longName) {
        for (int i = 0; i < monthArray.length; i++) {
            if (longName.equals(monthArray[i][0])) {
                return monthArray[i][1];
            }
        }
        return null;
    }

    public static String getLongMonthName(String shortName) {
        for (int i = 0; i < monthArray.length; i++) {
            if (shortName.equals(monthArray[i][1])) {
                return monthArray[i][0];
            }
        }

        return null;
    }

    public static List getShortMonthNameList() {
        List<String> monthList = new ArrayList();
        for (String[] strings : monthArray) {
            monthList.add(strings[1]);
        }
        return monthList;
    }

    public static List getLongMonthNameList() {
        List<String> monthList = new ArrayList();
        for (String[] strings : monthArray) {
            monthList.add(strings[0]);
        }
        return monthList;
    }
}
