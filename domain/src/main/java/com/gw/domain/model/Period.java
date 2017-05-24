package com.gw.domain.model;

/**
 * Created by vadym on 22.05.17.
 */

public enum Period {
    NO,
    DAY,
    WEEK,
    MONTH;

    public static Period fromInteger(int x) {
        switch(x) {
            case 0:
                return NO;
            case 1:
                return DAY;
            case 2:
                return WEEK;
            case 3:
                return MONTH;
        }
        return null;
    }
}
