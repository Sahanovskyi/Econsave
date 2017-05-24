package com.gw.domain.model;

public enum Interval {
    LAST_WEEK,
    LAST_MONTH,
    LAST_QUARTER,
    LAST_HALF_YEAR,
    LAST_YEAR,
    LAST_5_YEARS;

    public static Interval fromInteger(int x) {
        switch(x) {
            case 0:
                return LAST_WEEK;
            case 1:
                return LAST_MONTH;
            case 2:
                return LAST_QUARTER;
            case 3:
                return LAST_HALF_YEAR;
            case 4:
                return LAST_YEAR;
            case 5:
                return LAST_5_YEARS;
        }
        return null;
    }
}
