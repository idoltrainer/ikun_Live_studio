<template>
  <div class="main">
    <div class="video_inner" ref="videoInnerRef">
      <video-top-vue :activeTab="activeTab" @changeTab="changeActiveTab($event)" />
      <video-item-vue v-for="(video, index) in videos" :dataVideo="video" :key="index" :dataIndex="index" @nextVideo="nextVideoNow($event)" @onPlay="togglePlay($event)" />
    </div>
  </div>
</template>
<script>
import { onMounted, reactive, ref } from 'vue';
import VideoTopVue from '@/components/shotVideo/VideoTop.vue';
import VideoItemVue from '@/components/shotVideo/VideoItem.vue';
export default {
  // name: "ShotVideo",
  components: {
    VideoItemVue,
    VideoTopVue
  },
  created() {
    this.$parent.Ifshow= false
  },
  setup() {
    const activeTab = ref('follow');
    const videoInnerRef = ref(null);
    const onPlay = ref(false);
    onMounted(() => {
      var timer = null;
      videoInnerRef.value.addEventListener('scroll', function (e) {
        if (timer !== null) {
          clearTimeout(timer);
        }
        timer = setTimeout(function () {
          window.dispatchEvent(new CustomEvent('scroll.video.inner', {
            detail: {
              data: e.target.scrollTop,
              clicked: onPlay.value
            }
          }))
        }, 150);
      });
    })

    function nextVideoNow(index) {
      videoInnerRef.value.scrollTo({
        top: window.innerHeight * (index + 1),
        behavior: 'smooth'
      });
    }
    function firstClick() {
      onPlay.value = true;
    }

    function togglePlay(play) {
      onPlay.value = play.value;
    }

    const videos = reactive(

        [
      {

        name: "Độ Phùng",
        des: "Video số 1",
        like: 4567645,
        comment: 34535,
        share: 65464,
        musicName: "Việt Nam trong tôi là - Tí Nâu",
        avatar: "avatar.jpeg",
        src: "http://s3159m8av.hn-bkt.clouddn.com/user_file_store/user_video/9/JyOLjNmH-test2.mp4"
      }, {
        name: "Gái xinh",
        des: "Video số 3",
        like: 5435436,
        comment: 253400,
        share: 302342,
        musicName: "Anh Chưa Thương Em Đến Vậy Đâu - Lady Mây",
        avatar: "girl.jpg",
        src: "http://s3159m8av.hn-bkt.clouddn.com/user_file_store/user_video/9/JyOLjNmH-test2.mp4"
      },
      {
        name: "Jisoo",
        des: "Video số 3",
        like: 123424,
        comment: 2234432,
        share: 64234234,
        musicName: "How You Like That - BLΛƆKPIИK",
        avatar: "jisoo.png",
        src: "https://v16-webapp.tiktok.com/02a1f3fe7fe54b94d461001d3f55c323/6362ebe3/video/tos/useast2a/tos-useast2a-pve-0037c001-aiso/654854fbf1624353b8e4b6b7d6ce6325/?a=1988&ch=0&cr=0&dr=0&lr=tiktok_m&cd=0%7C0%7C1%7C0&cv=1&br=1864&bt=932&cs=0&ds=3&ft=kLO5qy-gZmo0P30UQBkVQ0zxDiHKJdmC0&mime_type=video_mp4&qs=0&rc=NGRoPDs4ZjdoZmRnNzU3OEBpam88azg6Zmk5ZzMzZjczM0BhYWEzXzEvX2AxNDM0XjUwYSNjbXEycjQwbC1gLS1kMWNzcw%3D%3D&l=202211021614440102452410101237933D&btag=80000"
      }
    ]);

    function changeActiveTab(type) {
      activeTab.value = type;
    }
    return { videoInnerRef, videos, activeTab, changeActiveTab, firstClick, nextVideoNow, togglePlay };
  }
};
</script>
<style scoped>
.video_inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 101vh;
  min-height: 100vh;
  overflow-y: auto;
  scroll-snap-type: y mandatory;
  /*object-fit: fill;*/
  overflow: hidden;
}

.main{
  margin:0px;
  padding: 0px;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background: #000;
  /*display: flex;*/
  justify-content: center;
  align-items: center;
  /*overflow: hidden;*/
  width: 100%;
  height: 100%;
}
</style>