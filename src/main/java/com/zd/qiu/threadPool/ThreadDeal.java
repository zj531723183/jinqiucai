package com.zd.qiu.threadPool;


import com.zd.qiu.FixdLengthString;
import com.zd.qiu.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * @Description: 质维码分配记录
 * @Author: zj
 * @Date: 2020-07-04
 * @Version: V1.0
 */


@Service
public class ThreadDeal {


    public static final String THREE = "3";
    public static final String ONE = "1";
    public static final String ZERO = "0";
    public static final String EMPTY = "";


    @Async("asyncServiceExecutor")
    public void dealRemove( List<String> result,List<String> listBefore, CountDownLatch countDownLatch, Param param) {
        try {
            Iterator<String> it = listBefore.iterator();
            while (it.hasNext()) {
                String s = it.next();
                if (!include(s, param.getInclude3(), THREE)) {
                    continue;
                }
                if (!include(s, param.getInclude1(), ONE)) {
                    continue;
                }
                if (!include(s, param.getInclude0(), ZERO)) {
                    continue;
                }
                if (!continueNum(s, param.getContinue3(), THREE)) {
                    continue;
                }
                if (!continueNum(s, param.getContinue1(), ONE)) {
                    continue;
                }
                if (!continueNum(s, param.getContinue0(), ZERO)) {
                    continue;
                }
                if (!continueNum(s, param.getWin(), THREE)) {
                    continue;
                }
                if (!continueNum(s, param.getPeace(), ONE)) {
                    continue;
                }
                if (!continueNum(s, param.getLose(), ZERO)) {
                    continue;
                }
                if (!sumAndBreak(s, param.getSum(), param.getBreakNum(), param)) {
                    continue;
                }
                result.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async("asyncServiceExecutor")
    public void dealRemove(StringBuffer finalString, String combinationPart, CountDownLatch countDownLatch, Param param) {
        try {
            int i = combinationPart.length() / 14;

            while (i > 0) {
                String s = combinationPart.substring((i - 1) * 14, i * 14);

                i--;
                if (!include(s, param.getInclude3(), THREE)) {
                    continue;
                }
                if (!include(s, param.getInclude1(), ONE)) {
                    continue;
                }
                if (!include(s, param.getInclude0(), ZERO)) {
                    continue;
                }
                if (!continueNum(s, param.getContinue3(), THREE)) {
                    continue;
                }
                if (!continueNum(s, param.getContinue1(), ONE)) {
                    continue;
                }
                if (!continueNum(s, param.getContinue0(), ZERO)) {
                    continue;
                }
                if (!continueNum(s, param.getWin(), THREE)) {
                    continue;
                }
                if (!continueNum(s, param.getPeace(), ONE)) {
                    continue;
                }
                if (!continueNum(s, param.getLose(), ZERO)) {
                    continue;
                }
                if (!sumAndBreak(s, param.getSum(), param.getBreakNum(), param)) {
                    continue;
                }

                finalString.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    private static boolean include(String s, List<Integer> include, String num) {
        if (!checkNotBlock(include)) {
            return true;

        }
        int length = 14 - s.replace(num, EMPTY).length();
        return length >= include.get(0) && length <= include.get(1);
    }

    private static boolean continueNum(String s, List<Integer> continueNum, String num) {
        if (!checkNotBlock(continueNum)) {
            return true;
        }
        if (continueNum.size() == 1) {
            return s.contains(FixdLengthString.generateZeroString(continueNum.get(0), num));
        } else if (continueNum.size() == 2) {
            boolean b1 = s.contains(FixdLengthString.generateZeroString(continueNum.get(0), num));
            if (continueNum.get(1) != 14) {
                boolean b2 = s.contains(FixdLengthString.generateZeroString(continueNum.get(1) + 1, num));
                return b1 && !b2;
            } else {
                return b1;
            }
        }
        return true;
    }

    private static boolean sumAndBreak(String s, List<Integer> sum, List<Integer> breakNum, Param param) {
        if (!checkNotBlock(sum) && !checkNotBlock(breakNum)
                && !checkNotBlock(param.getNumH()) && !checkNotBlock(param.getNumM()) && !checkNotBlock(param.getNumL())) {
            return true;

        }
        char char3 = 51;
        char char1 = 49;
        int sumReal = 0;
        int breakReal = 0;
        int highNum = 0;
        int midNum = 0;
        int lowNum = 0;
        List<List<String>> alias = param.getListAlias();
        for (int i = 0; i < s.length() - 1; i++) {
            char c = s.charAt(i);

            //breakNum MAX
            if (checkNotBlock(breakNum)) {
                if (s.charAt(i) != s.charAt(i + 1)) {
                    breakReal = breakReal + 1;
                    if (breakNum.size() > 1) {
                        if (breakReal > breakNum.get(1)) {
                            return false;
                        }
                    }
                }
            }

            //sum MAX
            if (checkNotBlock(sum)) {
                if (char1 == c) {
                    sumReal = sumReal + 1;
                    if (sum.size() > 1) {
                        if (sumReal > sum.get(1)) {
                            return false;
                        }
                    }
                } else if (char3 == c) {
                    sumReal = sumReal + 3;
                    if (sum.size() > 1) {
                        if (sumReal > sum.get(1)) {
                            return false;
                        }
                    }

                }
            }

//h-M-M
            if (checkNotBlock(param.getNumH()) ||
                    checkNotBlock(param.getNumM()) ||
                    checkNotBlock(param.getNumL())) {

                List<String> aliasOne = alias.get(i);
                if (c == '0') {
                    if (aliasOne.contains("1")) {
                        if (checkNotBlock(param.getNumL())) {
                            lowNum = lowNum + 1;
                            if (checkSizeEq2(param.getNumL())) {
                                if (lowNum > param.getNumL().get(1)) {
                                    return false;
                                }
                            }
                        }
                    } else if (aliasOne.contains("2")) {
                        if (checkNotBlock(param.getNumM())) {
                            midNum = midNum + 1;
                            if (checkSizeEq2(param.getNumM())) {
                                if (midNum > param.getNumM().get(1)) {
                                    return false;
                                }
                            }
                        }
                    } else if (aliasOne.contains("3")) {
                        if (checkNotBlock(param.getNumH())) {
                            highNum = highNum + 1;
                            if (checkSizeEq2(param.getNumH())) {
                                if (highNum > param.getNumH().get(1)) {
                                    return false;
                                }
                            }
                        }
                    }

                } else if (c == '1') {
                    if (aliasOne.contains("4")) {
                        if (checkNotBlock(param.getNumL())) {
                            lowNum = lowNum + 1;
                            if (checkSizeEq2(param.getNumL())) {
                                if (lowNum > param.getNumL().get(1)) {
                                    return false;
                                }
                            }
                        }
                    } else if (aliasOne.contains("5")) {
                        if (checkNotBlock(param.getNumM())) {
                            midNum = midNum + 1;
                            if (checkSizeEq2(param.getNumM())) {
                                if (midNum > param.getNumM().get(1)) {
                                    return false;
                                }
                            }
                        }
                    } else if (aliasOne.contains("6")) {
                        if (checkNotBlock(param.getNumH())) {
                            highNum = highNum + 1;
                            if (checkSizeEq2(param.getNumH())) {
                                if (highNum > param.getNumH().get(1)) {
                                    return false;
                                }
                            }
                        }
                    }
                } else if (c == '3') {
                    if (aliasOne.contains("7")) {
                        if (checkNotBlock(param.getNumL())) {
                            lowNum = lowNum + 1;
                            if (checkSizeEq2(param.getNumL())) {
                                if (lowNum > param.getNumL().get(1)) {
                                    return false;
                                }
                            }
                        }
                    } else if (aliasOne.contains("8")) {
                        if (checkNotBlock(param.getNumM())) {
                            midNum = midNum + 1;
                            if (checkSizeEq2(param.getNumM())) {
                                if (midNum > param.getNumM().get(1)) {
                                    return false;
                                }
                            }
                        }
                    } else if (aliasOne.contains("9")) {
                        if (checkNotBlock(param.getNumH())) {
                            highNum = highNum + 1;
                            if (checkSizeEq2(param.getNumH())) {
                                if (highNum > param.getNumH().get(1)) {
                                    return false;
                                }
                            }
                        }
                    }
                }


            }
        }
        //breakNum MIN
        if (checkNotBlock(breakNum)) {
            if (breakReal < breakNum.get(0)) {
                return false;
            }
        }
        Character lastChar = s.charAt(s.length() - 1);
        if (checkNotBlock(param.getNumH()) ||
                checkNotBlock(param.getNumM()) ||
                checkNotBlock(param.getNumL())
        ) {
            List<String> aliasOne = alias.get(s.length() - 1);
            if (lastChar == '0') {
                if (aliasOne.contains("1")) {
                    if (checkNotBlock(param.getNumL())) {
                        lowNum = lowNum + 1;
                        if (checkSizeEq2(param.getNumL())) {
                            if (lowNum > param.getNumL().get(1)) {
                                return false;
                            }
                        }
                    }
                } else if (aliasOne.contains("2")) {
                    if (checkNotBlock(param.getNumM())) {
                        midNum = midNum + 1;
                        if (checkSizeEq2(param.getNumM())) {
                            if (midNum > param.getNumM().get(1)) {
                                return false;
                            }
                        }
                    }
                } else if (aliasOne.contains("3")) {
                    if (checkNotBlock(param.getNumH())) {
                        highNum = highNum + 1;
                        if (checkSizeEq2(param.getNumH())) {
                            if (highNum > param.getNumH().get(1)) {
                                return false;
                            }
                        }
                    }
                }

            } else if (lastChar == '1') {
                if (aliasOne.contains("4")) {
                    if (checkNotBlock(param.getNumL())) {
                        lowNum = lowNum + 1;
                        if (checkSizeEq2(param.getNumL())) {
                            if (lowNum > param.getNumL().get(1)) {
                                return false;
                            }
                        }
                    }
                } else if (aliasOne.contains("5")) {
                    if (checkNotBlock(param.getNumM())) {
                        midNum = midNum + 1;
                        if (checkSizeEq2(param.getNumM())) {
                            if (midNum > param.getNumM().get(1)) {
                                return false;
                            }
                        }
                    }
                } else if (aliasOne.contains("6")) {
                    if (checkNotBlock(param.getNumH())) {
                        highNum = highNum + 1;
                        if (checkSizeEq2(param.getNumH())) {
                            if (highNum > param.getNumH().get(1)) {
                                return false;
                            }
                        }
                    }
                }
            } else if (lastChar == '3') {
                if (aliasOne.contains("7")) {
                    if (checkNotBlock(param.getNumL())) {
                        lowNum = lowNum + 1;
                        if (checkSizeEq2(param.getNumL())) {
                            if (lowNum > param.getNumL().get(1)) {
                                return false;
                            }
                        }
                    }
                } else if (aliasOne.contains("8")) {
                    if (checkNotBlock(param.getNumM())) {
                        midNum = midNum + 1;
                        if (checkSizeEq2(param.getNumM())) {
                            if (midNum > param.getNumM().get(1)) {
                                return false;
                            }
                        }
                    }
                } else if (aliasOne.contains("9")) {
                    if (checkNotBlock(param.getNumH())) {
                        highNum = highNum + 1;
                        if (checkSizeEq2(param.getNumH())) {
                            if (highNum > param.getNumH().get(1)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        if (checkNotBlock(param.getNumL())) {
            if (lowNum < param.getNumL().get(0)) {
                return false;
            }
        }
        if (checkNotBlock(param.getNumM())) {
            if (midNum < param.getNumM().get(0)) {
                return false;
            }
        }
        if (checkNotBlock(param.getNumH())) {
            if (highNum < param.getNumH().get(0)) {
                return false;

            }
        }

        //sum MIN
        if (checkNotBlock(sum)) {
            if (char1 == lastChar) {
                sumReal = sumReal + 1;
                if (checkSizeEq2(sum)) {
                    if (sumReal > sum.get(1)) {
                        return false;
                    }
                }
            } else if (char3 == lastChar) {
                sumReal = sumReal + 3;
                if (checkSizeEq2(sum)) {
                    if (sumReal > sum.get(1)) {
                        return false;
                    }
                }
            }

            if (sumReal < sum.get(0)) {
                return false;
            }
        }

        return true;

    }


    private static boolean checkNotBlock(List<Integer> s) {
        return s != null && !s.isEmpty();
    }

    private static boolean checkSizeEq2(List<Integer> s) {
        return s != null && !s.isEmpty() && s.size() > 1;
    }

    private boolean sum(String s, int[] sum) {
        String so = s.replace("0", "");
        if (so.length() < sum[0]) {
            return false;
        }
        char char3 = 51;
        char char1 = 49;
        int sumReal = 0;
        for (int i = 0; i < so.length(); i++) {
            char c = so.charAt(i);
            if (char1 == c) {
                sumReal = sumReal + 1;
                if (sumReal > sum[1]) {
                    return false;
                }
            } else if (char3 == c) {
                sumReal = sumReal + 3;
                if (sumReal > sum[1]) {
                    return false;
                }
            }
        }

        if (sumReal < sum[0]) {
            return false;
        }
        return true;

    }

    private boolean breakNum(String s, int[] breakNum) {
        int breakReal = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) != s.charAt(i + 1)) {
                breakReal = breakReal + 1;
                if (breakReal > breakNum[1]) {
                    return false;
                }
            }
        }
        if (breakReal < breakNum[0]) {
            return false;
        }
        return true;

    }

    public static void main(String[] args) {
//        Param param = new Param();
        List<String> ss = new ArrayList<>();
        ss.add("1");
        ss.add("2");
        ss.add("3");
        ss.add("4");
        ss.add("5");
        ss.add("6");
        ss.add("7");
        ss.add("8");
        ss.add("9");
        List<Integer> win = new ArrayList<>();
        win.add(4);
        win.add(5);
        Param param = new Param();
        param.setNumL(win);
        param.setNumH(win);
        param.setNumM(win);
        param.setAliasList(ss);
        System.out.println(sumAndBreak("000111333", null, null, param));
    }


}
