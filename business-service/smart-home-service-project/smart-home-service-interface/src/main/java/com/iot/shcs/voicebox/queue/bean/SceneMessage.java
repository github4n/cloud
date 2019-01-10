package com.iot.shcs.voicebox.queue.bean;

import com.iot.common.mq.bean.BaseMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/15 13:46
 * @Modify by:
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class SceneMessage extends BaseMessageEntity implements Serializable {
    private Long userId;
    private Long sceneId;
}
