package com.jxmk.connection.cabinet;

import com.jxmk.connection.cabinet.handler.MessageHandler;
import com.jxmk.connection.cabinet.service.DeviceService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class TcpServerApplication {

    @Value("${server.tcp.port}")
    private int port;

    private final DeviceService deviceService;
    private final Map<Integer, MessageHandler> messageHandlers;
    private final Executor messageHandlerExecutor;

    public static void main(String[] args) {
        SpringApplication.run(TcpServerApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
        return event -> {
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1); // 1 个线程处理连接
            NioEventLoopGroup workerGroup = new NioEventLoopGroup(); // 默认线程数 = CPU 核心数 * 2

            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024) // 定义服务端处理连接请求的队列大小。适用于大量客户端并发连接的场景
                        .childOption(ChannelOption.SO_KEEPALIVE, true) // 启用 TCP 的心跳机制，检测长时间未活动的连接是否仍然可用
                        .childOption(ChannelOption.TCP_NODELAY, true) // 关闭 Nagle 算法，立即发送小数据包以降低延迟
                        .childOption(ChannelOption.SO_SNDBUF, 32 * 1024) // 在低延迟场景下，建议设置为 16 KB 到 64 KB 的范围，结合实际业务需求和测试结果调整，避免盲目追求极限配置
                        .childOption(ChannelOption.SO_RCVBUF, 32 * 1024) // 在低延迟场景下，建议设置为 16 KB 到 64 KB 的范围，结合实际业务需求和测试结果调整，避免盲目追求极限配置
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline()
                                        // 进站⬇️
                                        .addLast(new JsonObjectDecoder())
                                        .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                        .addLast(new TcpServerHandler(deviceService, messageHandlers, messageHandlerExecutor))
                                        // 出站⬆️
                                        .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                        // 空闲检测
                                        .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.MINUTES)) // 10分钟没有读取到数据则触发
                                        .addLast(new HeartbeatHandler());
                            }
                        });
                serverBootstrap.bind(port).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("TcpServer启动失败", e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        };
    }
}
