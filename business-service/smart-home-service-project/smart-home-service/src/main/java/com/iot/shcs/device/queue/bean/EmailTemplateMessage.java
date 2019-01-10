package com.iot.shcs.device.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class EmailTemplateMessage extends BaseMessageEntity implements Serializable {

    private List<String> receiveTos;

    private String title;

    private String content;
}