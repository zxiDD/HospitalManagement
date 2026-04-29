package com.cg.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ErrorMessageDto {

    private String errMsg;
    private LocalDateTime timeStamp;
    private String status;
    private Map<String, List<String>> errMap;

    public ErrorMessageDto() {}

    public ErrorMessageDto(String errMsg, LocalDateTime timeStamp, String status) {
        this.errMsg = errMsg;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public ErrorMessageDto(String errMsg, LocalDateTime timeStamp, String status,
                           Map<String, List<String>> errMap) {
        this.errMsg = errMsg;
        this.timeStamp = timeStamp;
        this.status = status;
        this.errMap = errMap;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, List<String>> getErrMap() {
        return errMap;
    }

    public void setErrMap(Map<String, List<String>> errMap) {
        this.errMap = errMap;
    }
}