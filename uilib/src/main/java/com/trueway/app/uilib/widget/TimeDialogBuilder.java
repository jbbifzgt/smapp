package com.trueway.app.uilib.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;


import com.trueway.app.uilib.R;
import com.trueway.app.uilib.tool.Utils;

import java.util.Calendar;

/**
 * @author 汤海泉
 * @date 2013-3-19
 */
public class TimeDialogBuilder {

    private static final String TIME_FORMAT = "%s-%s-%s %s:%s";
    private static final String DATE_FORMAT = "%s-%s-%s";
    private final Dialog mD;
    private TimePickCallback callback;
    private DatePicker datePick;
    private TimePicker timePick;

    public TimeDialogBuilder(final Context mContext) {
        mD = new Dialog(mContext, R.style.IgDialog);
        this.mD.setContentView(R.layout.dialog_date_time);
        datePick = (DatePicker) this.mD.findViewById(R.id.datePicker);
        datePick.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        timePick = (TimePicker) this.mD.findViewById(R.id.timePicker);
        timePick.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePick.setIs24HourView(true);
        this.mD.findViewById(R.id.button2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mD.dismiss();
                    }
                });

        this.mD.findViewById(R.id.button1).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (timePick.getVisibility() == View.GONE) {
                            if (callback != null) {
                                callback.setTime(String.format(DATE_FORMAT,
                                        datePick.getYear(),
                                        convertInt(datePick.getMonth() + 1),
                                        convertInt(datePick.getDayOfMonth())));
                            }
                            mD.dismiss();
                            return;
                        }
                        String time = String.format(TIME_FORMAT,
                                datePick.getYear(),
                                convertInt(datePick.getMonth() + 1),
                                convertInt(datePick.getDayOfMonth()),
                                convertInt(timePick.getCurrentHour()),
                                convertInt(timePick.getCurrentMinute()));
//                        if (time.compareTo(Utils.currentTime()) < 0) {
//                            Toast.makeText(mContext, "时间必须大于当前时间，请重新输入!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (callback != null) {
                            callback.setTime(time);
                        }
                        mD.dismiss();
                    }
                });
    }

    public TimeDialogBuilder showDate() {
        timePick.setVisibility(View.GONE);
        return this;
    }

    public TimeDialogBuilder showTime() {
        datePick.setVisibility(View.GONE);
        return this;
    }

    private String convertInt(int param) {
        if (param < 10) {
            return "0" + param;
        }
        return param + "";
    }

    public Dialog create() {
        return this.mD;
    }

    public TimeDialogBuilder setCancelable(boolean flag) {
        mD.setCancelable(flag);
        return this;
    }

    public TimeDialogBuilder setCallback(TimePickCallback callback) {
        this.callback = callback;
        return this;
    }

    public TimeDialogBuilder setTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            Calendar cal = Calendar.getInstance();
            long lTime = Utils.getDateTime(time + ":00");
            cal.setTimeInMillis(lTime);
            datePick.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH), null);
            timePick.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePick.setCurrentMinute(cal.get(Calendar.MINUTE));
            timePick.setIs24HourView(true);
        }
        return this;
    }

    public interface TimePickCallback {
        public void setTime(String time);
    }

}
