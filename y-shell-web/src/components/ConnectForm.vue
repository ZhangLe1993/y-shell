<template>
  <el-dialog :title="connectTitle" :visible.sync="dialogFormVisible" :show-close="false" :close-on-click-modal="false" width="30%">

    <el-form :model="form" :rules="rules" ref="form">

      <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" auto-complete="off"></el-input>
      </el-form-item>

      <el-row>
        <el-col :span="16">
            <el-form-item label="主机地址" :label-width="formLabelWidth" prop="host">
              <el-input v-model="form.host" auto-complete="off"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item label="端口号" :label-width="formLabelWidth" prop="port">
              <el-input v-model="form.port" auto-complete="off"></el-input>
            </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="用户名" :label-width="formLabelWidth" prop="user">
        <el-input v-model="form.user" auto-complete="off"></el-input>
      </el-form-item>

      <el-form-item label="验证方式" :label-width="formLabelWidth">
        <el-select v-model="form.authMethod" placeholder="请选择验证方式" @change="chooseMethod">
          <el-option label="密码" value="password"></el-option>
          <el-option label="密钥" value="rsa"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item v-if=" form.authMethod ==='password'" label="密码" :label-width="formLabelWidth" prop="password">
        <el-input v-model="form.password" auto-complete="off"></el-input>
      </el-form-item>

      <el-row v-if=" form.authMethod ==='rsa'">
        <el-col :span="18">
          <el-form-item label="选择文件" :label-width="formLabelWidth" prop="identity">
            <el-select v-model="form.identity" filterable placeholder="选择已上传的密钥文件">
              <el-option v-for="item in rsaOptions" :key="item" :label="item" :value="item"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-upload
              :action="uploadUrl()"
              :before-upload="beforeUpload"
              multiple
              :show-file-list="false"
              :limit="1"
              :on-exceed="handleExceed"
              :on-success="handleSuccess"
              :on-progress="uploadProcess"
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
      options: [ {value: 'password', label: '密码'}, {value: 'rsa', label: '密钥'}],
      formLabelWidth: '80px',
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur' }
        ],
        description: [
          { min: 0, max: 500, message: '长度在 0 到 500 个字符', trigger: 'blur' },
        ],
        host: [
          { required: true, message: '请输入主机地址', trigger: 'blur' },
        ],
        port: [
          { required: true, message: '请输入端口号', trigger: 'blur' },
        ],
        user: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [{required: true, message: '请输入密码', trigger: 'blur' },],
        identity: [{ required: true, message: '请选择密钥文件', trigger: 'change' },],
      },
      rsaOptions: [],
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
      this.$refs[formName].validate(async (valid) => {
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
            const config = { host : formData.host, port : formData.port, user : formData.user, password : formData.password, identity : formData.identity, passphrase : formData.passphrase,authMethod: formData.authMethod };
            formData.config = JSON.stringify(config);
            console.log(formData);
            // 新增
            that.$api.post('/ecs', JSON.stringify(formData), (res) => {
              if(res !== undefined && res.status !== undefined && res.status === 200) {
                this.openLayer('消息', '恭喜你，新增成功。', 'success');
                // 关闭弹出层
                this.onCancel();
                this.treeRefresh();
              } else {
                this.openLayer('消息', res.data, 'error');
              }
            });
          } else {
            // 更新
            const config = { host : formData.host, port : formData.port, user : formData.user, password : formData.password, identity : formData.identity, passphrase : formData.passphrase,authMethod: formData.authMethod };
            formData.config = JSON.stringify(config);
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
    async chooseMethod(value) {
      if(value === 'rsa') {
        const res = await this.initRsaList();
        this.rsaOptions = res.data;
      }
    },
    onCancel() {
      this.cancel();
    },
    uploadUrl() {
      return 'http://127.0.0.1:8082/rsa/upload?host=' + this.form.host;
    },
    // 文件上传
    // eslint-disable-next-line no-unused-vars
    async handleSuccess(res, file) {
      if(this.form.authMethod === 'rsa') {
        const res = await this.initRsaList();
        this.rsaOptions = res.data;
        this.openLayer('消息', '文件上传成功, 密钥文件列表已刷新', 'success');
      }
    },
    // eslint-disable-next-line no-unused-vars
    uploadProcess(event, file, fileList) {

    },
    beforeUpload(file) {
      console.log('文件校验');
      if(this.form.host === null || this.form.host === '' ) {
        this.$message.warning(`请填写连接主机，将根据连接的主机进行文件夹的创建`);
        return false;
      }
      const limit = file.size / 1024 / 1024  < 1;
      if ('rsa'.indexOf(file.type) === -1) {
        this.$message.error('请上传正确的文件格式');
        return false;
      }
      if (!limit) {
        this.$message.error('RSA文件不能超过1MB哦!');
        return false;
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    async initRsaList() {
      const that = this;
      return await that.$api.syncGet('/rsa/list', { host : this.form.host });
    }

  },
  async mounted() {
    if(this.form.host === undefined || this.form.host === null || this.form.host === '') {
      this.rsaOptions = [];
    } else {
      const res = await this.initRsaList();
      this.rsaOptions = res.data;
    }
  }
}
</script>

<style scoped>
.el-select {
  display: block;
}
</style>