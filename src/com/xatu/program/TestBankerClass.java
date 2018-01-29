package com.xatu.program;

import java.util.Scanner;

public class TestBankerClass {

    public static void main(String[] args) {
        // TODO code application logic here
        boolean Choose = true;
        String C;
        Scanner in = new Scanner(System.in);
        System.out.println("这是一个多进程，初始系统考虑n个客户的请求m个资源的银行家算法：");
        System.out.println("请输入n个客户数：");
        int num = in.nextInt();
        System.out.println("请输入m个资源数：");
        int ziyuan_num = in.nextInt();
        BankerClass T = new BankerClass(num,ziyuan_num);
        T.setSystemVariable();
        while (Choose == true) {
            T.setRequest();
            System.out.println("您是否还要进行请求：y/n?");
            C = in.nextLine();
            if (C.endsWith("n")) {
                Choose = false;
            }
        }
    }
}
