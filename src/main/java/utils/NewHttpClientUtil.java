package utils;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 新的Http请求工具类
 *
 * @author fyn
 * @version 1.0
 */
public class NewHttpClientUtil {

    public static void main(String[] args) throws URISyntaxException, IOException {

        String url = "https://d.weibo.com/1087030002_2975_1003_0?page=32";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建url
        URI uri = new URIBuilder(url).build();

        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getStatusLine().getStatusCode());

        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));

    }


}
