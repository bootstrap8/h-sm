<script lang="ts" setup>
import {
  Edit
} from '@element-plus/icons-vue'
import {ref, reactive, onMounted, computed, provide, inject} from 'vue'
import axios from '@/network'
import {msg} from '@/utils/Utils'
import type {FormInstance, FormRules} from 'element-plus'
import router from "@/router";

const user = ref({})

const getUserInfo = () => {
  axios({
    url: '/system/user',
    method: 'get'
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      user.value = res.data.body.user
    } else {
      let content = '调用 ' + res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.error(err)
    msg('请求异常', 'error')
  })
}

const formLabelWidth = ref('140px')
const form = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  menuDesc: '',
  url: ''
})
const data = reactive({
  menus: [],
  level1Menus: [],
  total: 0,
  iconList: [],
  levels: [{key: 1, label: '一级'}, {key: 2, label: '二级'}]
});

const headerCellStyle = () => {
  // 添加表头颜色
  return {backgroundColor: '#f5f5f5', color: '#333', fontWeight: 'bold'};
}

const queryMenuList = () => {
  axios({
    url: '/menus/list',
    method: 'post',
    data: form,
    params: {
      pageNum: form.pageNum,
      pageSize: form.pageSize
    }
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      data.menus = res.data.body.menus.list
      data.total = res.data.body.menus.total
      data.level1Menus = res.data.body.level1Menus
      data.level1Menus.unshift({name: '-', menuDesc: '根菜单', url: '/', parentId: '', menuLevel: 0})
      data.iconList = res.data.body.iconList
    } else {
      let content = '调用 ' + res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.log('', err)
    msg('请求异常', 'error')
  })
}

const dialogFormVisible = ref(false)
const dialogTitle = ref('新增菜单')
const menuForm = reactive({
  name: '',
  menuDesc: '',
  urlType: 'route:',
  url: '',
  parentId: "-",
  menuLevel: 1,
  iconName: 'LogIcon',
  orderIndex: 0,
  show: function () {
    if (this.url.startsWith('inner:')) {
      this.urlType = 'inner:'
      this.url = this.url.substring(6)
    } else if (this.url.startsWith('http')) {
      this.urlType = 'http:'
    } else {
      this.urlType = 'route:'
    }
  },
  save: function () {
    if (this.urlType == 'route:') {
      // do nothing
    } else if (this.urlType == 'inner:') {
      this.url = this.urlType + this.url
    }
  }
})
const formRef = ref<FormInstance>()
const rules = reactive<FormRules>({
  name: [{required: true, message: '不能为空', trigger: 'blur'},],
  menuDesc: [{required: true, message: '不能为空', trigger: 'blur'},],
  url: [{required: true, message: '不能为空', trigger: 'blur'}],
  parentId: [{required: true, message: '不能为空', trigger: 'blur'}],
  menuLevel: [{required: true, message: '不能为空', trigger: 'blur'}],
  iconName: [{required: true, message: '不能为空', trigger: 'blur'}],
  orderIndex: [{required: true, message: '不能为空', trigger: 'blur'}]
})

const updateMenu = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      menuForm.save()
      axios({
        url: '/menus/menu',
        method: dialogTitle.value == '新增菜单' ? 'post' : 'put',
        data: menuForm
      }).then((res: any) => {
        if (res.data.state == 'OK') {
          msg(res.data.body, 'success')
          dialogFormVisible.value = false
          queryMenuList()
        } else {
          let content = '调用 ' + res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
          msg(content, "warning")
        }
      }).catch((err: Error) => {
        console.log('', err)
        msg('请求异常', 'error')
      })
    }
  })
}

const showMenuAddDialog = () => {
  dialogFormVisible.value = true
  dialogTitle.value = '新增菜单'
  menuForm.name = ''
  menuForm.menuDesc = ''
  menuForm.url = ''
  menuForm.parentId = "-"
  menuForm.menuLevel = 1
  menuForm.iconName = 'LogIcon'
  menuForm.orderIndex = 0
}

const showMenuEditDialog = (scope) => {
  dialogFormVisible.value = true
  dialogTitle.value = '编辑菜单'
  menuForm.id = scope.row.id
  menuForm.name = scope.row.name
  menuForm.menuDesc = scope.row.menuDesc
  menuForm.url = scope.row.url
  menuForm.show()
  menuForm.parentId = scope.row.parentId
  menuForm.menuLevel = scope.row.menuLevel
  menuForm.iconName = scope.row.iconName
  menuForm.orderIndex = scope.row.orderIndex
}

const deleteMenu = (scope) => {
  axios({
    url: '/menus/menu',
    method: 'delete',
    params: {name: scope.row.name}
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      msg(res.data.body, 'success')
      queryMenuList()
    } else {
      let content = '调用 ' + res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.log('', err)
    msg('请求异常', 'error')
  })
}

interface MenuItem {
  value: string
}

