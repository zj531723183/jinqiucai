package com.zd.qiu;


import java.util.*;



public class CombinationUtil {

    public static List<String> getCombination(List<List<String>> list) {
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
                    indexMap.get(m).index = index;
                }
            }

//            System.out.println(s);
            i++;
        }

//        System.out.println("listInteger: " + listInteger);
        List<String> combinationList = new ArrayList<>();
        listInteger.forEach(integers -> {
            StringBuffer s = new StringBuffer();
            integers.forEach(integer -> {
                s.append(integer);
            });

            combinationList.add(s.toString());


        });
//        System.out.println(String.valueOf(combinationList));
        return combinationList;
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


    public void testComposite() {
//        List<Integer> listA = new ArrayList<Integer>();
//        listA.add(8);
//        listA.add(2);
//        listA.add(3);
//
//        List<Integer> listB = new ArrayList<Integer>();
//        listB.add(4);
//        listB.add(7);
//
//        List<Integer> listC = new ArrayList<Integer>();
//        listC.add(6);
//        listC.add(5);
//        listC.add(1);
//
//        // 这个list可以任意扩展多个
//        List<List<Integer>> list = new ArrayList<List<Integer>>();
//        list.add(listA);    // 3
//        list.add(listB);    // 2
//        list.add(listC);    // 3
//        //list.add(listD);
//        //list.add(listE);
//        //list.add(listF);
//        getCombination(list);

        Integer[] i = {1, 2, 3};

        List<List<Integer>> iList = new ArrayList<>();
        for (int j = 0; j < 14; j++) {
            iList.add(Arrays.asList(i));
        }

//        List<String> list = CombinationUtil.getCombination(iList);

    }

}
