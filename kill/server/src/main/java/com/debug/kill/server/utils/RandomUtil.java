package com.debug.kill.server.utils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数生成util
 * @Author nilzxq
 * @Date 2020-06-14 9:51
 */
public class RandomUtil {

    private static final SimpleDateFormat dateFormatOne=new SimpleDateFormat("yyyyMMddHHmmssSS");

    private static final ThreadLocalRandom random=ThreadLocalRandom.current();

    /**
     * 生成订单编号-方式1
     * @return
     */
    public static String generateOrderCode(){
        //TODO：时间戳+N位随机数流水号
        return dateFormatOne.format(DateTime.now().toDate())+generateNumber(4);
    }

    //N为随机数流水号
    public static java.lang.String generateNumber(final int num){
        StringBuffer sb=new StringBuffer();
        for(int i=1;i<=num;i++){
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        for(int i=0;i<10000;i++){
//            System.out.println(generateOrderCode());
//        }
//    }

//    public static void main(String[] args) {
//        String salt="11299c42bf954c0abb373efbae3f6b26";
//        String password="123456";
//        System.out.println(new Md5Hash(password,salt));
//    }
}
