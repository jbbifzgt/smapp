package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/16.
 */
public class DisProductList extends MockObj {

    private List<DisProduct> disProductList;

    public List<DisProduct> getDisProductList() {
        return disProductList;
    }

    public void setDisProductList(List<DisProduct> disProductList) {
        this.disProductList = disProductList;
    }

    public static class DisProduct {
        private String materialStCode;
        private String materialCNDesc;
        private String id;

        public String getMaterialStCode() {
            return materialStCode;
        }

        public void setMaterialStCode(String materialStCode) {
            this.materialStCode = materialStCode;
        }

        public String getMaterialCNDesc() {
            return materialCNDesc;
        }

        public void setMaterialCNDesc(String materialCNDesc) {
            this.materialCNDesc = materialCNDesc;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
