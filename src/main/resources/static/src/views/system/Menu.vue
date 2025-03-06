<script lang="ts" setup>
import {
  Edit
} from '@element-plus/icons-vue'
import {ref, reactive, onMounted, computed, provide, inject} from 'vue'
import axios from '@/network'
import {msg} from '@/utils/Utils'
import type {FormInstance, FormRules} from 'element-plus'
import router from "@/router";
import {getLangData} from "@/i18n/locale";

const langData = getLangData()

const user = ref({})

const getUserInfo = () => {
  axios({
    url: '/system/user',
    method: 'get'
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      user.value = res.data.body.user
    } else {
      let content = res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.error(err)
    msg(langData.axiosRequestErr, 'error')
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
  levels: [{key: 1, label: 'Level-1'}, {key: 2, label: 'Level-2'}]
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
      data.level1Menus.unshift({name: '-', menuDesc: langData.menuRoot, url: '/', parentId: '', menuLevel: 0})
      data.iconList = res.data.body.iconList
    } else {
      let content = res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.log('', err)
    msg(langData.axiosRequestErr, 'error')
  })
}

const dialogFormVisible = ref(false)
const dialogTitle = ref(langData.menuDialogTitleAdd)
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
  name: [{required: true, message: langData.formValidateNotNull, trigger: 'blur'},],
  menuDesc: [{required: true, message: langData.formValidateNotNull, trigger: 'blur'},],
  url: [{required: true, message: langData.formValidateNotNull, trigger: 'blur'}],
  parentId: [{required: true, message: langData.formValidateNotNull, trigger: 'blur'}],
  menuLevel: [{required: true, message: langData.formValidateNotNull, trigger: 'blur'}],
  iconName: [{required: true, message: langData.formValidateNotNull, trigger: 'blur'}],
  orderIndex: [{required: true, message: langData.formValidateNotNull, trigger: 'blur'}]
})

const updateMenu = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      menuForm.save()
      axios({
        url: '/menus/menu',
        method: dialogTitle.value == langData.menuDialogTitleAdd ? 'post' : 'put',
        data: menuForm
      }).then((res: any) => {
        if (res.data.state == 'OK') {
          msg(res.data.body, 'success')
          dialogFormVisible.value = false
          queryMenuList()
        } else {
          let content = res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
          msg(content, "warning")
        }
      }).catch((err: Error) => {
        console.log('', err)
        msg(langData.axiosRequestErr, 'error')
      })
    }
  })
}

