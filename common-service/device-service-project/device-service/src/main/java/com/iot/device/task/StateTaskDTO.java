package com.iot.device.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:59 2018/11/8
 * @Modify by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StateTaskDTO {

    private static final Integer DEFAULT_SIZE = 1000;
    //当前页码
    private long currentPage;
    //当前页大小<=1000
    private long currentSize;
    //正在进行消费的页码 只有触发开启消费的时候才会做记录 默认=0 这一次最多可消费 当前页码数
    private long currentConsumerPage = 0;
    //是否正在消费
    private boolean whetherConsumer;
    //消费次数
    private long consumerCount = 0;
    //下一个页码
    private long nextPage;
    //总页码
    private long pages;
    //总数
    private long total;
    //每页大小
    private long pageSize = DEFAULT_SIZE;


    public long getNext() {
        return currentPage + 1;
    }

    public long getPages() {
        if (this.total == 0) {
            return 0;
        }
        this.pages = this.total / this.pageSize;
        if (this.total % this.pageSize != 0) {
            this.pages++;
        }
        return this.pages;
    }

    //可消费的页码数
    public long getCanConsumerPage() {
        return this.currentPage - this.currentConsumerPage;
    }

}
