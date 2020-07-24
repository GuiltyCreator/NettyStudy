package com.dcone.studynetty.service.nettycore;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * netty初始化
 * @author Jesse
 * @email xiaocpa@digitalchina.com
 * @date 2020-7-22
 */
@Slf4j
@Configuration
public class NettyServer {

    /**
     * 端口号
     */
    @Value("${netty.websocket.port}")
    private int port;

    /**
     * 绑定的网卡
     */
    @Value("${netty.websocket.ip}")
    private String ip;

    @Resource
    private NettyChannelInitializer nettyChannelInitializer;

    public void startServer() {
        log.info("服务端启动成功");
        //创建两个线程组，用于接收客户端的请求任务,创建两个线程组是因为netty采用的是反应器设计模式
        //反应器设计模式中bossGroup线程组用于接收
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //workerGroup线程组用于处理任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建netty的启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //创建一个通道
        ChannelFuture f = null;
        try {
            //设置线程组
            bootstrap.group(bossGroup, workerGroup)
                    //.localAddress(new InetSocketAddress(this.ip, this.port))
                    //设置通道为非阻塞IO
                    .channel(NioServerSocketChannel.class)
                    //设置日志
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //接收缓存
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    //是否保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //是否进行TCP无延迟连接
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(nettyChannelInitializer);

            //阻塞端口号，以及同步策略
            f = bootstrap.bind(port).sync();
            //关闭通道
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}