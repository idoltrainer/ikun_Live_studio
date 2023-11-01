<template>
  <div class="video-play-icon" @click="playVideoNow" v-if="!isShowIcon"></div>
</template>

<script>
import { computed } from '@vue/reactivity';

export default {
  props: {
    dataVideo: {
      type: [Object]
    },
    isPlay: {
      type: [Boolean]
    }
  },
  setup(props, { emit }) {
    const isShowIcon = computed(() => props.isPlay);

    function playVideoNow() {
      emit('togglePlayPause');
    }

    return { playVideoNow, isShowIcon }

  }
}
</script>

<style>
.video-play-icon {
  background-image: url('../assets/video-play-icon.png');
  width: 4rem;
  height: 5rem;
  position: absolute;
  z-index: 1;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-size: 100% 100%;
  filter: drop-shadow(2.2px 4.3px 4.3px #0000006e);
  cursor: pointer;
}
</style>