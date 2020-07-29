package com.zd.qiu.threadPool;


import com.zd.qiu.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * @Description: 质维码分配记录
 * @Author: zj
 * @Date: 2020-07-04
 * @Version: V1.0
 */


@Service
public class ThreadDealBuffer {


    @Async("asyncServiceExecutor")
    public void dealRemove(StringBuffer finalString, String combinationPart, CountDownLatch countDownLatch, Param param) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            int i = combinationPart.length() / 14;

            while (i > 0) {
                if (!sumAndBreak(combinationPart, (i - 1) * 14, param)) {
                    i--;
                    continue;
                }
                finalString.append(combinationPart.substring((i - 1) * 14, i * 14));
                i--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }

    private boolean sumAndBreak(String combinationPart, int charStart, Param param) {
        if (!checkNotBlock(param.getBreakNum())
                && !checkNotBlock(param.getSum())
                && !checkNotBlock(param.getContinue0())
                && !checkNotBlock(param.getContinue1())
                && !checkNotBlock(param.getContinue3())
                && !checkNotBlock(param.getInclude0())
                && !checkNotBlock(param.getInclude1())
                && !checkNotBlock(param.getInclude3())
                && !checkNotBlock(param.getNumH())
                && !checkNotBlock(param.getNumM())
                && !checkNotBlock(param.getNumL())
                && !checkNotBlock(param.getPeace())
                && !checkNotBlock(param.getLose())
                && !checkNotBlock(param.getWin())
        ) {
            return true;

        }


        //断点
        boolean dealBreakNum = checkNotBlock(param.getBreakNum());
        boolean checkBreakMax = checkSizeEq2(param.getBreakNum());
        int breakReal = 0;
        //和值
        boolean dealSumNum = checkNotBlock(param.getSum());
        boolean checkSumMax = checkSizeEq2(param.getSum());
        int sumReal = 0;
        //计算高中低概率
        boolean dealNumLNum = checkNotBlock(param.getNumL());
        boolean checkNumLMax = checkSizeEq2(param.getNumL());
        int lowNum = 0;
        boolean dealNumMNum = checkNotBlock(param.getNumM());
        boolean checkNumMMax = checkSizeEq2(param.getNumM());
        int midNum = 0;
        boolean dealNumHNum = checkNotBlock(param.getNumH());
        boolean checkNumHMax = checkSizeEq2(param.getNumH());
        int highNum = 0;
        //连胜平负
        boolean dealWinContinueNum = checkNotBlock(param.getWin());
        boolean checkWinContinueMax = checkSizeEq2(param.getWin());
        int winContinueNum = 0;
        int winContinueNumTemp = 0;


        boolean dealPeaceContinueNum = checkNotBlock(param.getPeace());
        boolean checkPeaceContinueMax = checkSizeEq2(param.getPeace());
        int peaceContinueNum = 0;
        int peaceContinueNumTemp = 0;

        boolean dealLoseContinueNum = checkNotBlock(param.getLose());
        boolean checkLoseContinueMax = checkSizeEq2(param.getLose());
        int loseContinueNum = 0;
        int loseContinueNumTemp = 0;
        //最大连续
        boolean dealZeroContinueNum = checkNotBlock(param.getContinue0());
        boolean checkZeroContinueMax = checkSizeEq2(param.getContinue0());
        int zeroContinueNum = 0;

        boolean dealOneContinueNum = checkNotBlock(param.getContinue1());
        boolean checkOneContinueMax = checkSizeEq2(param.getContinue1());
        int oneContinueNum = 0;
        boolean dealThreeContinueNum = checkNotBlock(param.getContinue3());
        boolean checkThreeContinueMax = checkSizeEq2(param.getContinue3());
        int threeContinueNum = 0;

        //包含
        boolean dealZeroIncludeNum = checkNotBlock(param.getInclude0());
        boolean checkZeroIncludeMax = checkSizeEq2(param.getInclude0());
        int zeroIncludeNum = 0;

        boolean dealOneIncludeNum = checkNotBlock(param.getInclude1());
        boolean checkOneIncludeMax = checkSizeEq2(param.getInclude1());
        int oneIncludeNum = 0;

        boolean dealThreeIncludeNum = checkNotBlock(param.getInclude3());
        boolean checkThreeIncludeMax = checkSizeEq2(param.getInclude3());
        int threeIncludeNum = 0;
        boolean threeFlag = false;
        boolean oneFlag = false;
        boolean zeroFlag = false;

        for (int i = 0; i < 14; i++) {
            int index = charStart + i;
            char c = combinationPart.charAt(index);

            if (i < 13) {
                //breakNum
                if (dealBreakNum) {
                    if (c != combinationPart.charAt(index + 1)) {
                        breakReal = breakReal + 1;
                        // check MAX
                        if (checkMax(checkBreakMax, breakReal, param.getBreakNum())) {
                            return false;
                        }
                    }
                }

            }
            //include

            if (dealThreeIncludeNum) {
                if (c == '3') {
                    threeIncludeNum = threeIncludeNum + 1;
                }
                if (checkMax(checkThreeIncludeMax, threeIncludeNum, param.getInclude3())) {
                    return false;
                }
            }
            if (dealOneIncludeNum) {
                if (c == '1') {
                    oneIncludeNum = oneIncludeNum + 1;
                }
                if (checkMax(checkOneIncludeMax, oneIncludeNum, param.getInclude1())) {
                    return false;
                }
            }
            if (dealZeroIncludeNum) {
                if (c == '0') {
                    zeroIncludeNum = zeroIncludeNum + 1;
                }
                if (checkMax(checkZeroIncludeMax, zeroIncludeNum, param.getInclude0())) {
                    return false;
                }
            }

            //win - peace - lose  && continue

            if (dealWinContinueNum || dealThreeContinueNum) {
                if (c == '3') {
                    threeFlag = true;
                    winContinueNumTemp = winContinueNumTemp + 1;
                    // check MAX
                    if (checkMax(checkWinContinueMax, winContinueNumTemp, param.getWin())) {
                        return false;
                    }

                } else {
                    if (threeFlag) {
                        threeFlag = false;
                        if (dealWinContinueNum && winContinueNumTemp > winContinueNum) {
                            winContinueNum = winContinueNumTemp;
                        }
                        if (dealThreeContinueNum && winContinueNumTemp > 1) {
                            threeContinueNum = winContinueNumTemp - 1 + threeContinueNum;
                            if (checkMax(checkThreeContinueMax, threeContinueNum, param.getContinue3())) {
                                return false;
                            }
                        }
                        winContinueNumTemp = 0;
                    }
                }
            }
            if (dealPeaceContinueNum || dealOneContinueNum) {
                if (c == '1') {
                    oneFlag = true;
                    peaceContinueNumTemp = peaceContinueNumTemp + 1;
                    // check MAX
                    if (checkMax(checkPeaceContinueMax, peaceContinueNumTemp, param.getPeace())) {
                        return false;
                    }

                } else {
                    if (oneFlag) {
                        oneFlag = false;
                        if (dealPeaceContinueNum && peaceContinueNumTemp > peaceContinueNum) {
                            peaceContinueNum = peaceContinueNumTemp;
                        }
                        if (dealOneContinueNum && peaceContinueNumTemp > 1) {
                            oneContinueNum = peaceContinueNumTemp - 1 + oneContinueNum;
                            if (checkMax(checkOneContinueMax, oneContinueNum, param.getContinue1())) {
                                return false;
                            }
                        }
                        peaceContinueNumTemp = 0;
                    }
                }
            }
            if (dealLoseContinueNum || dealZeroContinueNum) {
                if (c == '0') {
                    zeroFlag = true;
                    loseContinueNumTemp = loseContinueNumTemp + 1;
                    // check MAX
                    if (checkMax(checkLoseContinueMax, loseContinueNumTemp, param.getLose())) {
                        return false;
                    }


                } else {
                    if (zeroFlag) {
                        zeroFlag = false;
                        if (dealLoseContinueNum && loseContinueNumTemp > loseContinueNum) {
                            loseContinueNum = loseContinueNumTemp;
                        }
                        if (dealZeroContinueNum && loseContinueNumTemp > 1) {
                            zeroContinueNum = loseContinueNumTemp - 1 + zeroContinueNum;
                            if (checkMax(checkZeroContinueMax, zeroContinueNum, param.getContinue0())) {
                                return false;
                            }
                        }
                        loseContinueNumTemp = 0;
                    }
                }
            }

            if (i == 13) {
                //win
                if (dealWinContinueNum && winContinueNumTemp > winContinueNum) {
                    winContinueNum = winContinueNumTemp;
                }
                //three
                if (dealThreeContinueNum && winContinueNumTemp > 1) {
                    threeContinueNum = winContinueNumTemp - 1 + threeContinueNum;
                    if (checkMax(checkThreeContinueMax, threeContinueNum, param.getContinue3())) {
                        return false;
                    }
                }
                //peace
                if (dealPeaceContinueNum && peaceContinueNumTemp > peaceContinueNum) {
                    peaceContinueNum = peaceContinueNumTemp;
                }
                //one
                if (dealOneContinueNum && peaceContinueNumTemp > 1) {
                    oneContinueNum = peaceContinueNumTemp - 1 + oneContinueNum;
                    if (checkMax(checkOneContinueMax, oneContinueNum, param.getContinue1())) {
                        return false;
                    }
                }
                //LOSE
                if (dealLoseContinueNum && loseContinueNumTemp > loseContinueNum) {
                    loseContinueNum = loseContinueNumTemp;
                }
                //ZORO
                if (dealZeroContinueNum && loseContinueNumTemp > 1) {
                    zeroContinueNum = loseContinueNumTemp - 1 + zeroContinueNum;
                    if (checkMax(checkZeroContinueMax, zeroContinueNum, param.getContinue0())) {
                        return false;
                    }
                }
            }


            //sum
            if (dealSumNum) {
                if ('1' == c) {
                    sumReal = sumReal + 1;
                    // check MAX
                    if (checkMax(checkSumMax, sumReal, param.getSum())) {
                        return false;
                    }
                } else if ('3' == c) {
                    sumReal = sumReal + 3;
                    // check MAX
                    if (checkMax(checkSumMax, sumReal, param.getSum())) {
                        return false;
                    }
                }
            }
            //h-M-L
            if (dealNumLNum || dealNumHNum || dealNumMNum) {
                List<String> alias = param.getAliasList();
                String aliasOne = alias.get(i);
                if (c == '0') {
                    if (aliasOne.contains("1")) {
                        if (dealNumLNum) {
                            lowNum = lowNum + 1;
                            // check MAX
                            if (checkMax(checkNumLMax, lowNum, param.getNumL())) {
                                return false;
                            }
                        }
                    } else if (aliasOne.contains("2")) {
                        if (dealNumMNum) {
                            midNum = midNum + 1;
                            // check MAX
                            if (checkMax(checkNumMMax, midNum, param.getNumM())) {
                                return false;
                            }
                        }
                    } else if (aliasOne.contains("3")) {
                        if (dealNumHNum) {
                            highNum = highNum + 1;
                            // check MAX
                            if (checkMax(checkNumHMax, highNum, param.getNumH())) {
                                return false;
                            }
                        }
                    }

                } else if (c == '1') {
                    if (aliasOne.contains("4")) {
                        if (dealNumLNum) {
                            lowNum = lowNum + 1;
                            // check MAX
                            if (checkMax(checkNumLMax, lowNum, param.getNumL())) {
                                return false;
                            }
                        }
                    } else if (aliasOne.contains("5")) {
                        if (dealNumMNum) {
                            midNum = midNum + 1;
                            // check MAX
                            if (checkMax(checkNumMMax, midNum, param.getNumM())) {
                                return false;
                            }
                        }
                    } else if (aliasOne.contains("6")) {
                        if (dealNumHNum) {
                            highNum = highNum + 1;
                            // check MAX
                            if (checkMax(checkNumHMax, highNum, param.getNumH())) {
                                return false;
                            }
                        }
                    }
                } else if (c == '3') {
                    if (aliasOne.contains("7")) {
                        if (dealNumLNum) {
                            lowNum = lowNum + 1;
                            // check MAX
                            if (checkMax(checkNumLMax, lowNum, param.getNumL())) {
                                return false;
                            }
                        }
                    } else if (aliasOne.contains("8")) {
                        if (dealNumMNum) {
                            midNum = midNum + 1;
                            // check MAX
                            if (checkMax(checkNumMMax, midNum, param.getNumM())) {
                                return false;
                            }
                        }
                    } else if (aliasOne.contains("9")) {
                        if (dealNumHNum) {
                            highNum = highNum + 1;
                            // check MAX
                            if (checkMax(checkNumHMax, highNum, param.getNumH())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }


        //breakNum MIN
        if (checkMin(dealBreakNum, breakReal, param.getBreakNum())) {
            return false;
        }

        //sum MIN
        if (checkMin(dealSumNum, sumReal, param.getSum())) {
            return false;
        }
        //H-M-L  MIN
        if (checkMin(dealNumLNum, lowNum, param.getNumL())) {
            return false;
        }
        if (checkMin(dealNumMNum, midNum, param.getNumM())) {
            return false;
        }
        if (checkMin(dealNumHNum, highNum, param.getNumH())) {
            return false;
        }
        //continue
        if (checkMin(dealThreeContinueNum, threeContinueNum, param.getContinue3())) {
            return false;
        }
        if (checkMin(dealOneContinueNum, oneContinueNum, param.getContinue1())) {
            return false;
        }
        if (checkMin(dealZeroContinueNum, zeroContinueNum, param.getContinue0())) {
            return false;
        }
        //win peace lose
        if (checkMin(dealWinContinueNum, winContinueNum, param.getWin())) {
            return false;
        }
        if (checkMin(dealPeaceContinueNum, peaceContinueNum, param.getPeace())) {
            return false;
        }
        if (checkMin(dealLoseContinueNum, loseContinueNum, param.getLose())) {
            return false;
        }
        //include
        if (checkMin(dealThreeIncludeNum, threeIncludeNum, param.getInclude3())) {
            return false;
        }
        if (checkMin(dealOneIncludeNum, oneIncludeNum, param.getInclude1())) {
            return false;
        }
        if (checkMin(dealZeroIncludeNum, zeroIncludeNum, param.getInclude0())) {
            return false;
        }
        System.out.println("winContinueNum" + winContinueNum);
        System.out.println("winContinueNumTemp" + winContinueNumTemp);
        System.out.println("peaceContinueNum" + winContinueNum);
        System.out.println("peaceContinueNumTemp" + winContinueNumTemp);
        System.out.println("loseContinueNum" + winContinueNum);
        System.out.println("loseContinueNumTemp" + winContinueNumTemp);

        return true;
    }

    private static boolean checkMin(boolean dealNumHNum, int highNum, List<Integer> numH) {
        if (dealNumHNum) {
            if (highNum < numH.get(0)) {
                return true;

            }
        }
        return false;
    }

    private static boolean checkMax(boolean needCheckMax, int max, List<Integer> sum) {
        if (needCheckMax) {
            if (max > sum.get(1)) {
                return true;
            }
        }
        return false;
    }


    private static boolean checkNotBlock(List<Integer> s) {
        return s != null && !s.isEmpty();
    }

    private static boolean checkSizeEq2(List<Integer> s) {
        return s != null && !s.isEmpty() && s.size() > 1;
    }

//    public static void main(String[] args) {
//        Param param = new Param();
//        CountDownLatch c = new CountDownLatch(1);
//
//        StringBuffer result = new StringBuffer();
//        String dealS = "1333333333333311333333333333";
//        System.out.println("dealS.length" + dealS.length());
//        List<Integer> z = new ArrayList<>();
//        z.add(1);
//        z.add(14);
//        param.setPeace(z);
//        dealRemove(result, dealS, c, param);
//    }

}

