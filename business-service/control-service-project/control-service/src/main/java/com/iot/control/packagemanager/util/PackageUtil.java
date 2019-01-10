package com.iot.control.packagemanager.util;

public class PackageUtil {
    public static <T> com.iot.common.helper.Page<T> copyMybatisPlusPageToPage(com.baomidou.mybatisplus.plugins.Page<T> page,com.iot.common.helper.Page<T> myPage){
        myPage.setTotal(page.getTotal());
        myPage.setPages(page.getPages());
        myPage.setPageSize(page.getSize());
        myPage.setPageNum(page.getCurrent());
        myPage.setStartRow((page.getCurrent() - 1) * page.getSize());
        myPage.setEndRow(myPage.getStartRow()+page.getSize());
        myPage.setResult(page.getRecords());
        return myPage;
    }
}
