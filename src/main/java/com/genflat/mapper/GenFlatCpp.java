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
public class GenFlatCpp {

    /**
     * 客户端处理消息类型 注意消息命名XXXResponse--- 服务器响应消息类型
     */
    private static final String client = "Response";

    /**
     * 用于生成处理消息类型引用的变量名  注意是静态类型
     */
    private static final String controllorname = "controller";


    /**
     * 对应客户端处理来自服务器端消息命名
     * xxxResponse
     * <p/>
     * table LoginResponse{
     * objectID:int=2;
     * username:string;
     * }
     * <p/>
     * 生成处理来自服务器端的  Response 类型消息    <p/>
     * 自动生成映射的mapper  如：controller_map[msgID<LoginResponse>()]=LoginResponseController::controller; <p/>
     * 同时生成对应的controller处理部分  <p/>
     * 逻辑编写部位：virtual void dispatcherMessage(char *data);   <p/>
     *
     * @param fbspath        flatbuffer fbs 所在文件路径        <p/>
     * @param controllerpath 对应controller生成路径  <p/>
     * @param cppHeaderPath  对应flatbuffer生成头文件位置  主要用于修改msgID 作为一个静态的方法 方便调用
     * @throws java.io.IOException
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void genCppMapper(String fbspath, String controllerpath, String cppHeaderPath) throws Exception {
        File file;
        //get fbs table list
        file = new File(fbspath);
        String[] fbsNames = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".fbs");
            }
        });
        if (fbsNames == null) {
            throw new Exception("fbs not found in res!!!");
        }

        ArrayList<String> fbsPaths = new ArrayList<String>();

        for (String fbsname : fbsNames) {
            fbsPaths.add(fbspath + "/" + fbsname);
        }

        //读取所有fbs文件 获取文件中的table name
        ArrayList<String> mapperTable = new ArrayList<String>();

        //loading fbs and filter table name
        for (String fbs : fbsPaths) {
            file = new File(fbs);
            RandomAccessFile randomAccessFile;
            try {
                randomAccessFile = new RandomAccessFile(file, "r");     //O_RDONLY
                String txt = "";
                while (txt != null) {
                    txt = randomAccessFile.readLine();
                    if (txt != null
                            && txt.startsWith("table")) {
                        String temp = txt.substring(0, txt.lastIndexOf("{"))
                                .replace("table", "");
                        if (temp.endsWith(client)) mapperTable.add(temp.trim());

                    }
                }
                randomAccessFile.close();  //close   randomAccessFile
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fbsPaths.clear();


        ArrayList<String> controllerList = new ArrayList<String>();


        //for icontroller
        StringBuilder mapperBuilder = new StringBuilder();
        //  controller_map[LoginFlatController::controller->msgID()]=LoginFlatController::controller;
        for (String table : mapperTable) {
            String controllername = table + "Controller";
            controllername = controllername.replace(client, "");
            mapperBuilder.append("  controller_map[")
//                    .append(controllername)
                    .append(table)
                    .append("::")
//                    .append(controllorname + "->msgID()")
                    .append("msgID()")
                    .append("]")
                    .append("=")
                    .append(controllername)
                    .append("::")
                    .append(controllorname)
                    .append(";\n");
            controllerList.add(controllername);//add controller
        }

        mapperTable.clear();


        ArrayList<String> genIcontrollerHeader = new ArrayList<String>();
        genIcontrollerHeader.addAll(controllerList);

        File controllerFile = new File(controllerpath);
        if (!controllerFile.exists()) controllerFile.mkdirs();

        /**
         * don't override  had gen xxxcontroller
         * because  maybe had logic in it!!!!
         */
        String[] exsiteController = controllerFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".cpp") && !name.endsWith("IController.cpp");
            }
        });

        for (String name : exsiteController) {
            String temp = name.substring(0, name.indexOf("."));
            if (controllerList.contains(temp)) {
                controllerList.remove(temp);
            }
        }


        StringBuilder builderCpp = new StringBuilder();
        //gen .h file
        for (String controller : controllerList) {
            String define = controller + "_h";
            builderCpp.append("//this is gen file,don't modify it \n")
                    .append("#ifndef")
                    .append(" ").append(define).append("\n")
                    .append("#define")
                    .append(" ").append(define).append("\n")
                    .append("#include \"IController.h\"")
                    .append("\n")
                    .append("class ")
                    .append(controller)
                    .append(":").append("public IController{")
                    .append("\n")
                    .append("public:")
                    .append("\n")
                    .append("static ")
                    .append(controller)
                    .append(" *")
                    .append(controllorname)
                    .append(";\n")
                    .append("public:\n")
                    .append("/**\n" +
                            "负责处理数据分发    处理来自服务器数据\n" +
                            "*/\n")
                    .append("virtual void dispatcherMessage(char *data);\n")
//                    .append("/**\n" +
//                            " * 获取对应的消息ID\n" +
//                            " */\n")
//                    .append("virtual int msgID() override;\n")
                    .append("};\n")
                    .append("#endif")
                    .append(" //").append(define);
            byte[] cpp_h = builderCpp.toString().getBytes();
            builderCpp.delete(0, cpp_h.length);

            //write header
            controllerFile = new File(controllerpath);
            if (!controllerFile.exists()) {
                controllerFile.mkdirs();
            }
            FileUtils.writeByteArrayToFile(new File(controllerpath + "/" + controller + ".h"), cpp_h);


            //gen controller cpp file
            String tableName = controller.substring(0, controller.indexOf("Controller")) + client;
            String lowTablename = tableName.toLowerCase();
            builderCpp.append("//this is gen file,logic controler \n")
                    .append("\n")
                    .append("#include")
                    .append(" ")
                    .append("\"")
                    .append(controller).append(".h")
                    .append("\"")
                    .append("\n")
                    .append(controller).append("")
                    .append("*")
                    .append(controller)
                    .append("::")
                    .append(controllorname).append("=")
                    .append("new ").append(controller)
                    .append(";\n")
                    .append("\n")
//                    .append("int ")
//                    .append(controller + "::msgID(){\n")
//                    .append(" flatbuffers::FlatBufferBuilder builder;\n" +
//                            " builder.Finish(Create" + tableName + "(builder));\n" +
//                            " auto msgID = flatbuffers::GetRoot<" + tableName + ">(builder.GetBufferPointer())->msgID();\n" +
//                            " builder.ReleaseBufferPointer();\n" +
//                            " return msgID;\n")
//                    .append("}\n\n")
                    .append("/**\n" +
                            "负责处理数据分发    处理来自服务器数据\n" +
                            " */\n")
                    .append("void ").append(controller).append("::")
                    .append("dispatcherMessage(char *data) {\n\n").append("   auto ")
                    .append(lowTablename)
                    .append("=getRoot<")
                    .append(tableName)
                    .append(">(data);\n")
                    .append("  //TODO::处理来自服务器数据")
                    .append(tableName)
                    .append("\n\n")
                    .append("}");

            byte[] cpp = builderCpp.toString().getBytes();
            builderCpp.delete(0, cpp.length);
            FileUtils.writeByteArrayToFile(new File(controllerpath + "/" + controller + ".cpp"), cpp);
        }


        //IController always override
        // gen IController  generated header name
        ArrayList<String> generatedHeaders = new ArrayList<String>();
        String tempname;
        String tempnameup;
        char first;
        for (String name : fbsNames) {
            tempname = name.substring(1, name.lastIndexOf("."));
            tempnameup = name.substring(0, name.lastIndexOf(".")).toUpperCase();
            first = tempnameup.charAt(0);
            generatedHeaders.add(first + tempname);
        }


        builderCpp.append("//warn this is gen file,don't modify it \n")
                .append("#ifndef ").append("_ICONTROLLER_H\n")
                .append("#define ").append("_ICONTROLLER_H\n")
                .append("#include <map>\n")
                .append("#include \"cocos2d.h\"\n");
        //append generated header include
        for (String name : generatedHeaders) {
            builderCpp.append("#include \"flatgen/").append(name)
                    .append("_generated.h\"").append("\n");
        }
        generatedHeaders.clear();

        builderCpp.append("using  namespace cocos2d;\n")
                .append("using namespace gen;\n")
                .append("using  namespace std;\n")
                .append("#ifndef delete_array\n")
                .append("#define delete_array(p)     do { if(p) { delete[] (p); (p) = nullptr; } } while(0)\n")
                .append("#endif //delete_array\n")
                .append("class IController {\n")
                .append("\n")
                .append("public:\n")
                .append("  IController() { }\n")
                .append(" /**\n" +
                        "  注册消息ID 和对应处理controller的关系\n" +
                        "  以便于快速索引处理\n" +
                        "  */\n")
                .append("  static std::map<int, IController *> controller_map;\n")
                .append("  /**\n" +
                        "  负责处理数据分发\n" +
                        "  */\n")
                .append("  virtual void dispatcherMessage(char *data);\n")
