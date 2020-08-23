<template>
  <el-dialog :title="connectTitle" :visible.sync="dialogFormVisible" :show-close="false" :close-on-click-modal="false" width="30%">

    <el-form :model="form" :rules="rules" ref="form">
      <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" auto-complete="off"></el-input>
      </el-form-item>

      <el-row>
        <el-col :span="16">
            <el-form-item label="主机地址" :label-width="formLabelWidth">
              <el-input v-model="form.config.host" auto-complete="off"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="端口号" :label-width="formLabelWidth">
              <el-input v-model="form.config.port" auto-complete="off"></el-input>
            </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="用户名" :label-width="formLabelWidth">
        <el-input v-model="form.config.user" auto-complete="off"></el-input>
      </el-form-item>

      <el-form-item label="验证方式" :label-width="formLabelWidth">
        <el-select v-model="form.authMethod" placeholder="请选择验证方式">
          <el-option label="密码" value="password"></el-option>
          <el-option label="密钥" value="rsa"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item v-if=" form.authMethod ==='password' " label="密码" :label-width="formLabelWidth" prop="password">
        <el-input v-model="form.config.password" auto-complete="off"></el-input>
      </el-form-item>

      <el-row v-if=" form.authMethod ==='rsa' ">
        <el-col :span="18">
          <el-form-item label="选择文件" :label-width="formLabelWidth">
            <el-select v-model="form.config.identity" filterable placeholder="选择已上传的密钥文件">
              <el-option v-for="item in rsaOptions" :key="item" :label="item" :value="item"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-upload
              action="https://jsonplaceholder.typicode.com/posts/"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :before-remove="beforeRemove"
              :before-upload="beforeUpload"
              multiple
              show-file-list="false"
              :limit="1"
              :on-exceed="handleExceed"
          >
            <el-button size="medium" type="primary">点击上传</el-button>
          </el-upload>
        </el-col>
      </el-row>

      <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
        <el-input v-model="form.description" auto-complete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="onCancel">取 消</el-button>
      <el-button type="primary" @click="onSubmit('form')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "ConnectForm",
  props: {
    dialogFormVisible: {
      type: Boolean,
      default: false
    },
    cancel: Function,
    form: Object,
    treeRefresh:Function,
    currentClickNodeData: Object,
    connectTitle : String,
  },
  data() {
    return {
      options: [{value: 'password', label: '密码'},{value: 'rsa', label: '密钥'}],
      formLabelWidth: '80px',
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur' }
        ],
        description: [
          { min: 0, max: 500, message: '长度在 0 到 500 个字符', trigger: 'blur' }
        ],
      },
      rsaOptions: ['~/.ssh/id_rsa', '/data/y_shell/id_rsa'],
    }
  },
  methods: {
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
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const that = this;
          const formData = that.form;
          formData.type = "NODE";
          if(formData.id === 0) {
            formData.parentId = -1;
            // 新增的时候绑定父节点
            if(this.currentClickNodeData != null) {
              formData.parentId = this.currentClickNodeData.id;
            }
            formData.config = JSON.stringify(formData.config);
            console.log(formData);
            // 新增
            /*that.$api.post('/ecs', JSON.stringify(formData), (res) => {
              if(res !== undefined && res.status !== undefined && res.status === 200) {
                this.openLayer('消息', '恭喜你，新增成功。', 'success');
                // 关闭弹出层
                this.onCancel();
                this.treeRefresh();
              } else {
                this.openLayer('消息', res.data, 'error');
              }
            });*/
          } else {
            // 更新
            that.$api.put('/ecs', JSON.stringify(formData), (res) => {
              console.log(res);
              if(res !== undefined && res.status !== undefined && res.status === 200) {
                this.openLayer('消息', '恭喜你，修改成功。', 'success');
                // 关闭弹出层
                this.onCancel();
                this.treeRefresh();
              } else {
                this.openLayer('消息', res.data, 'error');
              }
            });
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    onCancel() {
      this.cancel();
    },
    // 文件上传
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    beforeUpload() {
      console.log('执行');
      if(this.form.config === undefined || this.form.config.host === null || this.form.config.host === '' ) {
        this.$message.warning(`请填写连接主机，将根据连接的主机进行文件夹的创建`);
        return;
      }
      //
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      console.log(fileList);
      return this.$confirm(`确定移除 ${ file.name }？`);
    }
  }
}
</script>

<style scoped>
.el-select {
  display: block;
}
</style>