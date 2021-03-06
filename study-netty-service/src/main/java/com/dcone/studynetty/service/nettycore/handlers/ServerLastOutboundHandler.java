package com.dcone.studynetty.service.nettycore.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.springframework.context.annotation.Configuration;


/**
 * Outbound表示服务器发送的handler
 *
 * @author Jesse
 * @email xiaocpa@digitalchina.com
 * @date 2020-7-22
 */
@Configuration
public class ServerLastOutboundHandler extends ChannelOutboundHandlerAdapter {

    /**
     * 服务端要传递消息的方法
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //将传递过来的内容转换为ByteBuf对象
        ByteBuf buf = (ByteBuf) msg;
        //和文件IO一样，用一个字节数组读数据
        byte[] reg = new byte[buf.readableBytes()];
        buf.readBytes(reg);
        String body=new String(reg,"UTF-8");
        String respMsg = body+"\n1.吃饭 2.睡觉";
        System.out.println("服务端要发送的消息是：\n"+respMsg);
        ByteBuf respByteBuf = Unpooled.copiedBuffer(respMsg.getBytes());
        ctx.write(respByteBuf);
        //ctx.write()方法执行后，需要调用flush()方法才能令它立即执行
        ctx.flush();
    }
}