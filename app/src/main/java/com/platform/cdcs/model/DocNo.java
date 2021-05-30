package com.platform.cdcs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by holytang on 2017/10/12.
 */
public class DocNo extends MockObj implements Serializable{

    private ArrayList<DocNo.DocItem> docNoList;

    public ArrayList<DocNo.DocItem> getDocNoList() {
        return docNoList;
    }

    public void setDocNoList(ArrayList<DocNo.DocItem> docNoList) {
        this.docNoList = docNoList;
    }

    public static class DocItem implements Serializable {
        private String docNo;
        private String docDate;
        private int position;
        private boolean check;

        public String getDocNo() {
            return docNo;
        }

        public void setDocNo(String docNo) {
            this.docNo = docNo;
        }

        public String getDocDate() {
            return docDate;
        }

        public void setDocDate(String docDate) {
            this.docDate = docDate;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
