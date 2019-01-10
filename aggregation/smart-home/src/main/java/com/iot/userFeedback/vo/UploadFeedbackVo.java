package com.iot.userFeedback.vo;

import java.util.List;

public class UploadFeedbackVo {
    private String feedbackContent;
    private List<String> fileIdList;

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public List<String> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<String> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
