<script lang="ts" setup>
import {
  Edit
} from '@element-plus/icons-vue'
import {ref, reactive, onMounted, computed, provide, inject} from 'vue'
import axios from '@/network'
import {msg} from '@/utils/Utils'
import type {FormInstance, FormRules} from 'element-plus'
import router from "@/router";

const user = inject('session').user

const formLabelWidth = ref('140px')
const form = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  desc: ''
})
const data = reactive({
  roles: [],
  total: 0,
  menus: []
});

const queryRoleList = () => {
  axios({
    url: '/roles/list',
    method: 'post',
    data: form,
    params: {
      pageNum: form.pageNum,
      pageSize: form.pageSize
    }
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      data.roles = res.data.body.list
      data.total = res.data.body.total
    } else {
      msg(res.data.errorMessage, 'warning')
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

const dialogFormVisible = ref(false)
const dialogTitle = ref('新增角色')
const roleForm = reactive({
  name: '',
  desc: ''
})
const formRef = ref<FormInstance>()
const rules = reactive<FormRules>({
  name: [
    {required: true, message: '不能为空', trigger: 'blur'}
  ]
})

const updateRole = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      axios({
        url: '/roles/role',
        method: dialogTitle.value == '新增角色' ? 'post' : 'put',
        data: roleForm
      }).then((res: any) => {
        if (res.data.state == 'OK') {
          msg(res.data.body, 'success')
          dialogFormVisible.value = false
          queryRoleList()
        } else {
          msg(res.data.errorMessage, 'warning')
        }
      }).catch((err: Error) => {
        msg('请求异常', 'error')
      })
    }
  })
}

const showRoleAddDialog = () => {
  dialogFormVisible.value = true
  dialogTitle.value = '新增角色'
  roleForm.name = ''
  roleForm.desc = ''
}

const showRoleEditDialog = (scope) => {
  dialogFormVisible.value = true
  dialogTitle.value = '编辑角色'
  roleForm.name = scope.row.name
  roleForm.desc = scope.row.desc
}

const deleteRole = (scope) => {
  axios({
    url: '/roles/role',
    method: 'delete',
    params: {
      name: scope.row.name
    }
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      msg(res.data.body, 'success')
      queryRoleList()
    } else {
      msg(res.data.errorMessage, 'warning')
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

const dialogFormVisible2 = ref(false)
const dialogTitle2 = ref('配置菜单')
const titles = ref(['待选择', '已选择'])
const roleForm2 = reactive({
  role: {
    name: ''
  },
  menus: [],
  mapTo: function () {
    this.menus = this.menus.map(item => {
      return {name: item}
    })
  }
})
const formRef2 = ref<FormInstance>()
const rules2 = reactive<FormRules>({
  name: [
    {required: true, message: '不能为空', trigger: 'blur'}
  ]
})

const updateRoleMenus = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      roleForm2.mapTo()
      axios({
        url: '/roles/role/menus',
        method: 'post',
        data: roleForm2
      }).then((res: any) => {
        if (res.data.state == 'OK') {
          msg(res.data.body, 'success')
          dialogFormVisible2.value = false
        } else {
          msg(res.data.errorMessage, 'warning')
        }
      }).catch((err: Error) => {
        msg('请求异常', 'error')
      })
    }
  })
}

