package com.platform.cdcs.model;

import android.text.TextUtils;

import com.platform.cdcs.tool.PYHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by holytang on 2017/10/16.
 */
public class DistCustomerList extends MockObj {

    private List<Customer> distCustomerList;

    public static LetterGroup parse(JSONArray array) throws JSONException {
        LetterGroup group = new LetterGroup();
        List<LetterGroup.LetterGroupItem> grous = new ArrayList<>();
        Map<String, LetterGroup.LetterGroupItem> map = new HashMap<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            String name = item.getString("distName");
            String firstLetter = null;
            if (name.startsWith("长沙") || name.startsWith("长江")) {
                firstLetter = "c";
            } else {
                firstLetter = name.substring(0, 1);
                firstLetter = PYHelper.getPinYinHeadChar(firstLetter);
                if (48 <= firstLetter.charAt(0) && firstLetter.charAt(0) <= 57) {
                    firstLetter = "#";
                }
            }
            firstLetter = firstLetter.toUpperCase();
            if (!map.containsKey(firstLetter)) {
                LetterGroup.LetterGroupItem g = new LetterGroup.LetterGroupItem();
                g.setTitle(firstLetter);
                g.setSubList(new ArrayList<Customer>());
                map.put(firstLetter, g);
                grous.add(g);
            }
            LetterGroup.LetterGroupItem g = map.get(firstLetter);
            DistCustomerList.Customer customer = new DistCustomerList.Customer();
            customer.setCustCode(item.getString("distCode"));
            customer.setCusType(item.getString("cusType"));
            customer.setCustName(name);
            g.getSubList().add(customer);
        }

        Collections.sort(grous, new Comparator<LetterGroup.LetterGroupItem>() {

            @Override
            public int compare(LetterGroup.LetterGroupItem t, LetterGroup.LetterGroupItem t1) {
                return t.getTitle().compareTo(t1.getTitle());
            }

        });
        group.setGroupList(grous);
        return group;
    }

    public List<Customer> getDistCustomerList() {
        return distCustomerList;
    }

    public void setDistCustomerList(List<Customer> distCustomerList) {
        this.distCustomerList = distCustomerList;
    }

    public LetterGroup getLetterModel() {
        LetterGroup group = new LetterGroup();
        List<LetterGroup.LetterGroupItem> grous = new ArrayList<>();
        Map<String, LetterGroup.LetterGroupItem> map = new HashMap<>();
        for (Customer item : distCustomerList) {
            String firstLetter = item.getStandardName().substring(0, 1);
            if (48 <= firstLetter.charAt(0) && firstLetter.charAt(0) <= 57) {
                firstLetter = "#";
            }
            firstLetter = firstLetter.toUpperCase();
            if (!map.containsKey(firstLetter)) {
                LetterGroup.LetterGroupItem g = new LetterGroup.LetterGroupItem();
                g.setTitle(firstLetter);
                g.setSubList(new ArrayList<Customer>());
                map.put(firstLetter, g);
                grous.add(g);
            }
            LetterGroup.LetterGroupItem g = map.get(firstLetter);
            item.setCustName(item.getCustName().substring(1, item.getCustName().length()));
            g.getSubList().add(item);
        }

        Collections.sort(grous, new Comparator<LetterGroup.LetterGroupItem>() {

            @Override
            public int compare(LetterGroup.LetterGroupItem t, LetterGroup.LetterGroupItem t1) {
                return t.getTitle().compareTo(t1.getTitle());
            }

        });
        group.setGroupList(grous);
        return group;
    }

    public static class Customer {
        private String custCode;
        private String custName;
        private String standardName;
        private String mdmc;
        private String id;
        private String cusType;
        private List<Customer> subs;

        public List<Customer> getSubs() {
            return subs;
        }

        public void setSubs(List<Customer> subs) {
            this.subs = subs;
        }

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
            if (TextUtils.isEmpty(standardName)) {
                standardName = "0";
            }
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

        public String getCusType() {
            return cusType;
        }

        public void setCusType(String cusType) {
            this.cusType = cusType;
        }
    }
}
