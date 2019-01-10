package com.iot.center.vo;

import java.util.List;

/**
 * Created Time by sheting on 2018/10/26
 * Created by sheting on ${UAER}
 */
public class IndexStr {

       private static final long serialVersionUID = 2025580783894328456L;
       private Long id;

       public Long getId() {
              return id;
       }

       public void setId(Long id) {
              this.id = id;
       }

       private String title;
       private int type;
       private List<InfoList> dataList;

       public String getTitle() {
              return title;
       }

       public void setTitle(String title) {
              this.title = title;
       }

       public int getType() {
              return type;
       }

       public void setType(int type) {
              this.type = type;
       }

      public List<InfoList> getDataList() {
              return dataList;
       }

       public void setDataList(List<InfoList> dataList) {
              this.dataList = dataList;
       }
}
