package com.platform.cdcs.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.platform.cdcs.MyApp;
import com.platform.cdcs.model.InvoiceList;
import com.platform.cdcs.model.ProductList;
import com.trueway.app.uilib.model.ChooseItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holytang on 2017/10/24.
 */
public class CacheTool {

    private static List<ProductList.ProductItem> inputList = null;
    private static List<ProductList.ProductItem> outputList = null;

    private static List<InvoiceList.Invoice> invoiceList = null;

    public static List<ProductList.ProductItem> getInputList(Context context) {
        if (inputList == null) {
            initInput(context);
        }
        return inputList;
    }

    public static List<ProductList.ProductItem> getOutputList(Context context) {
        if (outputList == null) {
            initOutput(context);
        }
        return outputList;
    }

    private static String getInputCache(Context context) {
        SharedPreferences cacheSP = context
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        return cacheSP.getString("input", "[]");
    }

    public static void clearInputCache(Context context) {
        if(context==null){
            context= MyApp.getInstance().getApplication();
        }
        SharedPreferences cacheSP = context
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        cacheSP.edit().remove("input").commit();
        if (inputList != null) {
            inputList.clear();
        }
    }


    private static String getOutputCache(Context context) {
        SharedPreferences cacheSP = context
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        return cacheSP.getString("output", "[]");
    }

    public static void clearOutputCache(Context context) {
        if(context==null){
            context= MyApp.getInstance().getApplication();
        }
        SharedPreferences cacheSP = context
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        cacheSP.edit().remove("output").commit();
        if (outputList != null) {
            outputList.clear();
        }
    }

    public static int getInputCount(Context context) {
        if (inputList == null) {
            initInput(context);
        }
        return inputList.size();
    }

    public static int getOutputCount(Context context) {
        if (outputList == null) {
            initOutput(context);
        }
        return outputList.size();
    }

    private static void initInput(Context context) {
        inputList = new ArrayList<>();
        try {
            inputList.addAll(ProductList.ProductItem.parseList(getInputCache(context)));
        } catch (Exception e) {

        }
    }

    private static void initOutput(Context context) {
        outputList = new ArrayList<>();
        try {
            outputList.addAll(ProductList.ProductItem.parseList(getOutputCache(context)));
        } catch (Exception e) {

        }
    }

    public static ProductList.ProductItem append(Context context, ProductList.ProductItem item, int model) {
        if (model == 0) {
            if (inputList == null) {
                initInput(context);
            }
            if (!inputList.contains(item)) {
                inputList.add(item);
            } else {
                //ProductList.ProductItem subItem = inputList.get(inputList.indexOf(item));
                //TODO 增加数量
//                subItem.setQty(String.valueOf(Float.parseFloat(item.getQty()) + Float.parseFloat(subItem.getQty())));
//                item = subItem;
            }
        } else {
            if (outputList == null) {
                initOutput(context);
            }
            if (!outputList.contains(item)) {
                outputList.add(item);
            } else {
//                item = outputList.get(outputList.indexOf(item));
//                item.setQty(String.valueOf(Integer.parseInt(item.getQty()) + 1));
            }
        }
        return item;
    }

    public static void saveCache(Context context, int model) {
        SharedPreferences cacheSP = context
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        if (model == 0) {
            cacheSP.edit().putString("input", ProductList.ProductItem.ListToString(inputList)).commit();
        } else {
            cacheSP.edit().putString("output", ProductList.ProductItem.ListToString(outputList)).commit();
        }
    }

    public static void changeNum(Context context, int model, String serialNumber, String num) {
        List<ProductList.ProductItem> items = null;
        if (model == 0) {
            items = getInputList(context);
        } else {
            items = getOutputList(context);
        }
        for (ProductList.ProductItem item : items) {
            if (serialNumber.equals(item.getSerialNumber())) {
                item.setNowqty(num);
                break;
            }
        }
    }

    public static void remove(Context context, int model, String serialNumber) {
        List<ProductList.ProductItem> items = null;
        if (model == 0) {
            items = getInputList(context);
        } else {
            items = getOutputList(context);
        }
        for (int i = items.size() - 1; i >= 0; i--) {
            if (serialNumber.equals(items.get(i).getSerialNumber())) {
                items.remove(i);
                break;
            }
        }
    }

    private static void initInvoice(Context context) {
        invoiceList = new ArrayList<>();
        try {
            invoiceList.addAll(InvoiceList.parseList(getOutputCache(context)));
        } catch (Exception e) {
        }
    }

    public static int invoiceCount(Context context) {
        if (invoiceList == null) {
            initInvoice(context);
        }
        return invoiceList.size();
    }

    public static void appendInvoice(Context context, InvoiceList.Invoice item) {
        if (invoiceList == null) {
            initInvoice(context);
        }
        if (!invoiceList.contains(item)) {
            invoiceList.add(item);
        }
    }

    public static void saveInvoiceCache(Context context) {
        SharedPreferences cacheSP = context
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        cacheSP.edit().putString("invoice", InvoiceList.ListToString(invoiceList)).commit();
    }

    public static List<InvoiceList.Invoice> getInvoiceList(Context context) {
        if (invoiceList == null) {
            initInvoice(context);
        }
        return invoiceList;
    }

    public static void clearInvoice(Context context) {
        if(context==null){
            context= MyApp.getInstance().getApplication();
        }
        SharedPreferences cacheSP = context
                .getSharedPreferences(Constant.OA_PREFERENCE,
                        Context.MODE_PRIVATE);
        cacheSP.edit().remove("invoice").commit();
        if (invoiceList != null) {
            invoiceList.clear();
        }
    }

    public static void clearAll(Context context) {
        CacheTool.clearInputCache(context);
        CacheTool.clearOutputCache(context);
        CacheTool.clearInvoice(context);
    }
}
