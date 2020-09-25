<template>
  <div :id="divId"></div>
</template>

<script>
import "xterm/css/xterm.css";
import "xterm/lib/xterm.js";
import { Terminal } from "xterm";
import { FitAddon } from "xterm-addon-fit";

export default {
  name:'BaseTerm',
  props: {
    divId: String,
    node: Object,
  },
  data() {
    return {
      term: "",
      socket:""
    };
  },
  methods: {
    initXterm() {
      this.term = new Terminal({
        rendererType: "canvas", //渲染类型
        cols: 170,
        rows: 47, //行数
        convertEol: true, //启用时，光标将设置为下一行的开头
        scrollback: 10, //终端中的回滚量
        disableStdin: false, //是否应禁用输入
        cursorStyle: "underline", //光标样式
        cursorBlink: true, //光标闪烁
        theme: {
          foreground: "yellow", //字体
          background: "#060101", //背景色
          cursor: "help" //设置光标
        }
      });
      this.term.open(document.getElementById(this.divId));
      const fitAddon = new FitAddon();
      this.term.loadAddon(fitAddon);
      // 支持输入与粘贴方法
      let _this = this; //一定要重新定义一个this，不然this指向会出问题
      this.term.onData(function(key) {
        // let order = ["stdin",key]; //这里key值是你输入的值，数据格式一定要找后端要！！！！
        _this.socket.onsend(JSON.stringify({type:"command", data: key})); //转换为字符串
      });
    },
    init(url) {
      // 实例化socket
      this.socket = new WebSocket(url);
      // 监听socket连接
      this.socket.onopen = this.open;
      // 监听socket错误信息
      this.socket.onerror = this.error;
      // 监听socket消息
      this.socket.onmessage = this.getMessage;
      // 发送socket消息
      this.socket.onsend = this.send;
    },
    open: function() {
      console.log("socket连接成功");
      this.initXterm();
      this.socket.onsend(JSON.stringify({type:"connect",data: this.node.id })); //转换为字符串
    },
    error: function() {
      console.log("连接错误");
    },
    close: function() {
      this.socket.close();
      console.log("socket已经关闭");
    },
    getMessage: function(msg) {
      console.log(msg);
      this.term.write(msg.data);
    },
    send: function(order) {
      this.socket.send(order);
    }
  },
  mounted() {
    let url ="ws://" + location.host + "/terminal";
    this.init(url);
  },
};
</script>

<style scoped>

</style>