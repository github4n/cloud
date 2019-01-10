package com.iot.building.template.vo.req;


import com.iot.building.ifttt.vo.req.SaveIftttReq;

/**
 * 描述：保存联动模板请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/4 10:35
 */
public class SaveIftttTemplateReq extends SaveIftttReq {
//    private Long tempalteId;
    private Long templateIftttId;

    private Long deployMentId;//部署类型id
//    public Long getTempalteId() {
//        return tempalteId;
//    }
//
//    public void setTempalteId(Long tempalteId) {
//        this.tempalteId = tempalteId;
//    }


    public Long getDeployMentId() {
        return deployMentId;
    }

    public void setDeployMentId(Long deployMentId) {
        this.deployMentId = deployMentId;
    }

    public Long getTemplateIftttId() {
        return templateIftttId;
    }

    public void setTemplateIftttId(Long templateIftttId) {
        this.templateIftttId = templateIftttId;
    }
}
