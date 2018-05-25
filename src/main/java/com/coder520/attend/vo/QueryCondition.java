package com.coder520.attend.vo;

import com.coder520.common.page.PageQueryBean;

public class QueryCondition extends PageQueryBean{

    private long userId;

    private String startDate;

    private String endDate;

    private String rangeDate;

    private byte attendStatus;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRangeDate() {
        return rangeDate;
    }

    public void setRangeDate(String rangeDate) {
        this.rangeDate = rangeDate;
    }

    public byte getAttendStatus() {
        return attendStatus;
    }

    public void setAttendStatus(byte attendStatus) {
        this.attendStatus = attendStatus;
    }
}
