import java.net.URLEncoder;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {

//        String url = "http://www.livemook.com/?s=%E7%94%B7%E8%A3%85&paged=1";
//        String string = HttpClientUtil.get(url);
//        System.out.println(string);

        String url = "http://bbs.xbiao.com/search.php?mod=forum";
        String body = "formhash=0cf6abff&srchtxt=%E5%8A%B3%E5%8A%9B%E5%A3%AB&searchsubmit=yes";
//        Map<String, String> headMap = UserAgent.getUserAgentHeadMap();

        String post = HttpClientUtil.post(url, body, null);
        System.out.println(post);

//        String encode1 = URLEncoder.encode(string, "UTF-8");
//        String encode2 = URLEncoder.encode(string, "GBK");
//        System.out.println(encode1);
//        System.out.println(encode2);
    }
}
