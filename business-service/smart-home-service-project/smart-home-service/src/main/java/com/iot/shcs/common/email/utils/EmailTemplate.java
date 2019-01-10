package com.iot.shcs.common.email.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:46 2018/8/10
 * @Modify by:
 */
public class EmailTemplate implements Serializable {

    private List<String> receiveTos;

    private String title;

    private String content;

    public EmailTemplate() {

    }

    public EmailTemplate(List<String> receiveTos, String title, String content) {
        this.receiveTos = receiveTos;
        this.title = title;
        this.content = content;
    }

    public List<String> getReceiveTos() {
        return receiveTos;
    }

    public void setReceiveTos(List<String> receiveTos) {
        this.receiveTos = receiveTos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
