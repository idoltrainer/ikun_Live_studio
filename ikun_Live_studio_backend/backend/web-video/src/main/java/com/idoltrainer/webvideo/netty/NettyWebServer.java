package com.idoltrainer.webvideo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyWebServer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline=ch.pipeline();
                    //http编码解码器
                    pipeline.addLast(new HttpServerCodec());
                    //分块传输
                    pipeline.addLast(new ChunkedWriteHandler());
                    //块聚合
                    pipeline.addLast(new HttpObjectAggregator(1024*1024));
                    //进入聊天室的http处理器
                    pipeline.addLast(new LiveChatHandler());
                    //协议处理器
                    pipeline.addLast(new WebSocketServerProtocolHandler("/channel"));
                    //自定义的处理器
                    pipeline.addLast(new HttpHandler());
                }
            });
            System.out.println("netty启动：8866");
            ChannelFuture channelFuture=bootstrap.bind(8866).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}

