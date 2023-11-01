<template>
  <div id="app">
    <PageHeader v-if="Ifshow"></PageHeader>
    <router-view></router-view>
    <PageFooter v-if="Ifshow"></PageFooter>


  </div>
</template>

<script>
import PageHeader from "@/components/header/PageHeader.vue";
import PageFooter from "@/components/footer/PageFooter.vue";



export default {
  name: 'App',
  components: {
    PageHeader,
    PageFooter,
    // eslint-disable-next-line vue/no-unused-components
  },
  data() {
    return {
      Ifshow: true
    }
  },
}


</script>

<style>

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  /*background-color: #F4F5F7;*/
  width: 100%;
  height: 100%;
  background-color: rgba(22, 24, 35, 0.9);
}
</style>