const showMenuConfigDialog = (scope) => {
  dialogFormVisible2.value = true
  dialogTitle2.value = '配置菜单(' + scope.row.name + ')'
  roleForm2.role.name = scope.row.name
  axios({
    url: '/roles/role/menus',
    method: 'get',
    params: {name: scope.row.name}
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      data.menus = res.data.body.all
      roleForm2.menus = res.data.body.conf
      console.log('all: %o, conf: %o', data.menus, roleForm2.menus)
    } else {
      msg(res.data.errorMessage, 'warning')
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}

const headerCellStyle = () => {
  // 添加表头颜色
  return {backgroundColor: '#f5f5f5', color: '#333', fontWeight: 'bold'};
}

onMounted(() => {
  queryRoleList()
});

const debounce = (callback: (...args: any[]) => void, delay: number) => {
  let tid: any;
  return function (...args: any[]) {
    const ctx = self;
    tid && clearTimeout(tid);
    tid = setTimeout(() => {
      callback.apply(ctx, args);
    }, delay);
  };
};

const _ = (window as any).ResizeObserver;
(window as any).ResizeObserver = class ResizeObserver extends _ {
  constructor(callback: (...args: any[]) => void) {
    callback = debounce(callback, 20);
    super(callback);
  }
};

</script>

<template>
  <div class="container">
    <el-table :data="data.roles" style="width: 100%" :border="true" table-layout="fixed" :stripe="true"
              size="small" :highlight-current-row="true" :header-cell-style="headerCellStyle">
      <el-table-column fixed="left" label="操作" width="180" header-align="center" align="center" v-if="user.roleName=='ADMIN'">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="showRoleEditDialog(scope)">编辑
          </el-button>
          <el-popconfirm title="删除角色会把关联的账号全部删除，请确认是否删除?" @confirm="deleteRole(scope)"
                         icon-color="red"
                         confirm-button-type="danger">
            <template #reference>
              <el-button link type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button link type="primary" size="small" @click="showMenuConfigDialog(scope)">配置菜单</el-button>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="操作" width="180" header-align="center" align="center" v-else-if="user.roleName=='MANAGE'">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="showMenuConfigDialog(scope)">配置菜单</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="desc" label="描述" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="fmtCreatedAt" label="创建时间" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="fmtUpdatedAt" label="修改时间" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
    </el-table>
    <el-pagination class="page" v-model:page-size="form.pageSize" v-model:current-page="form.pageNum"
                   layout="->, total, sizes, prev, pager, next, jumper" v-model:total="data.total"
                   @size-change="queryRoleList()"
                   @current-change="queryRoleList()" @prev-click="queryRoleList()" @next-click="queryRoleList()"
                   :small="true" :background="true"
                   :page-sizes="[5, 10, 20, 50, 100]" v-if="user.roleName=='ADMIN'"/>
    <div class="addBtn">
      <el-button :icon="Edit" size="small" round @click="showRoleAddDialog()" v-if="user.roleName=='ADMIN'">添加新角色
      </el-button>
    </div>

    <el-dialog v-model="dialogFormVisible" :title="dialogTitle" draggable>
      <el-form :model="roleForm" label-position="right" size="small" :inline="false" ref="formRef" :rules="rules"
               label-width="20%">
        <el-form-item label="角色名称：" prop="name">
          <el-input v-model="roleForm.name" type="text" :disabled="dialogTitle=='编辑角色'"/>
        </el-form-item>
        <el-form-item label="校色描述：" prop="url">
          <el-input v-model="roleForm.desc" type="textarea" rows="3"/>
        </el-form-item>
      </el-form>
      <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">取消</el-button>
                  <el-button type="primary" @click="updateRole(formRef)">保存</el-button>
                </span>
      </template>
    </el-dialog>


    <el-dialog v-model="dialogFormVisible2" :title="dialogTitle2" draggable>
      <el-form :model="roleForm2" label-position="right" size="small" :inline="false" ref="formRef2" :rules="rules2"
               label-width="20%">
        <el-transfer
            v-model="roleForm2.menus"
            filterable
            :filter-method="(query, item) => item.label.includes(query)"
            filter-placeholder="输入菜单名"
            :data="data.menus"
            :titles="titles"
        />
      </el-form>
      <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dialogFormVisible2 = false">取消</el-button>
                  <el-button type="primary" @click="updateRoleMenus(formRef2)">保存</el-button>
                </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.container {
  flex-grow: 1;
  padding: 0 2%;
  overflow-x: auto;
  overflow-y: hidden;
  width: 96%;
  height: calc(100vh + 60px);
}

.addBtn {
  margin-top: 5px;
  text-align: center;
}
</style>