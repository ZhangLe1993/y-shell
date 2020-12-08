<template>
  <div>
    <el-row>
      <el-col :span="4">
        <el-tabs type="border-card">
          <el-tab-pane>
            <span slot="label"><i class="el-icon-date"></i> 会话管理器</span>
            <Tree :addTab="addTab"></Tree>
          </el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col :span="20">
        <TerminalTabs ref="terminalTabs"></TerminalTabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    import TerminalTabs from '../components/TerminalTabs';   // 引入子组件XTerm
    import Tree from '../components/Tree';   // 引入子组件XTerm

    export default {
      name: "Terminal",
      components: {
        TerminalTabs: TerminalTabs,
        Tree: Tree,
      },
      methods: {
        addTab(node) {
          // const targetName = this.$refs.terminalTabs.editableTabsValue
          this.$refs.terminalTabs.addTab(node);
        },

      }
    }
</script>

<style scoped>

</style>