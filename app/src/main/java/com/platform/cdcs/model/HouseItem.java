package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/16.
 */
public class HouseItem {

    private String whName;
    private String whCode;
    private String whAddress;
    private String isMainHouse;
    private String id;

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getWhAddress() {
        return whAddress;
    }

    public void setWhAddress(String whAddress) {
        this.whAddress = whAddress;
    }

    public String getIsMainHouse() {
        return isMainHouse;
    }

    public void setIsMainHouse(String isMainHouse) {
        this.isMainHouse = isMainHouse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class HouseList extends MockObj{
        private List<HouseItem> houseList;

        public List<HouseItem> getHouseList() {
            return houseList;
        }

        public void setHouseList(List<HouseItem> houseList) {
            this.houseList = houseList;
        }
    }
}
