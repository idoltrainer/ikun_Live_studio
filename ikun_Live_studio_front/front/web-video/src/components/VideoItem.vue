<template>
  <div ref="videoRef" class="video">
    <video ref="videoControl" :src="dataVideo.src" @click="togglePlayVideo($event)"></video>
    <div class="video__bottom">
      <video-info-vue :dataVideo="dataVideo" />
      <video-control-vue :dataVideo="dataVideo" />
      <video-action-vue :dataVideo="dataVideo" />
    </div>
    <video-play-icon-vue :dataVideo="dataVideo" :isPlay="isPlay" @togglePlayPause="togglePlayVideo" />
  </div>
</template>

<script>
import { onBeforeMount, onMounted, ref } from "@vue/runtime-core"
import VideoInfoVue from "./VideoInfo.vue";
import VideoControlVue from "./VideoControl.vue";
import VideoActionVue from "./VideoAction.vue";
import VideoPlayIconVue from "./VideoPlayIcon.vue";
export default {
  components: {
    VideoInfoVue,
    VideoControlVue,
    VideoActionVue,
    VideoPlayIconVue
  },
  props: {
    dataVideo: {
      type: [Object]
    },
    dataIndex: {
      type: Number
    }
  },
  setup(props, { emit }) {
    const videoRef = ref(null);
    const videoControl = ref(null);
    const isPlay = ref(false);
    function resizeVideo() {
      videoRef.value.style.height = window.innerHeight + 'px';
      videoRef.value.style.width = window.innerHeight / 16 * 9 + 'px';
    }

    function endVideo() {
      videoControl.value.addEventListener('ended', function () {
        emit('nextVideo', props.dataIndex);
      });
    }
    onMounted(() => {
      resizeVideo();
      endVideo();
      window.addEventListener('keydown', e => {
        const indexCurrent = e.detail.data / window.innerHeight;
        isPlay.value = indexCurrent === props.dataIndex && e.detail.clicked;
        isPlay.value ? videoControl.value?.play() : videoControl.value?.pause();
      })
      window.addEventListener('resize', resizeVideo);
    })

    onBeforeMount(() => {
      window.removeEventListener('resize', resizeVideo)
    });

    function togglePlayVideo() {
      isPlay.value = !isPlay.value;
      emit('onPlay', isPlay);
      isPlay.value ? videoControl.value.play() : videoControl.value.pause();
    }

    return { videoRef, videoControl, isPlay, togglePlayVideo }
  }
}
</script>
<style scoped>
.video video {
  width: 100%;
  height: 100vh;

}

.video {
  position: relative;
  scroll-snap-align: start;

}

.video__bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
}
</style>
