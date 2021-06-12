package com.platform.cdcs.tool;

import com.platform.cdcs.MyApp;
import com.trueway.app.uilib.tool.Md5;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by holytang on 2017/9/20.
 */
public class Constant {

    public static final String OA_PREFERENCE = "cdcs";

    public static final String[] SCAN_TITLES = new String[]{"请将发票二维码\n放在如下扫码区域内进行扫", "请将唯一码\n放在如下扫码区域内进行扫描", "请将运单号和箱号\n分别放在如下扫码区域内", "请将GS1码\n放在如下扫码区域内进行扫描"};
    public static final int PAGE_SIZE = 20;

    //        public static final String URL = "http://120.77.75.94/cdcs/";
    public static final String URL = "http://119.23.146.158/cdcs/";

    //登录
    public static final String LOGIN_URL = URL + "login";
    public static final String LOGOUT_URL = URL + "logout";

    //首页
    public static final String NOTICE_LST = URL + "getNoticeLst";

    //产品
    public static final String PRODUCT_LST = URL + "getProductLst";
    public static final String SUBBU_LST = URL + "getSubBULst";
    public static final String SCAN_PRODUCT = URL + "getScanProduct";
    public static final String PRODUCT_COUNT_LIST = URL + "getProductCountList";
    public static final String PRODUCT_COUNTINFO_LIST = URL + "getProductCountInfoList";
    public static final String WARE_HOUSE = URL + "wareHouse";
    public static final String OUT_HOUSE = URL + "outHouse";

    //发票
    public static final String INVOICE_LST = URL + "getInvoiceLst";
    public static final String INVOICE_DETAIL = URL + "getInvoiceDetail";
    public static final String CANCLE_INVOICE_URL = URL + "cancleInvoice";
    public static final String LIST_FOR_INVOICE = URL + "getDocListForInvoice";
    public static final String NO_INVOICE_PRODUCT = URL + "getNoInvoiceProduct";
    public static final String INVOICE_PIC_UPLOAD = URL + "invoicePicUpload";
    public static final String ADD_INVOICE = URL + "addInvoice";
    public static final String ADD_INVOICE_PRODUCT = URL + "addInvoiceProduct";
    public static final String CANCEL_INVOICE_PRODUCT = URL + "cancelInvoiceProduct";

    //新客户申请
    public static final String NEW_CUSTOMER_APPLY_LST = URL + "getNewCustomerApplyLst";
    public static final String ADD_NEW_CUSTOMER_APPLY = URL + "addNewCustomerApply";

    //自定义产品
    public static final String DIST_PRODUCT_LST = URL + "getDistProductLst";
    public static final String DIST_PRODUCT_REG_LST = URL + "getDistProductRegLst";
    public static final String DIST_PRODUCT_PRICE_LST = URL + "getDistProductPriceLst";
    public static final String UPDATE_DIST_PRODUCT = URL + "updateDistProductInfo";

    //我的客户
    public static final String DIST_CUSTOMER_LST = URL + "getDistCustomerLst";
    public static final String CUST_LST = URL + "getCustLst";
    public static final String UPDATE_DIST_CUSTOMER = URL + "updateDistCustomerInfo";

    //库位管理
    public static final String DIST_WHHOUSE_LST = URL + "getDistWhHouseLst";
    public static final String UPDATE_DIST_WHHOUSEINFO = URL + "updateDistWhHouseInfo";

    //库存快照
    public static final String DIST_SYSSTOCK_TAKE_LST = URL + "getDistSysStockTakeLst";
    public static final String DIST_SYSSTOCK_TAKE_LST_BYITEMCODES = URL + "getDistSysStockTakeLstByItemCodes";
    public static final String DIST_ITEMCODESUBBU_LST = URL + "getDistItemCodeSubBuLst";

    //代报申请
    public static final String OTHER_REPORT_LST = URL + "getOtherReportLst";
    public static final String INSERT_OTHERR_EPORT_INFO = URL + "insertOtherReportInfo";

    //我
    public static final String DIST_MDMUD_INFO = URL + "getDistMDMUDInfo";
    public static final String EDIT_DIST_MDMUD_INFO = URL + "editDistMDMUDInfo";
    public static final String DIST_MSGINFO = URL + "getDistMsgInfo";
    public static final String EDIT_DIST_MSGINFO = URL + "editDistMsgInfo";

    //字典
    public static final String DIC_URL = URL + "getSurAndSutList";

    public static final String DIC_XTBM = URL + "getXtbm";

    //经销商列表
    public static final String DIST_LST = URL + "getDistLst";

    public static final String PROVINCE_CITY_LIST = URL + "getProvinceCityList";
    //到货通知
    public static final String DOCUMENT_INFO_LST = URL + "getDocumentInfoLst";

    public static Map<String, String> makeLoginParam(String data) {
        Map<String, String> params = new HashMap<>();
        params.put("parameter", data);
        params.put("chk", Md5.encode(String.format("apptoken=%s&parameter=%s", "dd0a4cd1926afca8a3e195326ad77b24", data)));
        params.put("apptoken", "dd0a4cd1926afca8a3e195326ad77b24");
        return params;
    }

    public static Map<String, String> makeParam(String data) {
        Map<String, String> params = new HashMap<>();
        params.put("parameter", data);
        String token = MyApp.getInstance().getAccount().getToken();
        params.put("chk", Md5.encode(String.format("apptoken=%s&parameter=%s", token, data)));
        params.put("apptoken", token);
        return params;
    }

    public static Map<String, String> makeParam(Map<String, String> map) {
        map.put("sysType", "2");
        return makeParam(new JSONObject(map).toString());
    }

    public static String makeID(String userName) {
        StringBuffer sb = new StringBuffer(userName.replace("MDMUD", ""));
        sb.append("11");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        sb.append(sdf.format(new Date()));
        sb.append((int) (Math.random() * 10) + "" + (int) (Math.random() * 10));
        return sb.toString();
    }
}
