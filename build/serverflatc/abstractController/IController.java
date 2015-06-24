/*
 * Copyright (c) 2015.
 * 游戏服务器核心代码编写人石头哥哥拥有使用权
 * 最终使用解释权归创心科技所有
 * 联系方式：E-mail:13638363871@163.com ;
 * 个人博客主页：http://my.oschina.net/chenleijava
 * powered by 石头哥哥
 */

package com.dc.gameserver.servercore.controller.abstractController;


import com.dc.gameserver.model.character.PlayerInstance;
import com.google.protobuf.MessageLite;

import java.io.Serializable;

/**
 * @author :陈磊 <br/>
 *         Date: 12-12-14<br/>
 *         Time: 下午2:36<br/>
 *         connectMethod:13638363871@163.com<br/>
 *         buf封装 数据包操作类
 *         数据分发处理接口  以及处理buffer数据
 *         <p/>
 *         数据包协议类型：1.包长+包类型【ID】+包体（消息部分）
 *         1）包体：length+msg+length+msg ... ...
 *         注意：操作ChannelBuffer的getxx()方式，method does not modify readerIndex
 *         readXXX(),writeXX(),将会改变读写指针
 */
public interface IController extends Serializable {

    /**
     * 使用数组来存储数据包和处理函数的关系，通过数组下标索引来找到相应的操作处理函数的引用*
     * <p/>
     * ref function of    :
     * DispatchMessageLit(final MessageLite messageLite, final PlayerInstance player)
     */
    static final IController[] GAME_CONTROLLERS = new IController[4500];//游戏数据包

    /**
     * 服务器初始化的时候加载MessageLite[]
     * <p/>
     * proto buf  实体
     */
    static final MessageLite MESSAGE_LITE[] = new MessageLite[4500];

    /**
     * messageLite数据结构体分发
     *
     * @param messageLite 数据载体
     * @param player      active object
     * @throws Exception 注意messageLite应该递交给GC直接处理  ，set null
     */
    void DispatchMessageLit(final MessageLite messageLite, final PlayerInstance player)
            throws Exception;



    /************for flatbuffers************************************************************************/

    /**
     * fast get ref which handle  data method
     * ${void DispatchFlatBuffer(final byte[] data, final PlayerInstance player)}
     * game_controllers[msgID]. DispatchFlatBuffer(data,player);
     * wrap table ,data[4,data.length-4];  msgID hand occupy  4
     */
    static final IController game_controllers[] = new IController[2];

    /**
     * 基于flatBuffers 结构数据传输
     *
     * @param data   cast subclass of table .
     * @param player game session
     * @throws Exception
     */
    void DispatchFlatBuffer(final byte[] data, final PlayerInstance player)
            throws Exception;

}
