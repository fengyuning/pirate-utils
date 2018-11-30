import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(4);
        list.add(3);
//        sort(list);
        qSort(list,0,list.size()-1);
        list.forEach(e-> System.out.println(e));
    }

    public static void sort(List<Integer> list) {
        qSort(list, 0, list.size() - 1);
    }

    private static void qSort(List<Integer> list, int low, int high) {
        int mid; //中间位置
        if (low < high) {
            mid = getMid(list, low, high);
            //分别快排两个区域
            qSort(list, mid + 1, high);
            qSort(list, low, mid - 1);
        }
    }

    private static int getMid(List<Integer> list, int low, int high) {
        int mid = list.get(low); //把最低当成中间位置
        while (low < high) {
            while (low < high && list.get(high) >= mid) {
                high--;
            }
            swap(list, low, high);
            while (low < high && list.get(low) <= mid) {
                low++;
            }
            swap(list, low, high);
        }
        return low;
    }

    //交换元素
    private static void swap(List<Integer> list, int i, int j) {
        int bigNum;
        bigNum = list.get(i);
        list.set(i, list.get(j));
        list.set(j, bigNum);
    }

}