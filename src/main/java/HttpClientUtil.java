import info.monitorenter.cpdetector.io.*;
import org.apache.commons.io.IOUtils;
import sun.net.www.protocol.https.Handler;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
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
    private static final String HTTPS = "https";
    private static final String UA = "User-Agent";

    /*重试次数*/
    private static final int DEFAULT_RETRY_TIMES = 3;
    /*超时时间*/
    public static int DEFAULT_TIME_OUT = 5000;
    /*默认字符集*/
    public static final String DEFAULT_CHARSET = "UTF-8";

    /*电脑UA*/
    private static List<String> PC_UA_LIST = new ArrayList<String>() {{
        add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
        add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
        add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
        add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3396.99 Safari/537.36");
        add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3396.99 Safari/537.36");
        add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3396.99 Safari/537.36");
        add("Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML); like Gecko); Safari/419.3) Arora/0.6");
        add("Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5");
        add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML); like Gecko) Chrome/19.0.1036.7 Safari/535.20");
    }};

    private static List<String> PHONE_UA_LIST = new ArrayList<String>() {{
        add("Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36");
        add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        add("Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Mobile Safari/537.36");
        add("Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Mobile Safari/537.36");
        add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/10.0 Mobile/15A372 Safari/604.1");
        add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/604.1");
        add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1");
    }};

    public static String get(String url, Map<String, String> headMap) throws Exception {
        AtomicBoolean httpsSign = new AtomicBoolean(false);
        if (url.contains(HTTPS)) {
            httpsSign.set(true);
        }
        URLConnection connection = null;
        try {
            connection = buildURLConnection(url, GET, headMap);
            connection.connect();
            return getResponse(connection);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (httpsSign.get()) {
                ((HttpsURLConnection) connection).disconnect();
            } else {
                ((HttpURLConnection) connection).disconnect();
            }
        }
    }

    public static String get(String url) throws Exception {
        return get(url, null);
    }

    public static String post(String url, String body, Map<String, String> headMap) throws Exception {
        AtomicBoolean httpsSign = new AtomicBoolean(false);
        if (url.contains(HTTPS)) {
            httpsSign.set(true);
        }
        URLConnection connection = null;
        try {
            connection = buildURLConnection(url, body, headMap);
            if (body != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), DEFAULT_CHARSET);
                writer.write(body);
                writer.flush();
                writer.close();
            }
            return getResponse(connection);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (httpsSign.get()) {
                ((HttpsURLConnection) connection).disconnect();
            } else {
                ((HttpURLConnection) connection).disconnect();
            }
        }
    }

    public static String post(String url) throws Exception {
        return post(url, null, null);
    }

    private static String getResponse(URLConnection connection) throws Exception {
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
            String line;
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
    }


    /**
     * 构建connection
     *
     * @param urlStr
     * @param method
     * @param headMap
     * @return
     * @throws Exception
     */
    private static URLConnection buildURLConnection(String urlStr, String method, Map<String, String> headMap) throws Exception {
        URL url = new URL(urlStr);
        AtomicBoolean httpsSign = new AtomicBoolean(false);
        if (urlStr.contains(HTTPS)) {
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
//        connection.setRequestProperty("Accept-Charset", DEFAULT_CHARSET);
        if (headMap != null && !headMap.isEmpty()) {
            for (String property : headMap.keySet()) {
                connection.setRequestProperty(property, headMap.get(property));
            }
        }
        return connection;
    }

    public static Map<String,String> getPCUserAgentHeadMap(){
        Map<String, String> map = new HashMap<String, String>();
        Collections.shuffle(PC_UA_LIST);
        map.put(UA,PC_UA_LIST.get(0));
        return map;
    }

    public static Map<String,String> getPhoneUAHeadMap(){
        Map<String, String> map = new HashMap<String, String>();
        Collections.shuffle(PHONE_UA_LIST);
        map.put(UA,PHONE_UA_LIST.get(0));
        return map;
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
