package com.genflat.mapper;/*
 * Copyright (c) 2015.
 * 游戏服务器核心代码编写人石头哥哥拥有使用权
 * 最终使用解释权归创心科技所有
 * 联系方式：E-mail:13638363871@163.com ;
 * 个人博客主页：http://my.oschina.net/chenleijava
 * powered by 石头哥哥
 */

import java.io.File;

/**
 * @author 石头哥哥
 *         </P>
 *         Date:   2015/6/2
 *         </P>
 *         Time:   9:34
 *         </P>
 *         Package: dcServer-parent
 *         </P>
 *         <p/>
 *         注解：
 *         生成客户端controller 和消息映射。
 *         修改客户端flatc生成头文件，将msgID方法修改为static方便获取msgID
 *         修改服务器端flatc生成的java文件， 将msgID方法修改为static方便获取msgID
 */
public class GenFlatMapper {


    /**
     * default gen c++
     * args[0] -l
     * args[1]  (java ,cpp)
     * args[2] -c
     * args[3]  (controller file path or java files)
     * args[4] -f
     * args[5] fbs file path
     * args[6] -g
     * args[7] genflat of cppfile      use change msgID
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String show =
                "1.生成客户端controller 和消息映射。\n" +
                        "2.修改客户端flatc生成头文件，将msgID方法修改为static方便获取msgID\n" +
                        "3.修改服务器端flatc生成的java文件， 将msgID方法修改为static方便获取msgID\n\n" +
                        "use cmd：java -jar genflatMapper.jar -l cpp -c controllerpath -f fbspath \n" +
                        "-l ： cpp ,ljava ;\n" +
                        "-c:  game controller path or java files；\n" +
                        "-f: game of fbs path；\n";

        System.out.println(show);

        if (args.length != 0) {
//            if (args.length < 6) {
//                System.err.println(show);
//                System.exit(0);
//            }
            String l = args[1];
            String controllerpath = args[3];
            if (l.equals("ljava")) {
                GenFlatJava.genJavaMapper(controllerpath);
            } else if (l.equals("cpp")) {
                String fbspath = args[5];
                GenFlatCpp.genCppMapper(fbspath, controllerpath, args[7]);
            }

        } else {
            // this is test interface
            try {
                //gen current file
                System.out.println("warn this is test interface ,please look readme!");
                System.out.println("not found args[] path ,tools will gen to current files,if your fbs in res file!!!");
                File file = new File("./controller");
                if (!file.exists()) file.mkdirs();
                //find fbs
                GenFlatCpp.genCppMapper("res", "./controller", "res");
                GenFlatJava.genJavaMapper("res");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

}
