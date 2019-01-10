package com.iot.common.exception;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：异常接口
 * 创建人： mao2080@sina.com
 * 创建时间：2017年3月21日 下午2:33:41
 * 修改人： mao2080@sina.com
 * 修改时间：2017年3月21日 下午2:33:41
 */
public interface IBusinessException {

    /**
     * 描述：获取异常代码
     *
     * @return int异常代码
     * @author mao2080@sina.com
     * @created 2017年3月21日 下午2:33:46
     * @since
     */
    public int getCode();

    /**
     * 描述：获取异常描述
     *
     * @return String异常描述
     * @author mao2080@sina.com
     * @created 2017年3月21日 下午2:33:50
     * @since
     */
    public String getMessageKey();

}