const showMenuAddDialog = () => {
  dialogFormVisible.value = true
  dialogTitle.value = langData.menuDialogTitleAdd
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
  dialogTitle.value = langData.menuDialogTitleEdit
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
      let content = res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.log('', err)
    msg(langData.axiosRequestErr, 'error')
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
      let content = res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.log('', err)
    msg(langData.axiosRequestErr, 'error')
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
    <el-form :model="form" size="small" label-position="right" inline-message :inline="true">
      <el-form-item :label="langData.menuFormMenuName" prop="name">
        <el-input v-model="form.name"/>
      </el-form-item>
      <el-form-item :label="langData.menuFormMenuUrl" prop="url">
        <el-input v-model="form.url"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="queryMenuList()">{{langData.menuFormBtnQuery}}</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="data.menus" style="width: 100%" :border="true" table-layout="fixed" :stripe="true"
              size="small" :highlight-current-row="true" :header-cell-style="headerCellStyle">
      <el-table-column fixed="left" :label="langData.menuTableHeaderOp" width="180" header-align="center" align="center"
                       v-if="user.roleName=='ADMIN' || user.roleName=='MANAGE'">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="showMenuEditDialog(scope)">{{langData.menuTableOpEdit}}
          </el-button>
          <el-popconfirm :title="langData.menuConfirmDeleteTitle" @confirm="deleteMenu(scope)"
                         icon-color="red"
                         confirm-button-type="danger">
            <template #reference>
              <el-button link type="danger" size="small">{{langData.menuTableOpDelete}}
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column prop="name" :label="langData.menuTableHeaderMenuName" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="menuDesc" :label="langData.menuTableHeaderMenuDesc" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="url" :label="langData.menuTableHeaderMenuUrl" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="iconName" :label="langData.menuTableHeaderIcon" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="parentId" :label="langData.menuTableHeaderParentMenu" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="menuLevel" :label="langData.menuTableHeaderMenuLevel" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="orderIndex" :label="langData.menuTableHeaderMenuOrder" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="fmtCreatedAt" :label="langData.menuTableHeaderCreateTime" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
      <el-table-column prop="fmtUpdatedAt" :label="langData.menuTableHeaderUpdateTime" :show-overflow-tooltip="true" header-align="center"
                       align="center"/>
    </el-table>
    <el-pagination class="page" v-model:page-size="form.pageSize" v-model:current-page="form.pageNum"
                   layout="->, total, sizes, prev, pager, next, jumper" v-model:total="data.total"
                   @size-change="queryMenuList()"
                   @current-change="queryMenuList()" @prev-click="queryMenuList()" @next-click="queryMenuList()"
                   :small="true" :background="true"
                   :page-sizes="[5, 10, 20, 50, 100]"/>
    <div class="addBtn">
        <el-button :icon="Edit" size="small" round @click="showMenuAddDialog" v-if="user.roleName=='ADMIN' || user.roleName=='MANAGE'">{{langData.menuAdd}}
        </el-button>
    </div>

    <el-dialog v-model="dialogFormVisible" :title="dialogTitle" draggable width="40%">
      <el-form :model="menuForm" label-position="right" size="small" :inline="false" ref="formRef" :rules="rules"
               label-width="20%">
        <el-form-item :label="langData.menuDialogMenuName" prop="name">
          <el-autocomplete
              v-model="menuForm.name"
              :fetch-suggestions="querySearchMenus"
              clearable
              style="width: 100%"
              :disabled="dialogTitle==langData.menuDialogTitleEdit"
          />
        </el-form-item>
        <el-form-item :label="langData.menuDialogMenuDesc" prop="menuDesc">
          <el-input v-model="menuForm.menuDesc" style="width: 100%"/>
        </el-form-item>
        <el-form-item :label="langData.menuDialogMenuUrl" prop="url">
          <el-input v-model="menuForm.url" class="input-with-select" style="width: 100%">
            <template #prepend>
              <el-select v-model="menuForm.urlType" :placeholder="langData.formSelectPlaceholder" style="width: 90px" size="small">
                <el-option :label="langData.menuUrlRoute" value="route:"/>
                <el-option :label="langData.menuUrlIframe" value="inner:"/>
                <el-option :label="langData.menuUrlOuter" value="http:"/>
              </el-select>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item :label="langData.menuDialogParentMenu" prop="parentId">
          <el-select v-model="menuForm.parentId" placeholder="请选择" size="small" filterable style="width: 100%">
            <el-option :key="m.name" :label="m.menuDesc" :value="m.name" v-for="m in data.level1Menus"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="langData.menuDialogMenuLevel" prop="menuLevel">
          <el-select v-model="menuForm.menuLevel" :placeholder="langData.formSelectPlaceholder" size="small" style="width: 100%">
            <el-option :key="level.key" :label="level.label" :value="level.key" v-for="level in data.levels"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="langData.menuDialogMenuIcon" prop="iconName">
          <el-select v-model="menuForm.iconName" :placeholder="langData.formSelectPlaceholder" size="small" filterable style="width: 100%">
            <el-option :key="icon.key" :label="icon.value" :value="icon.key" v-for="icon in data.iconList"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="langData.menuDialogMenuOrder" prop="orderIndex">
          <el-input v-model="menuForm.orderIndex" type="number" style="width: 100%"/>
        </el-form-item>
      </el-form>
      <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogFormVisible = false">{{langData.menuDialogBtnCancel}}</el-button>
              <el-button type="primary" @click="updateMenu(formRef)">{{langData.menuDialogBtnSave}}</el-button>
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

.addBtn {
  margin-top: 5px;
  text-align: center;
}
</style>