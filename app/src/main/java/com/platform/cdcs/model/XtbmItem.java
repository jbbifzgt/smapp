package com.platform.cdcs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holytang on 2017/10/31.
 */
public class XtbmItem {

    private String xtxx;

    public String getXtxx() {
        return xtxx;
    }

    public void setXtxx(String xtxx) {
        this.xtxx = xtxx;
    }

    public static class XtbmList extends MockObj {
        private List<XtbmItem> xtbmList;

        public List<XtbmItem> getXtbmList() {
            return xtbmList;
        }

        public void setXtbmList(List<XtbmItem> xtbmList) {
            this.xtbmList = xtbmList;
        }

        public String[] toArray() {
            try {
                List<String> array = new ArrayList<>();
                for (XtbmItem item : xtbmList) {
                    if(!item.getXtxx().contains("请选择")){
                        array.add(item.getXtxx());
                    }
                }
                return array.toArray(new String[array.size()]);
            } catch (Exception e) {
                return new String[0];
            }
        }
    }
}
