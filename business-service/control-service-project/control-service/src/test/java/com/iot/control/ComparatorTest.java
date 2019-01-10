package com.iot.control;

import com.alibaba.fastjson.JSON;
import com.iot.control.space.domain.Space;
import com.iot.control.space.util.AesSpaceComparator;
import org.assertj.core.util.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:57 2018/8/28
 * @Modify by:
 */
public class ComparatorTest {


    public static void main(String[] args) {

        List<Integer> list = Lists.newArrayList();
        list.add(10);
        list.add(3);
        list.add(2);
        list.add(40);
        list.add(12);
        list.add(1);
        Collections.sort(list, new AesIntegerComparator());

        list.forEach(temp -> {
            System.out.println(temp);
        });


        List<Space> spaceList = com.google.common.collect.Lists.newArrayList();
        Space space1 = new Space();
        space1.setId(225L);

        Space space2 = new Space();
        space2.setId(20000L);

        Space space3 = new Space();
        space3.setId(123545L);

        Space space4 = new Space();
        space4.setId(2500L);


        Space space5 = new Space();
        space5.setId(25L);

        spaceList.add(space1);
        spaceList.add(space2);
        spaceList.add(space3);
        spaceList.add(space4);
        spaceList.add(space5);
        Collections.sort(spaceList, new AesSpaceComparator());
        spaceList.forEach(space -> {
            System.out.println("--->" + JSON.toJSONString(space));
        });


    }

    public static class AesIntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            //下面这么写，结果是 升序
            if (o1 < o2) {
                return -1;
            } else if (o1 > o2) {
                return 0;
            }
            return 1;
        }

    }

    public static class DescIntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            //下面这么写，结果是降序
            if (o1 < o2) {
                return 1;
            } else if (o1 > o2) {
                return -1;
            }
            return 0;
        }

    }
}
