import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {

//        ArrayList<Integer> list = null;
//
//        System.out.println(list.isEmpty());

        String url = "http://www.baidu.com/link?url=YiudcL0Y_jELvUeUPca-UtL7Oo0350QQWSLT6uf7k3gU3djQZJ67cKieqFV_EUIlWuWcwQ2wV8vi-Ck355ao-_";
        String url2 = "https://mip.fengdu100.com/article62751.html";
        String string = HttpClientUtil.get302Url(url2,null);
        System.out.println(string);

//        String encode1 = URLEncoder.encode(string, "UTF-8");
//        String encode2 = URLEncoder.encode(string, "GBK");
//        System.out.println(encode1);
//        System.out.println(encode2);
    }
}
