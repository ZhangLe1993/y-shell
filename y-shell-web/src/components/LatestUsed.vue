<template>
  <el-main style="overscroll-behavior-y: auto">

  </el-main>
</template>

<script>
    export default {
      name: "LatestUsed",
      data() {
        return {
          activeIndex: '1',
          currentDate: ''
        };
      },
      methods: {
        handleSelect(key, keyPath) {
          console.log(key, keyPath);
        }
      }
    }
</script>

<style scoped>

</style>