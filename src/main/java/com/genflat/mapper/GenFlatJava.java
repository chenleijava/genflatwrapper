package com.genflat.mapper;/*
 * Copyright (c) 2015.
 * 游戏服务器核心代码编写人石头哥哥拥有使用权
 * 最终使用解释权归创心科技所有
 * 联系方式：E-mail:13638363871@163.com ;
 * 个人博客主页：http://my.oschina.net/chenleijava
 * powered by 石头哥哥
 */

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author 石头哥哥
 *         </P>
 *         Date:   2015/6/2
 *         </P>
 *         Time:   9:35
 *         </P>
 *         Package: dcServer-parent
 *         </P>
 *         <p/>
 *         注解：
 */
public class GenFlatJava {

    /**
     * @param flatgenPath flatbuffers 生成java文件位置
     * @throws java.io.IOException
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static  void genJavaMapper(String flatgenPath) throws IOException {

        System.out.println("-----begin make java file msgID change to static ---");

        File java_files = new File(flatgenPath);
        String[] javaFiles = java_files.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        });

        ArrayList<byte[]> datacppList = new ArrayList<byte[]>();
        String filePath;
        for (String filename : javaFiles) {
            RandomAccessFile randomAccessFile;
            try {
                filePath = flatgenPath + "/" + filename;
                File flatfile = new File(filePath);
                randomAccessFile = new RandomAccessFile(flatfile, "r");     //O_RDONLY
                String txt = "";
                while (txt != null) {
                    txt = randomAccessFile.readLine();
                    if (txt != null) {
                        //  public int msgID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 108; }
                        if (txt.contains(" public int msgID()")
                                && !txt.contains("static")) {
                            int value= Integer.parseInt(txt.substring(txt.indexOf(":")+1,txt.lastIndexOf(";")).trim());
                            txt ="  public static int msgID(){return "+value+";}";
                        }
                        txt += "\n";
                        datacppList.add(txt.getBytes());
                    }
                }
                randomAccessFile.close();  //close   randomAccessFile
                flatfile.delete();    //delete origin file
                for (byte[] dd : datacppList) {
                    FileUtils.writeByteArrayToFile(new File(filePath), dd, true);
                }
                datacppList.clear();//for next loop
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("----- make java file msgID method change to static success ---");
    }


    public static void main(String[] args) {
        String txt = "public int msgID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 108; } }".trim();
        int default_value= Integer.parseInt(txt.substring(txt.indexOf(":")+1,txt.lastIndexOf(";")).trim());
        txt =" public static int msgID(){return "+default_value+";}";
        System.out.println(default_value);
        System.out.println(txt);
        System.out.println(msgID());
    }
    public static int msgID(){return 108;}
}
