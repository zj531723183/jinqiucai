package com.zd.qiu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Task implements Callable<List<String>> {

    private List<String> list;
    private int start;
    private int end;

    public Task(List<String> list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
    }

    @Override
    public List<String> call() throws Exception {
        Object obj = null;
        List<String> retList = new ArrayList<String>();
//        for (int i = start; i < end; i++) {
//            obj = list.get(i);
//            //你的处理逻辑
//        }
        Pattern p = Pattern.compile(".*1111.*");

        for (int z = list.size() - 1; z >= 0; z--) {
//            if (Pattern.matches(".*1111.*",list.get(z))) {
//                list.remove(z);
//            }
            if (p.matcher(list.get(z)).matches()) {
                list.remove(z);
            }
        }
        //返回处理结果
        return retList;
    }

    public static void main(String[] args) {
//        Integer[] i2 = {1, 2};
//        Integer[] i = {1, 2};
//        List<List<Integer>> iList = new ArrayList<>();
//        for (int j = 0; j < 9; j++) {
//            iList.add(Arrays.asList(i));
//        }
//        for (int j = 0; j <5; j++) {
//            iList.add(Arrays.asList(i2));
//        }
////        List<String> list = CombinationUtil.getCombination(iList);
//        System.out.println(list);
//        System.out.println(list.subList(0,1));
//        System.out.println(list.subList(5,8));

        int sum = 11111111;

        int coreNum = 4;
        int coreZ = coreNum * 14;
        //TODO 家装14位完整切割
        int dealNum = sum / coreZ;
        coreNum = sum % coreZ == 0 ? coreNum : coreNum + 1;
        System.out.println(coreNum);
        while (coreNum > 0) {
            if (coreNum == 5) {

                System.out.println(dealNum * 14 * (coreNum - 1));
                System.out.println(sum - dealNum * 14 * (coreNum - 1));
            } else {
                System.out.println(dealNum * 14 * (coreNum - 1));
//                System.out.println(dealNum * 14 * (coreNum));
////                threadDeal.dealRemove(finalResult, combination.toString(), countDownLatch, param);
            }
            coreNum = coreNum - 1;
        }


    }
}