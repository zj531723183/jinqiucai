package com.zd.qiu;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@RestController
@Slf4j
public class QiuApplication {
    @Autowired
    private com.zd.qiu.threadPool.ThreadDealBuffer threadDealBuffer;
    @Autowired
    private com.zd.qiu.threadPool.ThreadDeal threadDeal;
    @Autowired
    private CombinationService combinationService;


    // 设置目标文件的映射路径
    @Value("classpath:static/result.txt")
    private Resource cityJson;

    public static void main(String[] args) {
        SpringApplication.run(QiuApplication.class, args);
    }


    @PostMapping("dealByBuff")
    public Map<String, Object> dealByBuff(@RequestBody Param param, HttpServletRequest req) throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        long start = System.currentTimeMillis();
        StringBuffer finalResult = new StringBuffer();
        StringBuilder combination = new StringBuilder();
        if (param.getNumList() == null || param.getNumList().size() != 14) {
            map.put("code", 400);
            map.put("message", "数据错误");
            return map;
        }
        GenerateStringBuff.m0(combination, new StringBuilder(), 0,
                param.getNumList().get(0), param.getNumList().get(1), param.getNumList().get(2),
                param.getNumList().get(3), param.getNumList().get(4), param.getNumList().get(5), param.getNumList().get(6),
                param.getNumList().get(7), param.getNumList().get(8), param.getNumList().get(9), param.getNumList().get(10),
                param.getNumList().get(11), param.getNumList().get(12), param.getNumList().get(13));
        System.out.println(combination.length());
        System.out.println(combination.length() * 8 / 1024 / 1024);
//        GenerateStringBuff.m0(combination, new StringBuilder(), 0, "1","3","3","3","1","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3");
        log.info("组合时间:" + (System.currentTimeMillis() - start) / 1000);
        dealWithThread(finalResult, combination, param);
        log.info("总时间:" + (System.currentTimeMillis() - start) / 1000);
        int z = finalResult.length() / 14;

        log.info("z筛选结果数量：" + z);
        start = System.currentTimeMillis();
        List<String> result = new ArrayList<>();


        if (param.getDown() != null && 1 == param.getDown()) {
            //文件目录
            FileWriter fileWriter = null;
            try {
                String path = cityJson.getFile().getPath().replace("\\", "\\\\");
                fileWriter = new FileWriter(path);
                //创建文本文件

                for (int i = 0; i < z; i++) {//循环写入
                    if(i==0){
                        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                        fileWriter.write("生成日期：" + dateFormat.format(new Date())+ " ---\r\n");
                    }
                    fileWriter.write(finalResult.substring(i * 14, (i + 1) * 14) + "\r\n");//写入 \r\n换行
                    if (i != 0 && i % 100 == 0) {
                        fileWriter.write("\r\n");//写入 \r\n换行
                        fileWriter.write("--- " + i + "条 ---\r\n");//写入 \r\n换行
                        fileWriter.write("\r\n");//写入 \r\n换行
                    }

                }
                fileWriter.write("共" + z + "条");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                map.put("message", "目录不存在：apache-tomcat-8.0.53/webapps/qiu/WEB-INF/classes/static/result.txt");
                map.put("code", 208);
                return map;
            } finally {

            }
            map.put("message", "可以下载");
            map.put("code", 200);
            return map;

        }

        for (int i = 0; i < z; i++) {
            result.add(finalResult.substring(i * 14, (i + 1) * 14));
        }
        log.info("add总时间:" + (System.currentTimeMillis() - start) / 1000);
        log.info("list筛选结果数量：" + result.size());
        map.put("code", 200);
        map.put("size", result.size());
        map.put("result", result);
        return map;
    }

    @PostMapping("dealByBuffOneThread")
//    @PostMapping("dealByBuff")
    public Map<String, Object> dealByBuffOneThread(@RequestBody Param param, HttpServletRequest req) throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        long start = System.currentTimeMillis();
        StringBuffer finalResult = new StringBuffer();
        StringBuilder combination = new StringBuilder();
        if (param.getNumList() == null || param.getNumList().size() != 14) {
            map.put("code", 400);
            map.put("message", "数据错误");
            return map;
        }
        GenerateStringBuff.m0(combination, new StringBuilder(), 0,
                param.getNumList().get(0), param.getNumList().get(1), param.getNumList().get(2),
                param.getNumList().get(3), param.getNumList().get(4), param.getNumList().get(5), param.getNumList().get(6),
                param.getNumList().get(7), param.getNumList().get(8), param.getNumList().get(9), param.getNumList().get(10),
                param.getNumList().get(11), param.getNumList().get(12), param.getNumList().get(13));
        System.out.println(combination.length());
        System.out.println(combination.length() * 8 / 1024 / 1024);
