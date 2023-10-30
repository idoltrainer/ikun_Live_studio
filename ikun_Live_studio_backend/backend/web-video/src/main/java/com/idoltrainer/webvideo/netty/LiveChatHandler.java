package com.idoltrainer.webvideo.netty;

import com.idoltrainer.webvideo.constant.ChatroomConstant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.idoltrainer.webvideo.netty.AsyncTask.chatRoomsMap;



@Component
public class LiveChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text=msg.text();
        String username=(String) ctx.channel().attr(AttributeKey.valueOf(ChatroomConstant.USERNAME)).get();
        String chatroom=(String) ctx.channel().attr(AttributeKey.valueOf(ChatroomConstant.CHATROOM)).get();
        String id=ctx.channel().id().asLongText();
        if(chatroom!=null){
            ChannelGroup channelGroup = chatRoomsMap.get(chatroom);
            if(channelGroup!=null){
                String time=new Date().toLocaleString();
                TextWebSocketFrame other=new TextWebSocketFrame(time+" "+username+": "+text);
                TextWebSocketFrame me=new TextWebSocketFrame(time+" "+"我: "+text);
                for(Channel channel:channelGroup){
                    String channelId=channel.id().asLongText();
                    if(id.equals(channelId)){
                        channel.writeAndFlush(me);
                    }else {
                        channel.writeAndFlush(other);
                    }
                }
            }
        }
    }
}

