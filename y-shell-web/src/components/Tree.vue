<template>
  <div>
    <div :class="['box', theme]" ref="box" v-on:contextmenu.prevent="handleShow($event)" style="min-height: 700px">
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
        config: {},
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
    },
    handleAddConnect() {
      this.connectFormVisible = true;
      this.connectTitle = '新增连接';
      // form 重置
      this.connectForm = {id: 0, name: '', description: '', authMethod: 'password', config : { host : '', port : '', user : '', password : '', identity : '', passphrase : '' }};
    },
    handleEditNode() {
      this.connectFormVisible = true;
      this.connectTitle = '编辑连接';
      this.folderForm = {id : this.currentClickNodeData.id, name: this.currentClickNodeData.name, description: this.currentClickNodeData.description, config : { host : '', port : '', user : '', password : '', authMethod : 'password', identity : '', passphrase : '' }};
    },
    handleDeleteNode() {
      console.log('删除链接');
    },
    handleFolderFormCancel() {
      this.folderFormVisible = false;
    },
    handleConnectFormCancel() {
      this.connectFormVisible = false;
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