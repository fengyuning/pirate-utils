public class Test {
    public static void main(String[] args) throws Exception {

        String url = "http://www.livemook.com/?s=%E7%94%B7%E8%A3%85&paged=1";
        String string = HttpClientUtil.get(url, null);
        System.out.println(string);
    }
}