//                .append("/**\n" +
//                        " * 获取对应的消息ID\n" +
//                        " */\n")
//                .append(" virtual int msgID()=0;\n")
//                .append(" /**\n" +
//                        " * @Author 石头哥哥, 15-06-01 23:06:00\n" +
//                        " *\n" +
//                        " * @brief  获取生成消息对应的msgID\n" +
//                        " *\n" +
//                        " * @return <#return value description#>\n" +
//                        " */\n")
//                .append("  template<typename T> static int msgID();\n")
                .append("  /**\n" +
                        "  转化对应消息为flatbuffers实体\n" +
                        "  data+4: 消息ID 占用4 字节\n" +
                        "  */\n")
                .append("  template <typename  T> const  T* getRoot(char* data);\n")
                .append(" /**\n" +
                        "  * @Author 石头哥哥, 15-06-01 23:06:26\n" +
                        "  *\n" +
                        "  * @brief  注册controoler and mapper\n" +
                        "  */\n")
                .append("  static void registerMapperController();\n")
                .append("};\n")
//                .append(" template<typename T>\n")
//                .append("inline  int IController::msgID() {\n" +
//                        "   flatbuffers::FlatBufferBuilder builder;\n" +
//                        "   builder.Finish(CreateLoginRequest(builder));\n" +
//                        "   auto msgID = flatbuffers::GetRoot<T>(builder.GetBufferPointer())->msgID();\n" +
//                        "   builder.ReleaseBufferPointer();\n" +
//                        "   return msgID;\n" +
//                        "}\n")
                .append("template<typename T>\n" +
                        "inline const T *IController::getRoot(char *data) {\n" +
                        "    return flatbuffers::GetRoot<T>(data+4);\n" +
                        "}")
                .append("\n").append("#endif//_ICONTROLLER_H");

        byte[] data = builderCpp.toString().getBytes();
        builderCpp.delete(0, data.length);
        FileUtils.writeByteArrayToFile(new File(controllerpath + "/IController.h"), data);


        //gen icontroller cpp
        builderCpp.append("//this is gen file,don't modify it \n")
                .append("#include \"IController.h\"\n");

        for (String controller : genIcontrollerHeader) {
            builderCpp.append("#include ")
                    .append("\"").append(controller).append(".h")
                    .append("\"\n");
        }
        genIcontrollerHeader.clear();
        controllerList.clear();
        builderCpp.append("\n")
                .append("map<int, IController *> IController::controller_map;\n")
                .append("void IController::dispatcherMessage(char *data) {}\n")
                .append("/**\n" + " * 注册controller mapper\n" + " */\n" + "void IController::registerMapperController() {\n" +
                        "  log(\"%s\", \"registerMapperController开始注册controller... ...\");\n")
                .append(mapperBuilder.toString()).append("}\n");
        data = builderCpp.toString().getBytes();
        builderCpp.delete(0, data.length);
        FileUtils.writeByteArrayToFile(new File(controllerpath + "/IController.cpp"), data);

        System.out.println("-------gen success cpp controller files success---------------------");


        System.out.println("-----begin make cpp file msgID change to static --------");

        File cppfile = new File(cppHeaderPath);
        fbsNames = cppfile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".h");
            }
        });

        ArrayList<byte[]> datacppList = new ArrayList<byte[]>();
        for (String fbsname : fbsNames) {
            RandomAccessFile randomAccessFile;
            try {
                File flatfile_h = new File(cppHeaderPath + "/" + fbsname);
                randomAccessFile = new RandomAccessFile(flatfile_h, "r");     //O_RDONLY
                String txt = "";
                while (txt != null) {
                    txt = randomAccessFile.readLine();
                    if (txt != null) {
                        if (txt.contains("int32_t msgID() const")
                                && !txt.contains("static")) {
                            int default_value = Integer.parseInt(txt.substring(txt.lastIndexOf(",") + 1, txt.lastIndexOf(")")).trim());
                            txt = "  static int32_t msgID(){return " + default_value + ";}";
                        }
                        txt += "\n";
                        datacppList.add(txt.getBytes());
                    }
                }
                randomAccessFile.close();  //close   randomAccessFile
                flatfile_h.delete();  //deleted origin file
            } catch (IOException e) {
                e.printStackTrace();
            }

            file = new File(cppHeaderPath + "/" + fbsname);
            for (byte[] dd : datacppList) {
                FileUtils.writeByteArrayToFile(file, dd, true);
            }
            //done  for next
            datacppList.clear();
        }

        System.out.println("----- make cpp file msgID method change to static success ---");
    }

    public static void main(String[] args) {
        String txt = "int32_t msgID() const { return GetField<int32_t>(4, 108); }".trim();
        int lastIndex = txt.lastIndexOf(")");
        int default_value = Integer.parseInt(txt.substring(txt.lastIndexOf(",") + 1, txt.lastIndexOf(")")).trim());
        System.out.println(default_value);
    }


}