const supportedMenus = ref<MenuItem[]>([])
const querySupportedMenus = () => {
  axios({
    url: '/menus/supported/menus',
    method: 'get',
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      res.data.body.forEach(m => supportedMenus.value.push({value: m}))
    } else {
      let content = '调用 ' + res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.log('', err)
    msg('请求异常', 'error')
  })
}
const querySearchMenus = (queryString: string, cb: (arg: any) => void) => {
  const results = queryString
      ? supportedMenus.value.filter(m => m.value.toLowerCase().includes(queryString.toLowerCase()))
      : supportedMenus.value
  cb(results)
}

onMounted(() => {
  getUserInfo()
  querySupportedMenus()
  queryMenuList()
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
    <el-divider content-position="left">查询条件</el-divider>
    <el-form :model="form" size="small" label-position="right" inline-message :inline="true">
      <el-form-item label="菜单名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入..."/>
      </el-form-item>
      <el-form-item label="菜单url" prop="url">
        <el-input v-model="form.url" placeholder="请输入..."/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="queryMenuList()">查询</el-button>
        <el-button type="success" :icon="Edit" circle @click="showMenuAddDialog()" title="新增菜单"
                   v-if="user.roleName=='ADMIN' || user.roleName=='MANAGE'"/>
      </el-form-item>
    </el-form>

    <el-divider content-position="left">查询结果</el-divider>
    <el-table :data="data.menus" style="width: 100%" :border="true" table-layout="fixed" :stripe="true"
              size="small" :highlight-current-row="true" :header-cell-style="headerCellStyle">
      <el-table-column fixed="left" label="操作" width="180" header-align="center" align="center"
                       v-if="user.roleName=='ADMIN' || user.roleName=='MANAGE'">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="showMenuEditDialog(scope)">编辑
          </el-button>
          <el-popconfirm title="你确定要删除本条记录吗?" @confirm="deleteMenu(scope)"
                         icon-color="red"
                         confirm-button-type="danger">
            <template #reference>
              <el-button link type="danger" size="small">删除
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="菜单名称" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="menuDesc" label="菜单描述" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="url" label="菜单url" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="iconName" label="图标" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="parentId" label="父菜单" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="menuLevel" label="菜单层级" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="orderIndex" label="顺序" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="fmtCreatedAt" label="创建时间" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="fmtUpdatedAt" label="修改时间" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
    </el-table>
    <el-pagination class="page" v-model:page-size="form.pageSize" v-model:current-page="form.pageNum"
                   layout="->, total, sizes, prev, pager, next, jumper" v-model:total="data.total"
                   @size-change="queryMenuList()"
                   @current-change="queryMenuList()" @prev-click="queryMenuList()" @next-click="queryMenuList()"
                   :small="true" :background="true"
                   :page-sizes="[5, 10, 20, 50, 100]"/>

    <el-dialog v-model="dialogFormVisible" :title="dialogTitle" draggable width="40%">
      <el-form :model="menuForm" label-position="right" size="small" :inline="false" ref="formRef" :rules="rules"
               label-width="20%">
        <el-form-item label="菜单名称：" prop="name">
          <el-autocomplete
              v-model="menuForm.name"
              :fetch-suggestions="querySearchMenus"
              placeholder="输入菜单关键字"
              clearable
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单描述" prop="menuDesc">
          <el-input v-model="menuForm.menuDesc" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="菜单url：" prop="url">
          <el-input v-model="menuForm.url" class="input-with-select" style="width: 100%">
            <template #prepend>
              <el-select v-model="menuForm.urlType" placeholder="请选择" style="width: 90px" size="small">
                <el-option label="路由" value="route:"/>
                <el-option label="内部地址" value="inner:"/>
                <el-option label="外部地址" value="http:"/>
              </el-select>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="父菜单" prop="parentId">
          <el-select v-model="menuForm.parentId" placeholder="请选择" size="small" filterable style="width: 100%">
            <el-option :key="m.name" :label="m.menuDesc" :value="m.name" v-for="m in data.level1Menus"/>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单层级" prop="menuLevel">
          <el-select v-model="menuForm.menuLevel" placeholder="请选择" size="small" style="width: 100%">
            <el-option :key="level.key" :label="level.label" :value="level.key" v-for="level in data.levels"/>
          </el-select>
        </el-form-item>
        <el-form-item label="图标" prop="iconName">
          <el-select v-model="menuForm.iconName" placeholder="请选择" size="small" filterable style="width: 100%">
            <el-option :key="icon.key" :label="icon.value" :value="icon.key" v-for="icon in data.iconList"/>
          </el-select>
        </el-form-item>
        <el-form-item label="顺序" prop="orderIndex">
          <el-input v-model="menuForm.orderIndex" type="number" style="width: 100%"/>
        </el-form-item>
      </el-form>
      <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取消</el-button>
              <el-button type="primary" @click="updateMenu(formRef)">保存</el-button>
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
}
</style>