package com.gw.domain.model.decision;

import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;


public class SellAsset extends Decision {

    private AssetType mAssetType;

    public AssetType getAssetType() {
        return mAssetType;
    }

    public void setAssetType(AssetType assetType) {
        this.mAssetType = assetType;
    }

    @Override
    public TreeMap<Date, Double> getPaymentMap() {
        TreeMap<Date, Double> map = new TreeMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        map.put(calendar.getTime(), getAmount());
        return map;
    }

    public enum AssetType {
        MOVABLES,
        REALTY,
        SECURITIES,
        HOUSEHOLD_APPLIANCES;

        public static SellAsset.AssetType fromInteger(int x) {
            switch(x) {
                case 0:
                    return MOVABLES;
                case 1:
                    return REALTY;
                case 2:
                    return SECURITIES;
                case 3:
                    return HOUSEHOLD_APPLIANCES;
            }
            return null;
        }

    }
}