package com.dcone.studynetty.service.nettycore;

import com.dcone.studynetty.service.nettycore.handlers.ServerInboundGetTimeHandler;
import com.dcone.studynetty.service.nettycore.handlers.ServerInboundHandler;
import com.dcone.studynetty.service.nettycore.handlers.ServerLastOutboundHandler;
import com.dcone.studynetty.service.nettycore.handlers.ServerOutboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * netty初始化
 * @author Jesse
 * @email xiaocpa@digitalchina.com
 * @date 2020-7-22
 */

@Slf4j
@Component
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 四个处理请求的逻辑类
     */
    @Resource
    ServerInboundHandler serverInboundHandler;

    @Resource
    ServerInboundGetTimeHandler serverInboundGetTimeHandler;

    @Resource
    ServerLastOutboundHandler serverLastOutboundHandler;

    @Resource
    ServerOutboundHandler serverOutboundHandler;

    /**
     * URI路径
     */
    @Value("${netty.websocket.path}")
    private String path;

    /**
     * 消息帧最大体积
     */
    @Value("${netty.websocket.max-frame-size}")
    private long maxFrameSize;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //ChannelPipeline是handler的任务组，里面有多个handler
        ChannelPipeline pipeline = socketChannel.pipeline();
        //逻辑处理类
        //先执行OutboundHandler再执行InboundHandler
        pipeline.addLast(serverLastOutboundHandler);
        pipeline.addLast(serverOutboundHandler);
        pipeline.addLast(serverInboundHandler);
        pipeline.addLast(serverInboundGetTimeHandler);
        pipeline.addLast(new WebSocketServerProtocolHandler(path, null, true, maxFrameSize));
    }
}
