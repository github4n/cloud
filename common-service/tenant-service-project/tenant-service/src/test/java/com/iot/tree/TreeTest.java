package com.iot.tree;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
public class TreeTest {

    /**
     * 初始化数据
     */
    @Test
    public void initData() {
        List<Org> list = Lists.newArrayList();

        //1
        list = addOrg(0, "1", list, 1); //1
        //1-1
        list = addOrg(1, "1-1", list, 1);  //1-2
        //1-1-1
        list = addOrg(2, "1-1-1", list, 1); //1-2-3
        //2
        list = addOrg(0, "2", list, 2); //4
        //2-1
        list = addOrg(4, "2-1", list, 1); //4-5
        //2-2
        list = addOrg(4, "2-2", list, 1); //4-6

        /*log.info("排序前：" + list.toString());
        for (Org org : list) {
            log.info(org.getPath());
        }*/
        //排序
        Collections.sort(list, new Comparator<Org>() {
            /*
             * int compare(Person p1, Person p2) 返回一个基本类型的整型，
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            public int compare(Org p1, Org p2) {
                if (p1.getPath().compareTo(p2.getPath()) > 0) {
                    return 1;
                }
                if (p1.getPath().compareTo(p2.getPath()) == 0) {
                    return 0;
                }
                return -1;
            }
        });

        /*log.info("排序后：" + list.toString());
        for (Org org : list) {
            log.info(org.getPath());
        }*/

        //添加子节点 封装树
        List<Item> root = Lists.newArrayList();
        Map<Long, Item> tempMap = Maps.newHashMap();
        List<Item> children;
        for (Org org : list) {
            children = Lists.newArrayList();
            Item item = new Item();
            BeanUtils.copyProperties(org, item);
            item.setChildren(children);
            tempMap.put(item.getId(), item);
            Item parent = tempMap.get(item.getParentId());
            if (parent != null) {
                parent.getChildren().add(item);
            } else {
                root.add(item);
            }
        }

        Comparator<Item> comparator = new Comparator<Item>() {
            public int compare(Item p1, Item p2) {
                if (p1.getOrder() > p2.getOrder()) {
                    return 1;
                }
                if (p1.getOrder() == p2.getOrder()) {
                    //若sort字段相同的话，根据创建时间来进行排序
                    Date d1 = p1.getCreateTime();
                    Date d2 = p2.getCreateTime();
                    if (null == d1 || null == d2) return 0;
                    return d1.compareTo(d2);
                }
                return -1;
            }
        };

        //排序
        //Collections.sort(root, comparator);
        sortItem(root, comparator);

        //展示
        //log.info("结果：" + JSON.toJSONString(root));
        for (Item item : root) {
            log.info(item.getName());
            getChildren(item.getChildren());
        }
    }

    public void sortItem(List<Item> list, Comparator<Item> comparator) {
        if (!CollectionUtils.isEmpty(list)) {
            Collections.sort(list, comparator);
            for (Item item : list) {
                sortItem(item.getChildren(), comparator);
            }
        }
    }

    public void getChildren(List<Item> children) {
        if (!CollectionUtils.isEmpty(children)) {
            for (Item item : children) {
                log.info(item.getName());
                getChildren(item.getChildren());
            }
        }
    }

    public List<Org> addOrg(Integer parentId, String name, List<Org> list, int order) {
        Integer id = list.size() + 1;
        Map<Integer, Org> map = getOrgMap(list);
        Org parentOrg = map.get(parentId);
        String path = id.toString() + "-";
        if (parentOrg != null) {
            //不是一级组织
            String parentPath = parentOrg.getPath();
            path = parentPath + id + "-";
        }
        Org org = new Org();
        org.setId(id.longValue());
        org.setName(name);
        org.setDesc(name);
        org.setOrder(order);
        org.setParentId(parentId.longValue());
        org.setTenantId(1l);
        org.setType(1);
        org.setPath(path);
        org.setCreateTime(new Date());
        list.add(org);
        return list;
    }

    public Map<Integer, Org> getOrgMap(List<Org> list) {
        Map<Integer, Org> map = Maps.newHashMap();
        if (list.size() > 0) {
            for (Org org : list) {
                map.put(org.getId().intValue(), org);
            }
        }
        return map;
    }
}
