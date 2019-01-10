package com.iot.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：CSV文件导入导出
 * 创建人： mao2080@sina.com
 * 创建时间：2017年8月26日 下午2:38:42
 * 修改人： mao2080@sina.com
 * 修改时间：2017年8月26日 下午2:38:42
 */
public class CSVUtils {

    /**
     * 描述：导出
     *
     * @param file     csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据（data1,data2,data3...）
     * @return
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:39:13
     * @since
     */
    public static boolean exportCsv(File file, List<String> dataList) {
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bfw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out, "gbk");
            bfw = new BufferedWriter(osw);
            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    bfw.append(data).append("\r\n");
                }
            }
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtil.closeQuietly(bfw, osw, out);
        }
    }

    /**
     * 描述：导入
     *
     * @param file csv文件(路径+文件名)
     * @return
     * @author mao2080@sina.com
     * @created 2017年8月26日 下午2:42:08
     * @since
     */
    public static List<String> importCsv(File file) {
        List<String> dataList = new ArrayList<String>();
        BufferedReader br = null;
        try {
            FileInputStream in = new FileInputStream(file);
            InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(inReader);
            String line = br.readLine();
            while (StringUtils.isNotBlank(line)) {
                dataList.add(line);
                line = br.readLine();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(br);
        }
        return dataList;
    }
}