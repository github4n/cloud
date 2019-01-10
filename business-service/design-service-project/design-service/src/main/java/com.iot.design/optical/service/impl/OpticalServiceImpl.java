package com.iot.design.optical.service.impl;

import com.google.common.collect.Maps;
import com.iot.design.optical.constants.Constants;
import com.iot.design.optical.service.OpticalService;
import com.iot.file.api.FileApi;
import com.iot.redis.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpticalServiceImpl implements OpticalService {
    @Autowired
    private FileApi fileApi;

    @Override
    public void readFile(String fileId) {
        int HttpResult; // 服务器返回的状态
        BufferedReader reader = null;
        String temp = null;
        int line = 1;
        int x = 1;
        int y = 1;
        String[] column = null;
        String[] row = null;
        Map<String, Float> result = new HashMap<String, Float>();
        try {
            String fileUrl = this.fileApi.getGetUrl(fileId).getPresignedUrl();

            URL url = new URL(fileUrl); // 创建URL
            URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
            urlconn.connect();
            HttpURLConnection httpconn = (HttpURLConnection) urlconn;
            HttpResult = httpconn.getResponseCode();
            if (HttpResult != HttpURLConnection.HTTP_OK) {
                System.out.print("无法连接到");
            } else {
                int filesize = urlconn.getContentLength(); // 取数据长度
                InputStreamReader isReader = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
                reader = new BufferedReader(isReader);
                while ((temp = reader.readLine()) != null) {
                    if (temp.indexOf("TILT=NONE") != -1) {
                        x = line;
                    }
                    if (line > x && x != 1) {
                        //System.out.println("line"+line+":"+temp);
                        if (line == x + 3) {
                            column = temp.split(" ");
                        }
                        if (line == x + 4) {
                            y = line + 1;
                            row = temp.split(" ");
                        }
                        if (line > x + 4) {
                            String[] data = temp.split(" ");
                            for (int i = 0; i < column.length; i++) {
                                result.put(row[line - y] + "-" + column[i], Float.valueOf(data[i]));
                            }
                        }
                    }

                    line++;
                }
            }

            //System.out.println(JSON.toJSONString(result)); 
            RedisCacheUtil.hashPutAll(Constants.DATA_PREFIX_KEY + fileId, result);
            Map<String, Object> confMap = Maps.newHashMap();
            confMap.put(Constants.DATA_ANGLE_UNIT_KEY, Float.valueOf(90 / (column.length - 1)));
            confMap.put(Constants.DATA_ANGLE_COLUMN_KEY, column);
            confMap.put(Constants.DATA_ANGLE_ROW_KEY, row);
            RedisCacheUtil.hashPutAll(Constants.DATA_CONFIG_KEY + fileId, confMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void addWZZS2Redis(Map<String, Float> wzzsMap) {
        RedisCacheUtil.hashPutAll(Constants.DATA_WZZS, wzzsMap);

    }

}
