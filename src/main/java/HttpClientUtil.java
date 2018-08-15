import info.monitorenter.cpdetector.io.*;
import org.apache.commons.io.IOUtils;
import sun.net.www.protocol.https.Handler;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPInputStream;

/**
 * Http请求工具类
 *
 * @author fyn
 * @version 1.0
 */
public class HttpClientUtil {
    private static final String GET = "get";
    private static final String POST = "post";
    /*重试次数*/
    private static final int DEFAULT_RETRY_TIMES = 3;
    /*超时时间*/
    public static int DEFAULT_TIME_OUT = 5000;
    /*默认字符集*/
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static void main(String[] args) throws Exception {

        String url = "http://www.livemook.com/?s=%E7%94%B7%E8%A3%85&paged=1";
        String url2 = "http://www.livemook.com/?p=150877";
        String string = get(url2, null);
        System.out.println(string);

    }


    public static String get(String url, Map<String, String> headMap) throws Exception {
        try {
            URLConnection connection = buildURLConnection(url, GET, headMap);
            connection.connect();

            //解析页面字符集(引入了第三方架包)
            CodepageDetectorProxy cdp = CodepageDetectorProxy.getInstance();
            cdp.add(JChardetFacade.getInstance());
            cdp.add(ASCIIDetector.getInstance());
            cdp.add(UnicodeDetector.getInstance());
            cdp.add(new ParsingDetector(false));
            cdp.add(new ByteOrderMarkDetector());

            InputStream inputStream = connection.getInputStream();
            ByteArrayInputStream bis = null;
            String encoding = connection.getHeaderField("Content-Encoding");
            if (encoding != null && encoding.contains("gzip")) {
                GZIPInputStream gip = new GZIPInputStream(inputStream);
                inputStream = gip;
                bis = new ByteArrayInputStream(IOUtils.toByteArray(gip));
            } else {
                bis = new ByteArrayInputStream(IOUtils.toByteArray(inputStream));
            }
            Charset charset = cdp.detectCodepage(bis, 2147483647);

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(bis, charset));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\r\n");
                }
                return builder.toString();
            } catch (Exception e) {
                throw new Exception(e);
            } finally {
                reader.close();
                inputStream.close();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static URLConnection buildURLConnection(String urlStr, String method, Map<String, String> headMap) throws Exception {
        URL url = new URL(urlStr);
        AtomicBoolean httpsSign = new AtomicBoolean(false);
        if (urlStr.contains("https://")) {
            url = new URL(null, urlStr, new Handler());
            httpsSign.set(true);
        }
        //跳过证书验证
        ignoreSsl();
        //构造connection
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setReadTimeout(DEFAULT_TIME_OUT);
        connection.setRequestProperty("Accept-Charset", DEFAULT_CHARSET);

        if (headMap != null && !headMap.isEmpty()) {
            for (String property : headMap.keySet()) {
                connection.setRequestProperty(property, headMap.get(property));
            }
        }
//        if (httpsSign.get()) {
//            ((HttpsURLConnection) connection).setRequestMethod(method);
//        } else {
//            ((HttpURLConnection) connection).setRequestMethod(method);
//        }
        return connection;
    }


    //----------------------------------------------------------------------------------------
    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }

    /**
     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
     *
     * @throws Exception
     */
    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

}
