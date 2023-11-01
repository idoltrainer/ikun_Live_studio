<template>
  <div class="controls">
    <div class="music">
      <div class="music__icon"></div>
      <div class="music__title"><span ref="musicTitleRef">{{ dataVideo.musicName }}</span></div>
    </div>
    <div class="image-music" :style="{ backgroundImage: `url(${require('../assets/' + dataVideo.avatar)})` }">
    </div>
  </div>
</template>
mar
<script>
import { onMounted, ref } from 'vue';

export default {
  props: {
    dataVideo: {
      type: [Object]
    }
  },
  setup() {
    const musicTitleRef = ref(null);
    function animateMarquee(element) {
      let elementWidth = element.offsetWidth;
      let parentWidth = element.parentElement.offsetWidth * 2;
      let flag = 0;

      setInterval(() => {
        element.style.marginLeft = --flag + "px";

        if (elementWidth == -flag) {
          flag = parentWidth;
        }
      }, 10);
    }

    onMounted(() => {
      animateMarquee(musicTitleRef.value)
    })
    return { musicTitleRef }
  }
}
</script>
<style>
.controls {
  position: absolute;
  bottom: 1rem;
  left: 1rem;
  display: flex;
  justify-content: space-between;
  width: calc(100% - 2rem);
}

.music {
  display: flex;
  align-items: self-end;
  gap: 0.5rem;
}

.music__title {
  color: white;
  font-size: 0.875rem;
  overflow: hidden;
  max-width: 8rem;
  white-space: nowrap;
}

.music__icon {
  background-image: url("../assets/icon-music.png");
  -webkit-background-size: 15px 15px;
  background-size: 15px 15px;
  background-repeat: no-repeat;
  width: 20px;
  height: 15px;
}

.image-music {
  background-size: 100% 100%;
  background-repeat: no-repeat;
  border-radius: 50%;
  width: 3rem;
  height: 3rem;
  animation: rotate360 4s linear 0.5s reverse infinite;
}

@keyframes rotate360 {
  from {
    transform: rotate(360deg);
  }
}
</style>
