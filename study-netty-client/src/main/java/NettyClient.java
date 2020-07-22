import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * netty客户端
 *
 * @author Jesse
 * @email xiaocpa@digitalchina.com
 * @date 2020-7-22
 */
public class NettyClient {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口号
     */
    private int port;

    /**
     * 构造函数
     *
     * @param host
     * @param port
     */
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startClient() {
        //客户端只需要创建一个线程就足够了
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //客户端启动类
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(group)
                    //设置通道类型
                    .channel(NioSocketChannel.class)
                    //设置IP和端口
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new NettyChannelInitializer());
            //阻塞通道
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        String host = "0.0.0.0";
        int port = 84;
        NettyClient nettyClient = new NettyClient(host, port);
        nettyClient.startClient();
    }

}
