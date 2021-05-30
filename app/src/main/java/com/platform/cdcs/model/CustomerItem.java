package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/12.
 */
public class CustomerItem {

    private String cusName;
    private String cusCode;
    private String status;

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class CusList extends MockObj {
        public List<CustomerItem> cusList;

        public List<CustomerItem> getCusList() {
            return cusList;
        }

        public void setCusList(List<CustomerItem> cusList) {
            this.cusList = cusList;
        }
    }
}
