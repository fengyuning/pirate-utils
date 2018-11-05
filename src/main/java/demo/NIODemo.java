package demo;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class NIODemo {

    public static void main(String[] args) throws IOException {

        RandomAccessFile aFile = new RandomAccessFile("C:/down/httpclient-2018-10-23-15.stat", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {

            buf.flip();

            while(buf.hasRemaining()){
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();

//        IOTest();
//        NIOTest();

    }

    public static void IOTest() {
        long start = System.currentTimeMillis();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(
                    new FileInputStream("C:/down/httpclient-2018-10-23-15.stat"));
            bos = new BufferedOutputStream(
                    new FileOutputStream("C:/down/IO.txt"));
            //定义一个缓冲区
            byte[] buffer = new byte[1024];
            int flag;
            while ((flag = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, flag);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static void NIOTest() {
        long start = System.currentTimeMillis();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        //            fos = new FileOutputStream("C:/down/NIO.txt");
        try {
            //创建selector
            Selector selector = Selector.open();
            //创建channel
            fis = new FileInputStream("C:/down/httpclient-2018-10-23-15.stat");
            FileChannel channel = fis.getChannel();



            ByteBuffer buffer = ByteBuffer.allocate(48);
            int flag = channel.read(buffer);
            while (flag != -1) {

                buffer.flip(); //ready to read

                while (buffer.hasRemaining()) {
                    System.out.println(buffer.get());
                }

                buffer.clear(); //ready to write
                flag = channel.read(buffer);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
