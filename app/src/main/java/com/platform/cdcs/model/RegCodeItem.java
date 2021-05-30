package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/17.
 */
public class RegCodeItem {

    private String batchNumber;
    private String expirationDate;
    private String updateDate;
    private String fillDate;
    private String registrationCode;
    private String updateOperName;

    public String getUpdateOperName() {
        return updateOperName;
    }

    public void setUpdateOperName(String updateOperName) {
        this.updateOperName = updateOperName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getFillDate() {
        return fillDate;
    }

    public void setFillDate(String fillDate) {
        this.fillDate = fillDate;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public static class RegCodeList extends MockObj {
        private List<RegCodeItem> regList;

        public List<RegCodeItem> getRegList() {
            return regList;
        }

        public void setRegList(List<RegCodeItem> regList) {
            this.regList = regList;
        }
    }
}
