package com.zd.qiu;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;


@Service
public class CombinationService {

    @Async("asyncServiceExecutor")
    public void getCombination(List<List<String>> list, List<String> listFinalResult, CountDownLatch countDownLatch) {
        try {
            int iterateSize = 1;// 总迭代次数，即组合总种数

            for (int i = 0; i < list.size(); i++) {
                // 每个List的n选1选法种数
                iterateSize *= list.get(i).size();
            }

            int median = 1; // 当前元素与左边已定元素的组合种数
            Map<Integer, Sign> indexMap = new HashMap<Integer, Sign>();
            for (int i = 0; i < list.size(); i++) {
                median *= list.get(i).size();
                Sign sign = new Sign();
                sign.index = 0;
                sign.whenChg = iterateSize / median;
                indexMap.put(i, sign);
            }

            System.out.println("条目总数: " + iterateSize);
            List<List<String>> listInteger = new ArrayList<>();

            int i = 1;  // 组合编号


            while (i <= iterateSize) {
//            String s = "i: " + i + "\t";
                List<String> integerList = new ArrayList<>();
                listInteger.add(integerList);
                // m值可变
                for (int m = 0; m < list.size(); m++) {
                    int whenChg = indexMap.get(m).whenChg;  // 组元素更换频率
                    int index = indexMap.get(m).index;      // 组元素索引位置

//                s += list.get(m).get(index);
                    integerList.add(list.get(m).get(index));
                    if (i % whenChg == 0) {
                        index++;
                        // 该组中的元素组合完了，按照元素索引顺序重新取出再组合
                        if (index >= list.get(m).size()) {
                            index = 0;
                        }
                        Collections.sort(integerList);
                        indexMap.get(m).index = index;
                    }
                }

//            System.out.println(s);
                i++;
            }

//        System.out.println("listInteger: " + listInteger);
//            List<String> combinationList = new ArrayList<>();
            listInteger.forEach(integers -> {
                StringBuffer s = new StringBuffer();
                integers.forEach(integer -> {
                    s.append(integer);
                });

                listFinalResult.add(s.toString());
            });
//        System.out.println(String.valueOf(combinationList));
//            return combinationList;
        } catch (Exception e) {
        } finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }

    /**
     * 组合记号辅助类
     */
    private static class Sign {
        /**
         * 每组元素更换频率，即迭代多少次换下一个元素
         */
        public int whenChg;
        /**
         * 每组元素的元素索引位置
         */
        public int index;
    }


}
