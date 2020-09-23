<template>
  <div>
    <div :class="['box', theme]" ref="box" v-on:contextmenu.prevent="handleShow($event)" style="min-height: 785px">
      <el-tree
          class="filter-tree"
          :data="data"
          :props="defaultProps"
          :default-expanded-keys="defaultExpandedKeys"
          ref="tree"
          :highlight-current="true"
          @node-click="handleNodeClick"
          @node-contextmenu="handleContextMenu"
      >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>
                <i :class="data.icon"> </i> {{ node.label }}
            </span>
          </span>
      </el-tree>
    </div>
    <v-contextmenu ref="contextmenu">
      <!-- 是否是点击空白区域 -->
      <v-contextmenu-item v-if="!isNodeClick" @click="handleAddFolder">新建文件夹</v-contextmenu-item>
      <v-contextmenu-item v-if="!isNodeClick" @click="handleAddConnect">新建连接</v-contextmenu-item>
      <!--编辑和删除文件夹-->
      <v-contextmenu-item v-if="isItemClick && currentClickNodeData.type === 'FOLDER'" @click="handleEditFolder">编辑</v-contextmenu-item>
      <v-contextmenu-item v-if="isItemClick && currentClickNodeData.type === 'FOLDER'" @click="handleDeleteFolder">删除</v-contextmenu-item>

      <!--编辑和删除Node-->
      <v-contextmenu-item v-if="isItemClick && currentClickNodeData.type === 'NODE'" @click="handleEditNode">编辑</v-contextmenu-item>
      <v-contextmenu-item v-if="isItemClick && currentClickNodeData.type === 'NODE'" @click="handleDeleteNode">删除</v-contextmenu-item>
    </v-contextmenu>

    <FolderForm :dialogFormVisible="folderFormVisible" :cancel="handleFolderFormCancel" :form="folderForm" :treeRefresh="refresh" :currentClickNodeData="currentClickNodeData" :folderTitle="folderTitle"/>
    <ConnectForm :dialogFormVisible="connectFormVisible" :cancel="handleConnectFormCancel" :form="connectForm" :treeRefresh="refresh" :currentClickNodeData="currentClickNodeData" :connectTitle="connectTitle"/>
  </div>

</template>

<script>
import FolderForm from '../components/FolderForm';
import ConnectForm from '../components/ConnectForm';
export default {
  name: "Tree",
  props: {
    addTab: Function,
    theme: String,
  },
  components: {
    FolderForm : FolderForm,
    ConnectForm : ConnectForm,
  },
  data() {
    return {
      defaultExpandedKeys: [1],
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name',
        isLeaf: 'leaf',
        icon: 'icon',
      },
      loading: true,
      folderFormVisible: false,
      connectFormVisible: false,
      folderForm: {
        id: 0,
        name: '',
        description: '',
      },
      connectForm: {
        id: 0,
        name: '',
        description: '',
        host: '',
        port : '',
        user : '',
        password : '',
        identity : '',
        passphrase : '',
        authMethod: 'password',
      },
      isItemClick: false,
      isNodeClick: false,
      currentClickNodeData: null,
      folderTitle: '新增文件夹',
      connectTitle: '新增连接',
    };
  },
  methods: {
    // eslint-disable-next-line no-unused-vars
    handleNodeClick(node) {
      if(node.type === 'NODE') {
        console.log(node);
        this.addTab(node);
      }
    },
    // eslint-disable-next-line no-unused-vars
    handleContextMenu(event, data, node) {
      const postition = {
        top: event.clientY,
        left: event.clientX,
      }
      this.isItemClick = true;
      if(data.type === 'FOLDER') {
        this.isNodeClick = false;
      } else {
        this.isNodeClick = true;
      }
      this.currentClickNodeData = data;
      this.$refs.contextmenu.show(postition);
    },
    handleShow(event) {
      // var DOM = event.currentTarget;
      // 获取节点距离浏览器视口的高度
      var top = event.clientY;
      // 获取节点距离浏览器视口的宽度
      var left = event.clientX;
      const postition = {
        top: top,
        left: left,
      }
      this.isItemClick = false;
      this.$refs.contextmenu.show(postition);
      this.isNodeClick = false;
      this.currentClickNodeData = null;
    },
    handleHide() {
      this.$refs.contextmenu.hide();
    },
    refresh() {
      this.search();
    },
    search() {
      this.loading = true;
      const that = this;
      that.$api.get('/ecs/tree', { }, (res) => {
        this.data = res.data;
      });
    },
    handleAddFolder() {
      // 打开 form
      this.folderFormVisible = true;
      this.folderTitle = '新增文件夹';
      // form 重置
      this.folderForm = {id: 0, name: '', description: ''};
    },
    handleEditFolder() {
      // 打开 form
      this.folderFormVisible = true;
      this.folderTitle = '编辑文件夹';
      // form 重置
      this.folderForm = {id: this.currentClickNodeData.id, name: this.currentClickNodeData.name, description: this.currentClickNodeData.description };
    },
    handleDeleteFolder() {
      console.log('删除文件夹');
      // 删除
      const that = this;
      that.$api.delete('/ecs', {id : this.currentClickNodeData.id}, (res) => {
        if(res !== undefined && res.status !== undefined && res.status === 200) {
          this.openLayer('消息', '恭喜你，删除成功。', 'success');
          // 关闭弹出层
          this.refresh();
        } else {
          this.openLayer('消息', res.data, 'error');
        }
      });
    },
    handleAddConnect() {
      this.connectTitle = '新增连接';
      // form 重置
      this.connectForm = {id: 0, name: '', description: '', host : '', port : '', user : '', password : '', identity : '', passphrase : '',authMethod: 'password' };
      this.connectFormVisible = true;
    },
    handleEditNode() {
      this.connectTitle = '编辑连接';
      this.connectForm = {id : this.currentClickNodeData.id, name: this.currentClickNodeData.name, description: this.currentClickNodeData.description, host : this.currentClickNodeData.config.host, port : this.currentClickNodeData.config.port, user : this.currentClickNodeData.config.user, password : this.currentClickNodeData.config.password, identity : this.currentClickNodeData.config.identity, passphrase : this.currentClickNodeData.config.passphrase, authMethod: this.currentClickNodeData.config.authMethod };
      this.connectFormVisible = true;
    },
    handleDeleteNode() {
      console.log('删除链接');
      // 删除
      const that = this;
      that.$api.delete('/ecs', {id : this.currentClickNodeData.id}, (res) => {
        if(res !== undefined && res.status !== undefined && res.status === 200) {
          this.openLayer('消息', '恭喜你，删除成功。', 'success');
          // 关闭弹出层
          this.refresh();
        } else {
          this.openLayer('消息', res.data, 'error');
        }
      });
    },
    handleFolderFormCancel() {
      this.folderFormVisible = false;
      this.folderForm = {id: 0, name: '', description: ''};
    },
    handleConnectFormCancel() {
      this.connectFormVisible = false;
      this.connectForm = {id: 0, name: '', description: '', host : '', port : '', user : '', password : '', identity : '', passphrase : '',authMethod: 'password' };
    },
    openLayer(title, msg, type) {
      if(type === 'error') {
        this.$notify.error({
          title: title,
          message: msg
        });
      } else {
        this.$notify({
          title: title,
          message: msg,
          type: type
        });
      }
    },
  },
  mounted() {
    const that = this;
    that.$api.get('/ecs/tree', { }, (res) => {
      this.data = res.data;
    });
  }
}
</script>

<style scoped>

</style>