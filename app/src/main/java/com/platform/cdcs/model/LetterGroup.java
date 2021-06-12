package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/31.
 */
public class LetterGroup {

    private List<LetterGroupItem> groupList;

    public List<LetterGroupItem> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<LetterGroupItem> groupList) {
        this.groupList = groupList;
    }

    public static class LetterGroupItem {
        private String title;
        private List<DistCustomerList.Customer> subList;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<DistCustomerList.Customer> getSubList() {
            return subList;
        }

        public void setSubList(List<DistCustomerList.Customer> subList) {
            this.subList = subList;
        }
    }
}
