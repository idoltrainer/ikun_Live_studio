package com.idoltrainer.webvideo.netty;

import com.idoltrainer.webvideo.constant.ChatroomConstant;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.service.UserService;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

import static com.idoltrainer.webvideo.netty.AsyncTask.chatRoomsMap;

@Component
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private WebSocketServerHandshaker handshaker;

    @Resource
    private UserService userService;


    static {
        AttributeKey.newInstance(ChatroomConstant.IP);
        AttributeKey.newInstance(ChatroomConstant.USERNAME);
        AttributeKey.newInstance(ChatroomConstant.CHATROOM);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        //http解码
        QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
        String chatroomName = decoder.parameters().get("chatroom").get(0);
        //获取登录用户
        String nickName =  decoder.parameters().get("nickname").get(0);
        //获取用户id
        String userId = decoder.parameters().get("userId").get(0);
        //房主id
        String anchorId = decoder.parameters().get("userId").get(0);
        //判断
        if(chatroomName==null || "".equals(chatroomName) || nickName == null || "".equals(nickName) || !req.decoderResult().isSuccess()||!"websocket".equals(req.headers().get("Upgrade"))){
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            ChannelFuture future = ctx.channel().writeAndFlush(response);
            future.addListener(ChannelFutureListener.CLOSE);
        }else{
            //获取IP
            String ip=((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
            //本地ip的情况
            if("0:0:0:0:0:0:0:1".equals(ip)){
                ip="127.0.0.1";
            }
            //设置websocket的ip
            String username=nickName+"("+ip+")";
            ctx.channel().attr(AttributeKey.valueOf(ChatroomConstant.IP)).set(ip);
            ctx.channel().attr(AttributeKey.valueOf(ChatroomConstant.USERNAME)).set(username);
            ctx.channel().attr(AttributeKey.valueOf(ChatroomConstant.CHATROOM)).set(chatroomName);
            //WebSocket工厂
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    req.uri(), null, false);
            //新的连接
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory
                        .sendUnsupportedVersionResponse(ctx.channel());
            } else {
                //握手
                handshaker.handshake(ctx.channel(), req).sync();
                ChannelGroup channelGroup= chatRoomsMap.get("live:"+userId);
                if(channelGroup == null){
                    //当前为房主
                    channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
                    channelGroup.add(ctx.channel());
                    chatRoomsMap.put(chatroomName,channelGroup);
                }else{
                    TextWebSocketFrame textWebSocketFrame=new TextWebSocketFrame(username+" 加入了直播间");
                    for(Channel channel:channelGroup){
                        channel.writeAndFlush(textWebSocketFrame);
                    }
                    channelGroup.add(ctx.channel());
                }
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道建立");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道关闭");
        String chatroom=(String) ctx.channel().attr(AttributeKey.valueOf(ChatroomConstant.CHATROOM)).get();
        if(chatroom!=null){
            ChannelGroup channelGroup = chatRoomsMap.get(chatroom);
            channelGroup.remove(ctx.channel());
        }
        super.channelInactive(ctx);
    }
}