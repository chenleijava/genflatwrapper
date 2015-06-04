/*
 * Copyright (c) 2015.
 * 游戏服务器核心代码编写人石头哥哥拥有使用权
 * 最终使用解释权归创心科技所有
 * 联系方式：E-mail:13638363871@163.com ;
 * 个人博客主页：http://my.oschina.net/chenleijava
 * powered by 石头哥哥
 */

package com;

import com.dc.server.flatgen.LoginTestResponse;

/**
 * @author 石头哥哥
 *         </P>
 *         Date:   2015/6/4
 *         </P>
 *         Time:   13:09
 *         </P>
 *         Package: dcServer-parent
 *         </P>
 *         <p/>
 *         注解：
 */
public class Test {
    public static void main(String[] args) {
       int msgID= LoginTestResponse.msgID();
        System.out.println(msgID);
    }
}
