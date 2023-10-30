package com.idoltrainer.webvideo.netty;

import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncTask {

    /**
     * key为直播id，value为channelgroup（包含多个channel，一个用户一个channel）
     */
    public static Map<String, ChannelGroup> chatRoomsMap = new ConcurrentHashMap<>();

}
