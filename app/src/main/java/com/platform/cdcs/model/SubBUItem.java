package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/19.
 */
public class SubBUItem {

    private String subBU;
    private String localSubBUCode;

    public String getSubBU() {
        return subBU;
    }

    public void setSubBU(String subBU) {
        this.subBU = subBU;
    }

    public String getLocalSubBUCode() {
        return localSubBUCode;
    }

    public void setLocalSubBUCode(String localSubBUCode) {
        this.localSubBUCode = localSubBUCode;
    }

    public static class SubBUList extends MockObj {

        private List<SubBUItem> objList;

        public List<SubBUItem> getObjList() {
            return objList;
        }

        public void setObjList(List<SubBUItem> objList) {
            this.objList = objList;
        }
    }

}
