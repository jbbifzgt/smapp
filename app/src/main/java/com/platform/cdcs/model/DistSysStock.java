package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/17.
 */
public class DistSysStock {

    private String stockTakeDate;
    private String subBu;
    private String id;

    public String getStockTakeDate() {
        return stockTakeDate;
    }

    public void setStockTakeDate(String stockTakeDate) {
        this.stockTakeDate = stockTakeDate;
    }

    public String getSubBu() {
        return subBu;
    }

    public void setSubBu(String subBu) {
        this.subBu = subBu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class DistSysStockTakeList extends MockObj{
        private List<DistSysStock> distSysStockTakeList;

        public List<DistSysStock> getDistSysStockTakeList() {
            return distSysStockTakeList;
        }

        public void setDistSysStockTakeList(List<DistSysStock> distSysStockTakeList) {
            this.distSysStockTakeList = distSysStockTakeList;
        }
    }
}