//        GenerateStringBuff.m0(combination, new StringBuilder(), 0, "1","3","3","3","1","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3","1,0,3");
        log.info("组合时间:" + (System.currentTimeMillis() - start) / 1000);
        dealWithThreadOne(finalResult, combination, param);
        log.info("总时间:" + (System.currentTimeMillis() - start) / 1000);
        int z = finalResult.length() / 14;
        log.info("z筛选结果数量：" + z);
        start = System.currentTimeMillis();
        List<String> result = new ArrayList<>();


        if (param.getDown() != null && 1 == param.getDown()) {
            //文件目录
            FileWriter fileWriter = null;
            try {
                String path = cityJson.getFile().getPath().replace("\\", "\\\\");
                fileWriter = new FileWriter(path);//创建文本文件

                for (int i = 0; i < z; i++) {//循环写入

                    fileWriter.write(finalResult.substring(i * 14, (i + 1) * 14) + "\r\n");//写入 \r\n换行
                    if (i != 0 && i % 100 == 0) {
                        fileWriter.write("\r\n");//写入 \r\n换行
                        fileWriter.write("--- " + i + "条 ---\r\n");//写入 \r\n换行
                        fileWriter.write("\r\n");//写入 \r\n换行
                    }

                }
                fileWriter.write("共" + z + "条");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        for (int i = 0; i < z; i++) {
            result.add(finalResult.substring(i * 14, (i + 1) * 14));
        }
        log.info("add总时间:" + (System.currentTimeMillis() - start) / 1000);
        log.info("list筛选结果数量：" + result.size());
        map.put("code", 200);
        map.put("size", result.size());
        map.put("result", result);
        return map;
    }


    private void dealWithThread(StringBuffer finalResult, StringBuilder combination, Param param) throws InterruptedException {
        long start = System.currentTimeMillis();
        int coreNum = 4;
        int coreZ = coreNum * 14;
        //TODO 家装14位完整切割
        int dealNum = combination.length() / coreZ;
        coreNum = combination.length() % coreZ == 0 ? coreNum : coreNum + 1;
        CountDownLatch countDownLatch = new CountDownLatch(coreNum);
        while (coreNum > 0) {
            if (coreNum == 5) {
                threadDealBuffer.dealRemove(finalResult, combination.substring(dealNum * 14 * (coreNum - 1), combination.length()), countDownLatch, param);
            } else {
                threadDealBuffer.dealRemove(finalResult, combination.substring(dealNum * 14 * (coreNum - 1), dealNum * 14 * (coreNum)), countDownLatch, param);
//                threadDeal.dealRemove(finalResult, combination.toString(), countDownLatch, param);
            }
            coreNum = coreNum - 1;
        }
        countDownLatch.await();
        log.info("线程处理时间（不含组合时间）" + (System.currentTimeMillis() - start) / 1000);
    }

    private void dealWithThreadOne(StringBuffer finalResult, StringBuilder combination, Param param) throws InterruptedException {
        long start = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        threadDealBuffer.dealRemove(finalResult, combination.toString(), countDownLatch, param);
        countDownLatch.await();
        log.info("线程处理时间（不含组合时间）" + (System.currentTimeMillis() - start) / 1000);
    }

    @PostMapping("deal")
    public String deal(@RequestBody Param param, HttpServletRequest req) throws InterruptedException {

        long start = System.currentTimeMillis();

        List<String> listFinal = testGenerate(param.getListString());
        List<String> listFinalResult = Collections.synchronizedList(new ArrayList<>());
        dealWithThread(listFinalResult, listFinal, param);
        log.info("总时间:" + (System.currentTimeMillis() - start) / 1000);
        return listFinalResult.size() + "zzz";
    }

    private void dealWithThread(List<String> result, List<String> list, Param param) throws InterruptedException {
        long start = System.currentTimeMillis();
        int coreNum = 4;
        int dealNum = list.size() / coreNum;
        coreNum = list.size() % coreNum == 0 ? coreNum : coreNum + 1;
        CountDownLatch countDownLatch = new CountDownLatch(coreNum);
        while (coreNum > 0) {
            if (coreNum == 5) {
                threadDeal.dealRemove(result, list.subList(0, list.size()), countDownLatch, param);
            } else {
                threadDeal.dealRemove(result, list.subList(dealNum * (coreNum - 1), dealNum * (coreNum)), countDownLatch, param);
            }
            coreNum = coreNum - 1;
        }
        countDownLatch.await();
        log.info("线程处理时间（不含组合时间）" + (System.currentTimeMillis() - start) / 1000);
    }

    @GetMapping("testDeal")
    public String testDeal() throws InterruptedException {
        List<String> listFinal = Collections.synchronizedList(new ArrayList<>());
        long start = System.currentTimeMillis();
        String[] i = {"1", "2", "3"};

        List<List<String>> iList = new ArrayList<>();
        for (int j = 0; j < 14; j++) {
            iList.add(Arrays.asList(i));
        }

        List<String> list = CombinationUtil.getCombination(iList);
        log.info(list.size() + "");
        log.info((System.currentTimeMillis() - start) / 1000 + "");
        dealWithThread(listFinal, list, null);
        log.info((System.currentTimeMillis() - start) / 1000 + "");

        return listFinal.size() + "testDeal";


    }

    @GetMapping("testGenerate")
    //多线程快速组合
    public List<String> testGenerate(List<List<String>> iList) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<String> listFinal1 = Collections.synchronizedList(new ArrayList<>());
        List<String> listFinal2 = Collections.synchronizedList(new ArrayList<>());
        List<String> listFinal3 = Collections.synchronizedList(new ArrayList<>());
        List<String> listFinal = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(3);
        if (true) {
            combinationService.getCombination(iList.subList(0, 5), listFinal1, countDownLatch);
            combinationService.getCombination(iList.subList(5, 10), listFinal2, countDownLatch);
            combinationService.getCombination(iList.subList(10, 14), listFinal3, countDownLatch);
        }
        countDownLatch.await();
        List<List<String>> listFinalDo = new ArrayList<>();
        listFinalDo.add(listFinal1);
        listFinalDo.add(listFinal2);
        listFinalDo.add(listFinal3);
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        combinationService.getCombination(listFinalDo, listFinal, countDownLatch1);
        countDownLatch1.await();
        log.info("组合时间:" + (System.currentTimeMillis() - start) / 1000);
        return listFinal;

    }


}