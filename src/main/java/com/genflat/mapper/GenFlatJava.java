package com.genflat.mapper;/*
 * Copyright (c) 2015.
 * 游戏服务器核心代码编写人石头哥哥拥有使用权
 * 最终使用解释权归创心科技所有
 * 联系方式：E-mail:13638363871@163.com ;
 * 个人博客主页：http://my.oschina.net/chenleijava
 * powered by 石头哥哥
 */

import org.apache.commons.io.FileUtils;

import java.io.*;
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
 *         注解：用于将flatbuffer生成的java文件 中获取msgID 转化为对应的static方法。
 *         生成对应服务器端处理游戏逻辑controller 模块，完成对应的controller 和msgID 的注册 映射关系
 */
public class GenFlatJava {


    /**
     * 服务器端处理来自客户端 xxxRequest,这里生成对应的xxxRequestController
     * 服务器端基于spring中PostConstruct() 自动调用。
     * <p/>
     * 服务器端 存在接口IController和AbstractController
     * 在PostConstruct() 完成对应msgID 和controller注册
     * 如：
     * game_controllers[LoginRequest.msgID()] = this;
     * 其中 game_controllers  为IController的数组，
     * AbstractController 中为一些通用类型方法。
     * 如构建bytebuffer ，warp  byte arrays
     * <p/>
     * 生成处理业务逻辑controller格式如
     * <p/>
     * 这套生成工具同样可应用于rpc
     *
     * @param controllerPath 生成controller 位置
     * @param fbspath        flat buffers 描述文件夹
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void genController(String controllerPath, String fbspath) throws Exception {
        System.out.println("begin gen logic controller....");
        //解析fbs中的xxxRequest
        String[] fbsfilesArray = new File(fbspath).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".fbs");
            }
        });

        //遍历处理fbs文件内容
        String fbs;
        ArrayList<String> requestNames = new ArrayList<String>();
        RandomAccessFile randomAccessFile;
        File fbsfile;
        for (String fbsname : fbsfilesArray) {
            fbs = fbspath + "/" + fbsname;
            fbsfile = new File(fbs);
            randomAccessFile = new RandomAccessFile(fbsfile, "r");
            String txt = "";
            while (txt != null) {
                txt = randomAccessFile.readLine();
                if (txt != null
                        && txt.contains("Request")
                        &&txt.contains("table")
                        &&!txt.contains("//")) {
                    //get request  table of name
                    txt = txt.replace("table", "").replace("{", "").trim();
                    requestNames.add(txt);
                }
            }
        }

        File controllerfiles = new File(controllerPath);
        if (!controllerfiles.exists())controllerfiles.mkdirs();
        String[] controllers = controllerfiles.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        });
        //过滤掉已经存在的controller模块，避免已经存在的逻辑被覆盖
        if (controllers!=null){
            for (String filename : controllers) {
                String temp = filename.substring(0, filename.indexOf(".")).replace("Controller", "Request").trim();//
                if (requestNames.contains(temp)) requestNames.remove(temp);
            }
        }


        //生成对应的controller 模块代码
        StringBuilder builder = new StringBuilder();
        //LoginRequest
        String filepath;
        String controllerfilename;
        for (String controller : requestNames) {
            controllerfilename = controller.replace("Request", "")
                    .trim()+"Controller";
            filepath = controllerPath + "/" + controllerfilename+".java";
            builder.append("import org.springframework.stereotype.Service;\n");
            builder.append("\n\n");
            builder.append("@Service\n" + "public class ")
                    .append(controllerfilename)
                    .append(" extends AbstractController {\n")
                    .append("\n")
                    .append("    /**\n")
                    .append("     * spring 容器初始化 加载并初始化相应控制器处理句柄\n")
                    .append("     * 非spring环境下 可以采用静态的初始化方案\n")
                    .append("     */\n")
                    .append("    @Override\n")
                    .append("    public void PostConstruct() {\n")
                    .append("        game_controllers[")
                    .append(controller).append(".msgID()] = this;\n").append("    }\n").append("\n").append("\n").append("\n").append("    /**\n").append("     * 基于flatBuffers 结构数据传输\n").append("     *\n").append("     * @param data   cast subclass of table .         msgID + 游戏数据\n").append("     * @param player game session\n").append("     * @throws Exception\n").append("     */\n").append("    @Override\n").append("    public void DispatchFlatBuffer(byte[] data, PlayerInstance player) throws Exception {\n").append("\n").append("    }\n").append("\n").append("}");
            byte[] tempdata = builder.toString().getBytes();
            builder.delete(0, tempdata.length);// for next
            FileUtils.writeByteArrayToFile(new File(filepath), tempdata, true);
        }
        requestNames.clear();

        System.out.println(" gen logic controller success....");

    }

    /**
     * @param flatgenPath flatbuffers 生成java文件位置
     * @throws java.io.IOException
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void genJavaMapper(String flatgenPath) throws IOException {

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
        RandomAccessFile randomAccessFile;
        for (String filename : javaFiles) {
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
                            int value = Integer.parseInt(txt.substring(txt.indexOf(":") + 1, txt.lastIndexOf(";")).trim());
                            txt = "  public static int msgID(){return " + value + ";}";
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


    public static void main(String[] args) throws Exception {
//        String txt = "public int msgID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 108; } }".trim();
//        int default_value = Integer.parseInt(txt.substring(txt.indexOf(":") + 1, txt.lastIndexOf(";")).trim());
//        txt = " public static int msgID(){return " + default_value + ";}";
//        System.out.println(default_value);
//        System.out.println(txt);
//        System.out.println(msgID());
//
        String txt = "table LoginRequest";
        txt = txt.replace("table", "").replace("{", "").trim();
        System.out.println(txt);

        txt = "TestFlatBufferController.java";
        String temp = txt.substring(0, txt.indexOf(".")).replace("Controller", "").trim();//
        System.out.println(temp);


       String path= ClassLoader.getSystemResource("Game.fbs").getPath().replace("Game.fbs","").trim();
        genController("./controller", path);

    }

    public static int msgID() {
        return 108;
    }
}
