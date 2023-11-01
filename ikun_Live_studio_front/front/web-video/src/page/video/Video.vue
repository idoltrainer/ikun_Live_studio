<template>
  <div id="video">
    <div style="width: 85%; max-width: 900px; margin: 0 30px;">
      <br>
      <h3 style="word-break: break-all; color: #ffffff">{{ videoInfo.name }}</h3>
      <br>
<!--      <video-player class="video-player-box"-->
<!--                    ref="videoPlayer"-->
<!--                    :options="playerOptions"-->
<!--                    :playsinline="true"-->
<!--                    @play="onPlayerPlay($event)"-->
<!--                    @pause="onPlayerPause($event)"-->
<!--                    style="width: 100%;"-->
<!--      >-->
<!--      </video-player>-->
      <div id="nPlayer" ref="nPlayer" />
      <br>
      <div>
        <svg class="icon" aria-hidden="true" @click="videoLike" style="margin-right: 50px;">
          <use v-if="videoInfo.isLiked" xlink:href="#icon-like2"/>
          <use v-else xlink:href="#icon-like1"/>
        </svg>
        <svg class="icon" aria-hidden="true" @click="videoMark" style="margin-right: 50px;">
          <use v-if="videoInfo.isMarked" xlink:href="#icon-mark2"/>
          <use v-else xlink:href="#icon-mark1"/>
        </svg>
        <span v-clipboard:copy="page.url">
                    <svg class="icon" aria-hidden="true" @click="videoShare">
                        <use xlink:href="#icon-share"/>
                    </svg>
                </span>
      </div>
      <br>
      <p style="font-size: 14px;">{{ videoInfo.introduction }}</p>
      <br>
    </div>
    <div style="width: 85%; max-width: 500px;">
      <br>
      <div style="font-size: 20px;">评论</div>
      <el-input  v-model="comment.content" type="textarea" resize="none" :autosize="{ minRows: 1, maxRows: 3 }" style="width: 80%;resize: none;font-size: 15px;"/>
      <el-button type="primary" size="medium" round @click="videoComment">发表</el-button>
      <el-divider />
      <div class="infinite-list" v-infinite-scroll="videoCommentGetMore" style="overflow: auto; height: 400px; padding: 0 2px;">
        <div v-for="(v, k) in videoInfo.comments" :key="'comment' + k">
          <el-row>
            <el-col :span="12" style="font-size: 18px;">
              <div class="nav-user">
                <img class="user-head" src="@/assets/image/default_head.jpg" />
                {{ v.userName }}
              </div>
            </el-col>
            <el-col :span="12" style="text-align: right; font-size: 14px; font-style: italic">{{ v.time }}</el-col>
          </el-row>
          <br>
          <el-row style="font-size: 15px;">
            {{ v.content }}
          </el-row>
          <el-divider />
        </div>
        <div>
          {{ isVideoCommentEnd ? '到底啦～' : '下拉加载更多'}}
        </div>
        <br>
      </div>
      <br>
    </div>
  </div>
</template>

<script>
import 'video.js/dist/video-js.css'
import { videoPlayer } from 'vue-video-player'
import './icon/iconfont'
import Player from 'nplayer'
import Danmaku from '@nplayer/danmaku'


export default {
  components: {
    videoPlayer
  },
  data() {
    return {
      isMeDanmu:'',
      page: {
        url: window.location.href,
      },
      isVideoCommentEnd: false,
      playerOptions: {
        muted: true,
        language: 'zh-CN',
        playbackRates: [0.25, 0.5, 1.0, 1.25, 2.0],
        fluid: true,
        sources: [{
          type: "video/mp4",
          src: "https://www.w3school.com.cn/i/movie.mp4"
        }],
      },
      videoInfo: {
        id: null,
        name: 'test video',
        introduction: '简介简介简介',
        isLiked: false,
        isMarked: false,
        comments: [
          {
            userName: 'a',
            userHead: '',
            time: '2020-01-01',
            content: 'fd'
          },
          {
            userName: 'b',
            userHead: '',
            time: '2020-02-01',
            content: 'ads'
          },
        ]
      },
      comment: {
        userName: 'a',
        userHead: '',
        time: '2020-01-01',
        content: ''
      }
    }

  },
  created() {
    this.videoInfo.id = this.$route.path.substring(this.$route.path.lastIndexOf('/') + 1);
    // this.initPlayer();
  },
  mounted() {
    console.log('player: mounted', this.player);
    this.nPlayer()
  },
  // computed: {
  //   player() {
  //     return this.$refs.videoPlayer.player
  //   }
  // },
  methods: {
    nPlayer() {
      const danmakuOptions = {
        items: [
          { time: 5, text: '弹幕1～', color: '#FE0302' },
          { time: 10, text: '是我是我', color: '#75ffcd', isMe: true },
          { time: 17, text: '弹幕2～', color: '#A0EE00' },
          { time: 18, text: '弹幕3～', color: '#019899' },
          { time: 20, text: '弹幕4～', color: '#CC0273' }
        ]
      }
      const player = new Player({
        src: 'https://mpv.videocc.net/e785b2c81c/5/e785b2c81c9e018296671a1287e99615_2.mp4', // 视频地址
        contextMenus: ['loop', 'pip'], // 右键菜单设置项
        themeColor: 'rgb(255,44,85)',
        progressBg: 'rgb(255,44,85)',
        volumeProgressBg: 'rgb(255,44,85)',
        plugins: [new Danmaku(
            {
              danmakuOptions,
            }
            )] // 弹幕配置项
        // controls: [['play', 'progress', 'time', 'web-fullscreen', 'fullscreen'], [], ['spacer', 'settings']]
      })
      player.mount('#nPlayer')
      // 链接服务器
      var ws = new WebSocket('ws://10.10.8.223:9283/lbh')
      ws.onopen = function () {
        console.log('数据发送中...')
      }
      ws.onmessage = function (e) {
        this.isMeDanmu = e.data
        console.log('接受到消息:' + e.data)
      }
      ws.onclose = function () {
        console.log('连接已关闭...')
      }
      function sendMsg(msg) {
        ws.send(msg)
      }
      player.on('DanmakuSend', opts => {
        sendMsg(opts)
        console.log('用户当前发送的弹幕信息', opts)
      })
    },
    videoLike() {
      this.videoInfo.isLiked = !this.videoInfo.isLiked
    },
    videoMark() {
      this.videoInfo.isMarked=!this.videoInfo.isMarked
    },
    videoShare() {
      this.$message.success({ message: '已复制链接', duration:1500 });
    },
    videoComment() {
      if (this.comment.content.length === 0) {
        this.$message.warning({ message: '评论不能为空', duration:1500 });
        return;
      }
      this.comment.content = '';
    },
    videoCommentGetMore() {
      if (this.videoInfo.comments.length > 10) {
        this.isVideoCommentEnd = true;
        return;
      }
      this.videoInfo.comments.push({
        userName: 'c',
        time: '2020-02-05',
        content: 'adsa'
      })
    },
    // onPlayerPlay(player) {
    //   console.log('player: play', player)
    // },
    // onPlayerPause(player) {
    //   console.log('player: pause', player)
    // },

  }
}
</script>

<style scoped>
#video{
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-flow:row wrap;
  padding-bottom: 1000px;
}
.icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
  cursor: pointer;
  font-size: 30px;
  color: #1b61a9;
}
.nav-user {
  display: flex;
  align-items: center;
}
.user-head {
  height: 32px;
  width: 32px;
  border-radius: 45%;
  cursor: pointer;
  border: 1px solid #dedede;
  margin-right: 10px;
}
</style>