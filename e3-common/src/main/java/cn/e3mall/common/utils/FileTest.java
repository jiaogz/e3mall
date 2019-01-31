package cn.e3mall.common.utils;

import java.io.*;
import java.util.Arrays;

public class FileTest {

    /**
     *1 按字节写入 FileOutputStream
     *
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public static void outputStreamTest(int count, String str) throws Exception{
        File f = new File("f:test1.txt");
        OutputStream os = null;
        try {
            os = new FileOutputStream(f);
            for (int i = 0; i < count; i++) {
                os.write(str.getBytes());
            }
            os.flush();
            System.out.println("file's long:" + f.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *2 按字节缓冲写入 BufferedOutputStream
     *
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public static void bufferedOutputTest(int count, String str) {
        File f = new File("f:test2.txt");
        BufferedOutputStream bos = null;
        try {
            OutputStream os = new FileOutputStream(f);
            bos = new BufferedOutputStream(os);
            for (int i = 0; i < count; i++) {
                bos.write(str.getBytes());
            }
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *3 按字符写入 FileWriter
     *
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public static void fileWriteTest(int count, String str) {
        File f = new File("f:test.txt");
        Writer writer = null;
        try {
            writer = new FileWriter(f);
            for (int i = 0; i < count; i++) {
                writer.write(str);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *4 按字符缓冲写入 BufferedWriter
     *
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public static void bufferedWriteTest(int count, String str) {
        File f = new File("f:test3.txt");
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            OutputStream os = new FileOutputStream(f);
            writer = new OutputStreamWriter(os);
            bw = new BufferedWriter(writer);
            for (int i = 0; i < count; i++) {
                bw.write(str);
            }
            bw.flush();
            if(f.exists()){
                f.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *5 按字符缓冲写入 BufferedWriter and BufferedOutputStream
     *
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public static void bufferedWriteAndBufferedOutputStreamTest(File file,int count, String str) {
        BufferedOutputStream bos=null;
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            OutputStream os = new FileOutputStream(file);
            bos=new BufferedOutputStream(os);
            writer = new OutputStreamWriter(bos);
            bw = new BufferedWriter(writer);
            for (int i = 0; i < count; i++) {
                bw.write(str);
            }
            bw.flush();
            if(file.exists()){
                file.delete();
                System.out.println("delete---");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *6 按字符缓冲写入 BufferedWriter and FileWriter
     *
     * @param count 写入循环次数
     * @param str 写入字符串
     */
    public static void bufferedWriteAndFileWriterTest(File file,int count, String str) {
        FileWriter fw=null;
        BufferedWriter bw = null;
        try {
            fw=new FileWriter(file);
            bw = new BufferedWriter(fw);
            for (int i = 0; i < count; i++) {
                bw.write(RandomValue.getChineseName()+" "+RandomValue.getTel()+str+"\n");
            }
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
//                if(file.exists()){
//                    file.delete();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Administrator\\Desktop\\外域邮件发件人.txt");
        String str = "迟天固 13305694297@wo.cn,阳宁博 13902354276@wo.cn,西冠 15106356769@wo.cn,";
//        bufferedWriteAndFileWriterTest(file,100000,str);

//        String[] s = str.split(" ");
//        System.out.println(s[0]);
//        String businessCategoryTypeId="";
//        System.out.println(! "BUSINESS_EXTERNAL".equals(businessCategoryTypeId));

//        System.out.println(str.substring(0,str.indexOf("@")));

//        System.out.println(str.toUpperCase());

        String[] strings = str.split(",");
//        for (int i = 0; i < 10; i++) {
//            int index = (int) (Math.random() * (strings.length));
//            System.out.println(index);
//        }
        int i = (int) (Math.random() * (strings.length));
        System.out.println(i);
        System.out.println(strings[strings.length-1]);
        strings[i] = strings[strings.length-1];
        strings = Arrays.copyOf(strings,strings.length-1);
        System.out.println(strings.length);
        System.out.println(Arrays.toString(strings));





    }
}
