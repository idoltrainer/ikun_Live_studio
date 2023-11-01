<template>
  <div class="video_inner" ref="videoInnerRef">
    <video-top-vue :activeTab="activeTab" @changeTab="changeActiveTab($event)" />
    <video-item-vue v-for="(video, index) in videos" :dataVideo="video" :key="index" :dataIndex="index" @nextVideo="nextVideoNow($event)" @onPlay="togglePlay($event)" />
  </div>
</template>
<script>
import { onMounted, reactive, ref } from 'vue';
import VideoItemVue from './components/VideoItem.vue';
import VideoTopVue from './components/VideoTop.vue';
export default {
  name: "App",
  components: {
    VideoItemVue,
    VideoTopVue
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

    const videos = reactive([
      {
        name: "Độ Phùng",
        des: "Video số 1",
        like: 4567645,
        comment: 34535,
        share: 65464,
        musicName: "Việt Nam trong tôi là - Tí Nâu",
        avatar: "avatar.jpeg",
        src: "https://v16-webapp.tiktok.com/46c558bd6737ab5ed878954cf24f6e53/6362eabb/video/tos/useast2a/tos-useast2a-pve-0037c001-aiso/d59715eda0fb4e1e96959904d4569bd3/?a=1988&ch=0&cr=0&dr=0&lr=tiktok_m&cd=0%7C0%7C1%7C0&cv=1&br=1458&bt=729&cs=0&ds=3&ft=kLO5qy-gZmo0P7aUQBkVQRBBDiHKJdmC0&mime_type=video_mp4&qs=0&rc=Zmg8NDg0O2c0ZzU5NTQ5ZkBpajloOTo6Zjs0ZjMzZjczM0AvY2BhLV8wNl4xLWEvXzNjYSMuNmA1cjRfYGxgLS1kMWNzcw%3D%3D&l=202211021609490102452461050F339375&btag=80000"
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
  height: 100vh;
  overflow-y: auto;
  scroll-snap-type: y mandatory;
  /*object-fit: fill;*/
}
</style>