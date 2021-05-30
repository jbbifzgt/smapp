package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/16.
 */
public class DistCustomerList extends MockObj {

    private List<Customer> distCustomerList;

    public List<Customer> getDistCustomerList() {
        return distCustomerList;
    }

    public void setDistCustomerList(List<Customer> distCustomerList) {
        this.distCustomerList = distCustomerList;
    }

    public static class Customer {
        private String custCode;
        private String custName;
        private String standardName;
        private String mdmc;
        private String id;

        public String getCustCode() {
            return custCode;
        }

        public void setCustCode(String custCode) {
            this.custCode = custCode;
        }

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getStandardName() {
            return standardName;
        }

        public void setStandardName(String standardName) {
            this.standardName = standardName;
        }

        public String getMdmc() {
            return mdmc;
        }

        public void setMdmc(String mdmc) {
            this.mdmc = mdmc;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
