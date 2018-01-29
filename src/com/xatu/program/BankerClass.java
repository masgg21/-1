package com.xatu.program;

import java.util.Scanner;

public class BankerClass {
	private static int numOfCustomers;
	private static int numOfResources;

	int[] Available;
	int[][] Max;
	int[][] Alloction;
	int[][] Need;
	int[][] Request;
	int[] Work;
	int[] S;// 安全序列
	int num = 0;// 进程编号
	Scanner in = new Scanner(System.in);
	// 进程数

	public static void setNumber(int number) {
		BankerClass.numOfCustomers = number;
	}

	public static void setZiyuan_num(int ziyuan_num) {
		BankerClass.numOfResources = ziyuan_num;
	}

	public BankerClass(int num1, int ziyuan_num) {
		BankerClass.setNumber(num1);
		BankerClass.setZiyuan_num(ziyuan_num);

		Alloction = new int[num1][ziyuan_num];
		Max = new int[num1][ziyuan_num];
		Need = new int[num1][ziyuan_num];
		Request = new int[num1][ziyuan_num];
		Work = new int[ziyuan_num];
		S = new int[num1];
		Available = new int[ziyuan_num];
	}

	public void setSystemVariable() {// 设置各初始系统变量，并判断是否处于安全状态。
		System.out.println("请输入每个类型的资源数：");
		for (int j = 0; j < numOfResources; j++) {
			Available[j] = in.nextInt();
		}
		setMax();
		setAlloction();
		printSystemVariable();
		SecurityAlgorithm();
	}

	public void setMax() {// 设置Max矩阵
		System.out.println("请设置各个客户的最大需求矩阵Max：");
		for (int i = 0; i < numOfCustomers; i++) {
			System.out.println("请输入客户 P" + i + "的最大资源需求量：");
			for (int j = 0; j < numOfResources; j++) {
				Max[i][j] = in.nextInt();
			}
		}
	}

	public void setAlloction() {// 设置已分配矩阵Alloction
		System.out.println("请设置请各客户已有的已分配矩阵Alloction：");
		for (int i = 0; i < numOfCustomers; i++) {
			System.out.println("请输入客户 P" + i + "的以分配资源量：");
			for (int j = 0; j < numOfResources; j++) {
				Alloction[i][j] = in.nextInt();
			}
		}
		System.out.println("Available=Available-Alloction.");
		System.out.println("Need=Max-Alloction.");
		for (int i = 0; i < numOfResources; i++) {// 设置Alloction矩阵
			for (int j = 0; j < numOfCustomers; j++) {
				Available[i] = Available[i] - Alloction[j][i];
			}
		}
		for (int i = 0; i < numOfCustomers; i++) {// 设置Need矩阵
			for (int j = 0; j < numOfResources; j++) {
				Need[i][j] = Max[i][j] - Alloction[i][j];
			}
		}
	}

	public void printSystemVariable() {
		System.out.println("此时资源分配量如下：");
		System.out.println("客户  " + "   Max   " + "   Alloction " + "    Need  " + "     Available ");
		for (int i = 0; i < numOfCustomers; i++) {
			System.out.print("P" + i + "  ");
			for (int j = 0; j < numOfResources; j++) {
				System.out.print(Max[i][j] + "  ");
			}
			System.out.print("|  ");
			for (int j = 0; j < numOfResources; j++) {
				System.out.print(Alloction[i][j] + "  ");
			}
			System.out.print("|  ");
			for (int j = 0; j < numOfResources; j++) {
				System.out.print(Need[i][j] + "  ");
			}
			System.out.print("|  ");
			if (i == 0) {
				for (int j = 0; j < numOfResources; j++) {
					System.out.print(Available[j] + "  ");
				}
			}
			System.out.println();
		}
	}

