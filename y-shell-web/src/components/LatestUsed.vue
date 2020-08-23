<template>
  <el-main style="overscroll-behavior-y: auto">
    <el-divider content-position="left">最近使用</el-divider>
    <el-row style="width:100%;margin: auto;">
      <el-col :span="4" v-for="(o, index) in 5" :key="o" :offset="index > 0 ? 1 : 0">
        <el-card :body-style="{ padding: '0px' }">
          <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image" style="height:200px;">
          <div style="padding: 14px;">
            <span>唐僧</span>
            <div class="bottom clearfix">
              <time class="time" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">{{ currentDate }}</time>
              <!--<el-button type="text" class="button" style="float: right">查看</el-button>
              <el-button type="text" class="button" style="float: left">分享</el-button>-->
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <br>
    <br>
    <el-divider content-position="left">我的主机</el-divider>
    <el-row style="width:100%;margin: auto;">
      <el-col :span="4" v-for="(o, index) in 5" :key="o" :offset="index > 0 ? 1 : 0">
        <el-card :body-style="{ padding: '0px' }">
          <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image" style="height:200px;">
          <div style="padding: 14px;">
            <span>唐僧</span>
            <div class="bottom clearfix">
              <time class="time" style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">{{ currentDate }}</time>
              <el-button type="text" class="button" style="float: right">删除</el-button>
              <el-button type="text" class="button" style="float: left">编辑</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
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