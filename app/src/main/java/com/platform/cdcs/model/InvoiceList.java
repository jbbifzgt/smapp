package com.platform.cdcs.model;

import android.text.TextUtils;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by holytang on 2017/10/11.
 */
public class InvoiceList {

    private String code;
    private String msg;
    private List<Invoice> invoiceList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Invoice {
        private String code;
        private String msg;
        private String invoiceId;
        private String inNO;
        private String docType;
        private String inDate;
        private String distCode;
        private String unRegDistName;
        private int havePic;
        private String inCode;
        private String cusName;
        private String cusCode;
        private String inRemark;
        private String nonetaxTotal;
        private String taxTotal;
        private List<PicModel> picList;
        //发货方
        private String distName;
        private String tax;
        private String number;
        private String status;

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getInNO() {
            return inNO;
        }

        public void setInNO(String inNO) {
            this.inNO = inNO;
        }

        public String getDistName() {
            return distName;
        }

        public void setDistName(String distName) {
            this.distName = distName;
        }

        public String getDocType() {
            return docType;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public String getInDate() {
            return inDate;
        }

        public void setInDate(String inDate) {
            this.inDate = inDate;
        }

        public String getDistCode() {
            return distCode;
        }

        public void setDistCode(String distCode) {
            this.distCode = distCode;
        }

        public String getUnRegDistName() {
            return unRegDistName;
        }

        public void setUnRegDistName(String unRegDistName) {
            this.unRegDistName = unRegDistName;
        }

        public int getHavePic() {
            return havePic;
        }

        public void setHavePic(int havePic) {
            this.havePic = havePic;
        }

        public String getInCode() {
            return inCode;
        }

        public void setInCode(String inCode) {
            this.inCode = inCode;
        }

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getInRemark() {
            if(TextUtils.isEmpty(inRemark)){
                return "";
            }
            return inRemark;
        }

        public void setInRemark(String inRemark) {
            this.inRemark = inRemark;
        }

        public String getNonetaxTotal() {
            return nonetaxTotal;
        }

        public void setNonetaxTotal(String nonetaxTotal) {
            this.nonetaxTotal = nonetaxTotal;
        }

        public String getTaxTotal() {
            return taxTotal;
        }

        public void setTaxTotal(String taxTotal) {
            this.taxTotal = taxTotal;
        }

        public List<PicModel> getPicList() {
            return picList;
        }

        public void setPicList(List<PicModel> picList) {
            this.picList = picList;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


}
