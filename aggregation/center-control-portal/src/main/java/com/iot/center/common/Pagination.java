package com.iot.center.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：常用工具
 * 功能描述：分页对象
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月18日 下午4:02:27
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月18日 下午4:02:27
 */
public class Pagination implements Serializable {

    /**序列*/
    private static final long serialVersionUID = 4542617637761955078L;

    /**当前页*/
    private int currPage = 1;
    
    /**每页大小*/
    private int pageSize = 10;
    
    /**总页数*/
    private int pageTotal;
    
    /**总条数*/
    private int recordTotal = 0;
    
    /**前一页*/
    private int prevPage;
    
    /**下一页*/
    private int nextPage;
    
    /**第一页*/
    private int firstPage = 1;
    
    /**最后一页*/
    private int lastPage;
    
    /**每页的内容*/
    private Object list;
    
    /**
     * 
     * 描述：构造函数
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:57:01
     * @since
     */
    public Pagination() {
		super();
	}

    /**
     * 
     * 描述：构造函数
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:56:57
     * @since 
     * @param currentPage 当前页
     * @param pageSize 每页大小
     */
	public Pagination(int currPage, int pageSize) {
		super();
		this.currPage = currPage;
		this.pageSize = pageSize;
	}
	
	/**
	 * 
	 * 构建函数
	 * @author wujianlong
	 * @created 2017年11月13日 下午2:30:09
	 * @since 
	 * @param params
	 */
	public Pagination(Map<String,String> params) {
		this.currPage = params.get("currPage")==null?this.currPage:Integer.parseInt(params.get("currPage"));
		this.pageSize = params.get("pageSize")==null?this.pageSize:Integer.parseInt(params.get("pageSize"));
	}
	

    /**
     * 
     * 描述：设置总条数,默认为0
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:50:28
     * @since 
     * @param recordTotal
     */
    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
        this.calculate();
    }

    /**
     * 
     * 描述：设置分页内容
     * @author mao2080@sina.com
     * @created 2017年4月18日 下午3:50:09
     * @since 
     * @param list 内容
     */
    public void setList(Object list) {
        this.list = list == null?new ArrayList<Object>():list;
    }

    /**
     * 
     * 描述：设置其他参数
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