package com.zd.qiu;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Param {
    public List<List<String>> getListString() {
        List<List<String>> list = new ArrayList<>();
        numList.forEach(listi -> {
//            List<String> listz = new ArrayList<>();
//            for (int i = 0; i <listi.length() ; i++) {
//                if (listi.length() > i) {
//                    listz.add(listi.substring(i, i+1));
//                }
//            }
//            list.add(listz);
            list.add(Arrays.asList(listi.split("")));
        });

        return list;
    }

    public List<List<String>> getListAlias() {
        List<List<String>> list = new ArrayList<>();
        aliasList.forEach(listi -> {

            list.add(Arrays.asList(listi.split("")));
        });
        return list;
    }

    private List<String> numList;
    // 4di5zhong6gao

    private List<String> aliasList;
    //连续

    private List<Integer> include3;
    private List<Integer> include1;
    private List<Integer> include0;
    //断点

    private List<Integer> breakNum;
    //和

    private List<Integer> sum;
    //连号

    private List<Integer> continue3;
    private List<Integer> continue1;
    private List<Integer> continue0;
    //主

    private List<Integer> win;
    private List<Integer> lose;
    private List<Integer> peace;
    //连续高中低

    private List<Integer> numH;
    private List<Integer> numM;
    private List<Integer> numL;
    private Integer down;

}
