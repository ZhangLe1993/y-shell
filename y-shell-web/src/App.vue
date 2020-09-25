<template>
  <div id="app">
    <Header></Header>
    <router-view/>
  </div>
</template>

<script>
import Header from './components/Header.vue';
export default {
  name: 'App',
  components: {
    Header: Header
  },
  created() {
    setTimeout(() => {
      window.L2Dwidget.init({
        pluginRootPath: 'static/live2dw/',
        pluginJsPath: 'lib/',
        pluginModelPath: 'live2d-widget-model-haru_1/assets/',
        tagMode: false,
        debug: false,
        model: { jsonPath: 'static/live2dw/live2d-widget-model-haru_1/assets/haru01.model.json' },
        display: { position: 'right', width: 300, height: 600 },
        mobile: { show: true },
        log: false
      })
    }, 1000)
  },
}
</script>
<style>
body {
  -webkit-user-select:none;

  -moz-user-select:none;

  -ms-user-select:none;

  user-select:none;
}
</style>
<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>