	public void setRequest() {// 设置请求资源量Request

		System.out.println("请输入请求资源的客户编号：");
		num = in.nextInt();// 设置全局变量进程编号num
		System.out.println("请输入请求各种资源的数量：");
		for (int j = 0; j < numOfResources; j++) {
			Request[num][j] = in.nextInt();
		}
		System.out.print("即客户 P" + num + "对各资源请求Request：(");
		for (int j = 0; j < numOfResources; j++) {
			if (j == numOfResources - 1) {
				System.out.println(Request[num][j] + ").");
			} else
				System.out.print(+Request[num][j] + ",");
		}

		BankerAlgorithm();
	}

	public void BankerAlgorithm() {// 银行家算法,资源请求算法
		boolean T = true;
		int m = 0;
		int n = 0;
		for (int j = 0; j < numOfResources; j++) {
			if (Request[num][j] <= Need[num][j]) {
				n++;
				if (Request[num][j] <= Available[j]) {
					m++;
				}
			}
		}
		if (n == numOfResources && m == numOfResources) {
			for (int i = 0; i < numOfResources; i++) {
				Available[i] -= Request[num][i];
				Alloction[num][i] += Request[num][i];
				Need[num][i] -= Request[num][i];
			}
		} else if (n < numOfResources) {
			System.out.println("客户 P" + num + "请求已经超出最大需求量Need.");
			printSystemVariable();
			T = false;
		} else if (n == numOfResources && m < numOfResources) {
			System.out.println("当前没有足够的资源可分配，客户 P" + num + "需等待。");
			printSystemVariable();
			T = false;
		}
		if (T == true) {
			printSystemVariable();
			System.out.println("现在进入安全算法：");
			SecurityAlgorithm();
		}
	}

	public void SecurityAlgorithm() {// 安全算法
		boolean[] Finish = new boolean[numOfCustomers];
		for (int i = 0; i < numOfCustomers; i++) {
			Finish[i] = false;// 初始化Finish
		}
		int count = 0;// 完成进程数
		int circle = 0;// 循环圈数
		for (int i = 0; i < numOfResources; i++) {// 设置工作向量
			Work[i] = Available[i];
		}
		boolean flag = true;
		while (count < numOfCustomers) {
			if (flag) {
				System.out.println("客户  " + "   Work  " + "   Alloction " + "    Need  " + "     Work+Alloction ");
				flag = false;
			}
			int m = 0;
			for (int i = 0; i < numOfCustomers; i++) {
				for (int s = 0; s < numOfResources; s++) {
					if (Need[i][s] <= Work[s]) {
						m++;
					}
				}
				if (Finish[i] == false && m == numOfResources) {// 判断条件
					System.out.print("P" + i + "  ");
					for (int k = 0; k < numOfResources; k++) {
						System.out.print(Work[k] + "  ");
					}
					System.out.print("|  ");
					for (int j = 0; j < numOfResources; j++) {
						Work[j] += Alloction[i][j];
					}
					Finish[i] = true;// 当当前进程能满足时
					S[count] = i;// 设置当前序列排号

					count++;// 满足进程数加1
					for (int j = 0; j < numOfResources; j++) {
						System.out.print(Alloction[i][j] + "  ");
					}
					System.out.print("|  ");
					for (int j = 0; j < numOfResources; j++) {
						System.out.print(Need[i][j] + "  ");
					}
					System.out.print("|  ");
					for (int j = 0; j < numOfResources; j++) {
						System.out.print(Work[j] + "  ");
					}
					System.out.println();
				}
				m = 0;
			}
			circle++;// 循环圈数加1

			if (count == numOfCustomers) {// 判断是否满足所有进程需要
				System.out.print("此时存在一个安全序列：");
				for (int i = 0; i < numOfCustomers; i++) {// 输出安全序列
					System.out.print("P" + S[i] + " ");
				}
				System.out.println("故当前可分配！");
				break;// 跳出循环
			}
			if (count < circle) {// 判断完成进程数是否小于循环圈数
				count = 5;
				System.out.println("当前系统处于不安全状态，故不存在安全序列。");
				System.out.println("恢复原来状态：");
				for (int i = 0; i < numOfResources; i++) {
					Available[i] += Request[num][i];
					Alloction[num][i] -= Request[num][i];
					Need[num][i] += Request[num][i];
				}
				printSystemVariable();
				break;// 跳出循环
			}
		}
	}
}
