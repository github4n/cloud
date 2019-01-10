package com.iot.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iot.common.constant.SystemConstants;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：分页对象
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月18日 下午4:02:27
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月18日 下午4:02:27
 */
public class Pagenation<T> implements Serializable {

    /**
     * 序列
     */
    private static final long serialVersionUID = 4542617637761955078L;

    /**
     * 当前页
     */
    private int currPage = 1;

    /**
     * 每页大小
     */
    private int pageSize = SystemConstants.PAGE_SIZE;

    /**
     * 总页数
     */
    private int pageTotal;

    /**
     * 总条数
     */
    private int recordTotal = 0;

    /**
     * 前一页
     */
    private int prevPage;

    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 第一页
     */
    private int firstPage = 1;

    /**
     * 最后一页
     */
    private int lastPage;

    /**
     * 每页的内容
     */
    private List<T> list;

    /**
     * 描述：构造函数
     *
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:57:01
     * @since
     */
    public Pagenation() {
        super();
    }

    /**
     * 描述：构造函数
     *
     * @param currPage 当前页
     * @param pageSize 每页大小
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:56:57
     * @since
     */
    public Pagenation(int currPage, int pageSize) {
        super();
        this.currPage = currPage;
        this.pageSize = pageSize;
    }

    /**
     * 描述：构造函数
     *
     * @param paramsMap 条件
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午7:52:27
     * @since
     */
    public Pagenation(Map<String, String> paramsMap) throws BusinessException {
        super();
        if (StringUtil.isBlank(paramsMap.get(SystemConstants.TABLE_NAME))) {
            throw new BusinessException(ExceptionEnum.DB_TABLE_NAME_IS_EMPTY);
        }
        this.currPage = CommonUtil.toInteger(paramsMap.get("search_currPage"), 1);
        this.pageSize = CommonUtil.toInteger(paramsMap.get("search_pageSize"), this.pageSize);
        //paramsMap.put(SystemConstants.PAGE_SQL_STA, "select * from ( select * from ( select temp_.*, rownum rn from (");
        //paramsMap.put(SystemConstants.PAGE_SQL_END, ") temp_ ) temp2_ where temp2_.rn <="+this.currPage*this.pageSize+") where rn>"+(this.currPage-1)*this.pageSize);
        int start = (this.currPage - 1) * this.pageSize;
        paramsMap.put(SystemConstants.PAGE_SQL_STA, "");
        paramsMap.put(SystemConstants.PAGE_SQL_END, " limit " + start + "," + this.pageSize);
    }

    /**
     * 描述：设置总条数,默认为0
     *
     * @param recordTotal
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:50:28
     * @since
     */
    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
        this.calculate();
    }

    /**
     * 描述：设置分页内容
     *
     * @param list 内容
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:50:09
     * @since
     */
    public void setList(List<T> list) {
        this.list = list == null ? (List<T>) new ArrayList<List<T>>() : list;
    }

    /**
     * 描述：设置其他参数
     *
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:53:54
     * @since
     */
    public void calculate() {
        this.pageTotal = this.recordTotal % this.pageSize > 0 ? this.recordTotal / this.pageSize + 1 : this.recordTotal / this.pageSize;
        this.firstPage = 1;
        this.lastPage = this.pageTotal;
        if (this.currPage > 1) {
            this.prevPage = this.currPage - 1;
        } else {
            this.prevPage = this.firstPage;
        }
        if (this.currPage < this.lastPage) {
            this.nextPage = this.currPage + 1;
        } else {
            this.nextPage = this.lastPage;
        }
    }

    public int getCurrPage() {
        return currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public Object getList() {
        return list;
    }

    @Override
    public String toString() {
        return "Pager [currPage=" + currPage + ", pageSize=" + pageSize
                + ", pageTotal=" + pageTotal + ", recordTotal=" + recordTotal
                + ", prevPage=" + prevPage + ", nextPage=" + nextPage
                + ", firstPage=" + firstPage + ", lastPage=" + lastPage
                + ", list=" + list + "]";
    }

}