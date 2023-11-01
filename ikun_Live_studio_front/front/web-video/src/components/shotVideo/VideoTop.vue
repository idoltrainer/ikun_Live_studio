<template>
  <ul class="tabs">
    <li :class="{ active: activeTab == 'follow' }" @click="handleChangeActiveTab($event, 'follow')">
      Đang Follow
    </li>
    <li :class="{ active: activeTab == 'forMe' }" @click="handleChangeActiveTab($event, 'forMe')">
      Dành cho bạn
    </li>
  </ul>
</template>

<script>
export default {
  props: {
    activeTab: {
      type: String
    }
  },
  setup(props, { emit }) {
    function handleChangeActiveTab(e, tabType) {
      emit('changeTab', tabType);
    }
    return { handleChangeActiveTab }
  },
}
</script>
<style>
.tabs {
  position: absolute;
  left: 50%;
  top: 1rem;
  display: flex;
  gap: 1rem;
  transform: translateX(-50%);
  z-index: 1;
}

.tabs li {
  list-style: none;
  color: white;
  opacity: 0.3;
  transition: all 200ms;
  font-weight: bold;
  position: relative;
}

.tabs li.active {
  opacity: 1;
}

.tabs li.active::before {
  content: "";
  width: 1.5rem;
  height: 3px;
  border-radius: 25px;
  position: absolute;
  background-color: white;
  transform: translateX(-50%);
  left: 50%;
  bottom: -10px;
}

.tabs li:hover {
  cursor: pointer;
  opacity: 1;
}
</style>
