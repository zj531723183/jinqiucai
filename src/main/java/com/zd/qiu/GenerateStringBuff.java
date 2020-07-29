package com.zd.qiu;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合生成StringBuff
 *
 * @Author zj
 */
public class GenerateStringBuff {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String s1 = "013";
        List<String> sLust = new ArrayList<>();
//        List<String> sLust1 = new ArrayList<>();
        StringBuilder finalString = new StringBuilder();
//        m0(sLust, new StringBuilder(), 0, s1, s1, s1, s1, s1, s1, s1, s1, s1, s1, s1, s1, s1, s1);
        m0(finalString, new StringBuilder(), 0,"013", "013", "013", "013", "013", "013", "013", "013", "013", "013", "013", "013", "013", "013");
//        m0(sLust1, new StringBuilder(), 0, s1, s1, s1, s1, s1, s1, s1);
//        List<List<String>> a = new ArrayList<>();
//        a.add(sLust);
//        a.add(sLust1);
//        CombinationUtil.getCombination(a);
        System.out.println("总时间" + (System.currentTimeMillis() - start));

        System.out.println(finalString.length());
        System.out.println(finalString.capacity());

        System.out.println("总时间" + (System.currentTimeMillis() - start));
    }


    private static void m0(List<String> sLust, StringBuilder stringBuffer, int j, String... s) {
        if (j == s.length) {
            return;
        }
        for (int i = 0; i < s[j].length(); i++) {
            StringBuilder stringBufferAfter = new StringBuilder(stringBuffer);
            stringBufferAfter.append(s[j].charAt(i));
            if (j == s.length - 1) {
                sLust.add(stringBufferAfter.toString());
            } else {
                m0(sLust, stringBufferAfter, j + 1, s);
            }
        }
    }

    public static void m0(StringBuilder finalString, StringBuilder stringBuffer, int j, String... s) {
        if (j == s.length) {
            return;
        }
        for (int i = 0; i < s[j].length(); i++) {
            StringBuilder stringBufferAfter = new StringBuilder(stringBuffer);
            stringBufferAfter.append(s[j].charAt(i));
            if (j == s.length - 1) {
                finalString.append(stringBufferAfter);
            } else {
                m0(finalString, stringBufferAfter, j + 1, s);
            }
        }
    }


}
