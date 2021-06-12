package com.platform.cdcs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by holytang on 2017/10/31.
 */
public class ProvinceCity {
    private String province;
    private String city;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static class ProvinceCityList extends MockObj {
        private List<ProvinceCity> provinceCityList;
        private List<String> proList;

        public List<ProvinceCity> getProvinceCityList() {
            return provinceCityList;
        }

        public void setProvinceCityList(List<ProvinceCity> provinceCityList) {
            this.provinceCityList = provinceCityList;
        }

        public List<String> getProList() {
            return proList;
        }

        public void setProList(List<String> proList) {
            this.proList = proList;
        }

        public Map<String, List<String>> proMap() {
            Map<String, List<String>> map = new HashMap<>();
            proList = new ArrayList<>();
            for (ProvinceCity item : provinceCityList) {
                if (!map.containsKey(item.getProvince())) {
                    proList.add(item.getProvince());
                    map.put(item.getProvince(), new ArrayList<String>());
                }
                if (!item.getCity().contains("请选择")) {
                    map.get(item.getProvince()).add(item.getCity());
                }
            }
            return map;
        }
    }
}
