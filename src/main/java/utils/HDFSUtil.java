package utils;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 测试HDFS
 *
 * @author fyn
 * @version 1.0
 */
public class HDFSUtil {

    private static Configuration config = new Configuration();
    //1.指定用的是hdfs文件系统

//

    //2.获取文件系统客户端对象
//        try {
//            FileSystem fs = FileSystem.get(config);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static FileSystem getDefalutFs() throws IOException {
        System.setProperty("HADOOP_USER_NAME", "root");
        config.set("fs.defaultFS", "hdfs://47.106.144.16:9000");
        return FileSystem.get(config);
    }

    /**
     * 上传测试
     */
    public static void upLoadTest() throws IOException {
        FileSystem fs = HDFSUtil.getDefalutFs();
        FileInputStream fis = new FileInputStream("C:\\down\\测试.txt");
        FSDataOutputStream fsos = fs.create(new Path("/user/fyn/111.txt"), true);
        IOUtils.copyBytes(fis, fsos, 4096);
        fs.close();
    }

    /**
     * 测试
     */
    public static void deleteTest() throws IOException {
        FileSystem fs = HDFSUtil.getDefalutFs();
        fs.delete(new Path("/user/root"), true);
        fs.delete(new Path("/user/fyn/222.xlsx"), true);
        fs.close();
    }

    public static void downLoadTest() throws IOException {
        FileSystem fs = HDFSUtil.getDefalutFs();
        //第一个false表示不删除源文件,后面的true使用本地文件系统
        fs.copyToLocalFile(false, new Path("/user/fyn/222.xlsx"),
                new Path("C:/down"), true);
        fs.close();
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        HDFSUtil.upLoadTest();
//        HDFSUtil.downLoadTest();
//        HDFSUtil.deleteTest();
    }

}
