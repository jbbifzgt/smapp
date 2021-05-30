package com.platform.cdcs.model;

import java.util.List;

/**
 * Created by holytang on 2017/10/16.
 */
public class NoticeList extends MockObj {
    private List<NoticeItem> noticeList;

    public List<NoticeItem> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeItem> noticeList) {
        this.noticeList = noticeList;
    }

    public static class NoticeItem {
        private String fill_date;
        private String id;
        private String msgInfo;
        private int msgType;
        private String send_date;
        private int showType;
        private int status;
        private String statusStr;
        private String topicId;
        private String topicType;
        private String userId;

        public String getFill_date() {
            return fill_date;
        }

        public void setFill_date(String fill_date) {
            this.fill_date = fill_date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMsgInfo() {
            return msgInfo;
        }

        public void setMsgInfo(String msgInfo) {
            this.msgInfo = msgInfo;
        }

        public String getSend_date() {
            return send_date;
        }

        public void setSend_date(String send_date) {
            this.send_date = send_date;
        }

        public int getShowType() {
            return showType;
        }

        public void setShowType(int showType) {
            this.showType = showType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusStr() {
            return statusStr;
        }

        public void setStatusStr(String statusStr) {
            this.statusStr = statusStr;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getTopicType() {
            return topicType;
        }

        public void setTopicType(String topicType) {
            this.topicType = topicType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }
    }
}
