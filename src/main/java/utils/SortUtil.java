package utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 排序工具
 *
 * @author fyn
 * @version 1.0
 */
public class SortUtil {

    /**
     * 冒泡排序
     */
    public static List<Integer> bubbleSort(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list is null");
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                //交换位置
                if (list.get(i) > list.get(j)) {
                    swap(list, i, j);
                }
            }
        }
        return list;
    }

    /**
     * 简单选择排序,每次取出n个元素中的最小值
     */
    public static List<Integer> simpleSelectionSort(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list is null");
        }
        int min;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            min = i; //把当前下标定义为最小下标
            for (int j = i + 1; j < size; j++) {    //取出当中最小的元素
                if (list.get(min) > list.get(j)) {
                    min = j;
                }
            }
            if (i != min) {
                swap(list, i, min);
            }
        }
        return list;
    }

    /**
     * 快速排序,核心思想就是分成两部分进行排序
     */
    public static List<Integer> quickSort(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list is null");
        }
        qSort(list, 0, list.size() - 1);
        return list;
    }

    private static void qSort(List<Integer> list, int low, int high) {
        int pivot;
        if (low < high) {
            pivot = partition(list, low, high);
            qSort(list, low, pivot - 1);
            qSort(list, pivot + 1, high);
        }
    }

    /**
     * 把中间值替换到中间,并返回下标
     */
    private static int partition(List<Integer> list, int low, int high) {
        int pivotKey = list.get(low);
        while (low < high) {
            //把比枢纽小的值移到枢纽左边
            while (low < high && list.get(high) >= pivotKey) {
                high--;
            }
            swap(list, low, high);
            //把比枢纽大的值移到右边
            while (low < high && list.get(low) <= pivotKey) {
                low++;
            }
            swap(list, low, high);
        }
        return low; //返回枢纽的位置
    }


    /**
     * 二分法
     */
    public static List<Integer> binarySort(List<Integer> list, Integer middleKey) {
        int low = 0;
        int high = list.size();
        int middleIndex = (low + high) / 2;
        //该值比中间值大,替换
//        if (middleKey > list.get(middleIndex)) {
//        }
        return list;
    }

    //交换元素
    private static void swap(List<Integer> list, int i, int j) {
        int bigNum;
        bigNum = list.get(i);
        list.set(i, list.get(j));
        list.set(j, bigNum);
    }

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(2);
        list.add(4);
        list.add(5);
        list.forEach(e -> System.out.print(e));
        System.out.println();

//        bubbleSort(list);
//        simpleSelectionSort(list);
        quickSort(list);
        list.forEach(e -> System.out.print(e));

    }
}
