package com.platform.cdcs.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by holytang on 2017/10/12.
 */
public class ProductList extends MockObj {

    private List<ProductItem> disProductList;

    public List<ProductItem> getProductList() {
        return disProductList;
    }

    public void setProductList(List<ProductItem> disProductList) {
        this.disProductList = disProductList;
    }

    public static class ProductItem implements Serializable {

        private String id;
        //数量
        private String qty;
        //单位
        private String uom;
        //产品代码
        private String itemCode;
        //产品名称
        private String itemName;

        private String serialNumber;
        private String materialName;
        private String materialNumber;
        private String saleUnit;
        private String productNum;
        private String subBU;
        private String defaultUnit;
        private boolean choose;
        //当前数量
        private String nowqty;

        public static List<ProductItem> parseList(String array) throws JSONException {
            List<ProductItem> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(array);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                ProductItem item = new ProductItem();
                item.setItemName(obj.getString("name"));
                item.setSerialNumber(obj.getString("code"));
                item.setMaterialNumber(obj.getString("number"));
                item.setSubBU(obj.getString("subBU"));
                item.setUom(obj.getString("unit"));
                item.setQty(obj.getString("num"));
                item.setNowqty(obj.getString("nowqty"));

                list.add(item);
            }
            return list;
        }

        public static String ListToString(List<ProductItem> list) {
            JSONArray array = new JSONArray();
            for (ProductItem item : list) {
                try {
                    array.put(item.toJSON());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return array.toString();
        }

        public String getNowqty() {
            return nowqty;
        }

        public void setNowqty(String nowqty) {
            this.nowqty = nowqty;
        }

        public boolean isChoose() {
            return choose;
        }

        public void setChoose(boolean choose) {
            this.choose = choose;
        }

        public String getDefaultUnit() {
            return defaultUnit;
        }

        public void setDefaultUnit(String defaultUnit) {
            this.defaultUnit = defaultUnit;
        }

        public String getSubBU() {
            return subBU;
        }

        public void setSubBU(String subBU) {
            this.subBU = subBU;
        }

        public String getSerialNumber() {
            if (serialNumber == null) {
                serialNumber = "";
            }
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public String getMaterialNumber() {
            return materialNumber;
        }

        public void setMaterialNumber(String materialNumber) {
            this.materialNumber = materialNumber;
        }

        public String getSaleUnit() {
            return saleUnit;
        }

        public void setSaleUnit(String saleUnit) {
            this.saleUnit = saleUnit;
        }

        public String getProductNum() {
            if (TextUtils.isEmpty(productNum)) {
                productNum = "0";
            }
            return productNum;
        }

        public void setProductNum(String productNum) {
            this.productNum = productNum;
        }

        public String getQty() {
            if (TextUtils.isEmpty(qty)) {
                qty = "0";
            }
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public JSONObject toJSON() throws JSONException {
            JSONObject object = new JSONObject();
            object.put("name", itemName);
            //序列号
            object.put("code", serialNumber);
            //批号
            object.put("number", materialNumber);
            object.put("subBU", subBU);
            object.put("unit", uom);
            object.put("num", qty);
            object.put("nowqty", 0);
            return object;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;
            if (this == o)
                return true;
            if (getClass() != o.getClass())
                return false;
            ProductItem other = (ProductItem) o;
            return other.getSerialNumber().equals(getSerialNumber());
        }
    }
}
