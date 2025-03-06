
<script lang="ts" setup>
import {Expand, Fold, SwitchButton,} from '@element-plus/icons-vue';
import {
  defineAsyncComponent,
  markRaw,
  onMounted,
  reactive,
  ref,
  resolveComponent,
  defineComponent,
  h,
  nextTick,
  computed,
  provide,
  inject
} from 'vue';
import axios from '@/network';
import {msg} from '@/utils/Utils';
import router from '@/router'
import {getLangData} from "@/i18n/locale";

const langData = getLangData()

const layout = ref('main_left')

const data = reactive({
  currentPage: langData.mainMainPage,
  user: {},
  smInfo: {},
  adminMenus: [],
  menus: [],
  menuEntry: {},
})
provide('session', data)
const menuMap = reactive({})
const logout = () => {
  axios({
    url: '/system/logout',
    method: 'post'
  }).then((res: any) => {
    if (res.data.state == 'OK') {
      router.push({path: '/'})
    } else {
      msg(res.data.errorMessage, 'warning')
    }
  }).catch((err: Error) => {
    msg('请求异常', 'error')
  })
}
onMounted(() => {
  queryUserinfo()
})

const queryUserinfo = () => {
  axios({
    url: '/system/user',
    method: 'get'
  }).then((res: any) => {
    if (res.data.state == 'OK') {

      let user = res.data.body.user
      data.smInfo = res.data.body.smInfo
      data.user.userName = user.userName
      data.user.roleName = user.roleName
      data.menus = user.menus
      if (data.menus && data.menus.length > 0) {
        data.menus.forEach(m => {
          data.menuEntry[m.name] = m.menuDesc
          menuMap[m.url] = m.menuDesc
          if (m.menus && m.menus.length > 0) {
            m.menus.forEach(sm => {
              data.menuEntry[sm.name] = sm.menuDesc
              menuMap[sm.url] = sm.menuDesc
            })
          }
        })
      }
      data.adminMenus = res.data.body.allMenus
      if (data.adminMenus && data.adminMenus.length > 0) {
        data.adminMenus.forEach(item => {
          menuMap[item.url] = item.menuDesc
          if (item.menus && item.menus.length > 0) {
            item.menus.forEach(sItem => {
              menuMap[sItem.url] = sItem.menuDesc
            })
          }
        })
      }
    } else {
      let content = '调用 ' + res.config.baseURL + res.config.url + ': ' + res.data.errorMessage;
      msg(content, "warning")
    }
  }).catch((err: Error) => {
    console.error(err)
    msg('请求异常', 'error')
  })
}

const getIconComponent = (iconName: string) => {
  return resolveComponent(iconName);
};

// 菜单收起状态
const isCollapse = ref(false);

// 当前激活的菜单项
const activeMenu = ref('1');

// 当前激活的 Tab 页
const activeTab = ref('');

// Tab 页列表
const tabs = ref<{ name: string; label: string; component: any }[]>([]);

// 切换菜单收起状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};

const checkUrlStatus = async (url: string) => {
  try {
    const response = await fetch(url, {method: 'HEAD'});
    return response.status;
  } catch (error) {
    console.error('URL 检查失败:', error);
    return 500;
  }
};

const addTimestamp = (iframeUrl) => {
  let timestamp = new Date().getTime();
  if (iframeUrl.includes('#')) {
    let array = iframeUrl.split('#')
    if (array[0].includes('?')) {
      iframeUrl = array[0] + '&h_sm_t=' + timestamp + '#' + array[1]
    } else {
      iframeUrl = array[0] + '?h_sm_t=' + timestamp + '#' + array[1]
    }
  } else {
    if (iframeUrl.includes('?')) {
      iframeUrl = iframeUrl + '&h_sm_t=' + timestamp
    } else {
      iframeUrl = iframeUrl + '?h_sm_t=' + timestamp
    }
  }
  return iframeUrl
}

// 处理菜单项选择
const handleMenuSelect = async (index: string) => {
  let component;

  if (index.startsWith('http:') || index.startsWith('https:')) {
    // 如果是外部链接，直接在新标签页打开
    window.open(index, '_blank');
  } else {
    activeMenu.value = index;
    let menuName = menuMap[index];
    langData.mainMainPage = menuName;

    // 检查是否已经存在该 Tab
    const tab = tabs.value.find((tab) => tab.name === index);
    if (!tab) {
      if (index.startsWith('inner:')) {
        // 处理内部 iframe
        let iframeUrl = index.substring(6);
        iframeUrl = addTimestamp(iframeUrl)
        console.log('加载菜单链接: ', iframeUrl)
        component = defineComponent({
          setup() {
            const iframe = ref<HTMLIFrameElement | null>(null);
            const isLoading = ref(true); // 加载状态

            const onIframeLoad = () => {
              if (iframe.value && iframe.value.contentWindow?.document.body) {
                // 动态设置 iframe 的高度
                let contentHeight = iframe.value.contentWindow.document.body.scrollHeight;
                if (contentHeight < 400) {
                  contentHeight = window.innerHeight - 156;
                }
                iframe.value.style.height = `${contentHeight}px`;
                console.log('iframe 高度已调整:', contentHeight);

                // 监听内容变化
                const observer = new MutationObserver(() => {
                  let newHeight = iframe.value.contentWindow.document.body.scrollHeight;
                  if (newHeight <= 0) {
                    return
                  }
                  if (newHeight < 400) {
                    newHeight = window.innerHeight - 156;
                  }
                  iframe.value.style.height = `${newHeight}px`;
                  console.log('iframe 高度动态调整:', newHeight);
                });

                observer.observe(iframe.value.contentWindow.document.body, {
                  childList: true,
                  subtree: true,
                });

                // 关闭加载状态
                isLoading.value = false;
              }
            };

            return {iframeUrl, onIframeLoad, iframe, isLoading};
          },
          render() {
            return h('div', {style: {width: '100%', height: '100%', position: 'relative'}}, [
              // 加载动画
              this.isLoading
                  ? h('div', {
                    style: {
                      position: 'absolute',
                      top: '50%',
                      left: '50%',
                      transform: 'translate(-50%, -50%)'
                    }
                  }, '加载中...')
                  : null,
              // iframe
              h('iframe', {
                src: this.iframeUrl,
                style: {width: '100%', height: '100%', border: 'none'},
                onLoad: this.onIframeLoad,
                ref: 'iframe',
              }),
            ]);
          },
        });
      } else {
        // 处理内部 Vue 组件
        component = defineAsyncComponent(() => import(`@/views${index}.vue`))
      }

      // 将组件添加到 tabs
      tabs.value.push({
        name: index,
        label: menuName,
        component: markRaw(component),
      });
    }

    // 激活当前 Tab
    activeTab.value = index;
  }
};

// 移除 Tab 页
const removeTab = (tabName: string) => {
  const index = tabs.value.findIndex((tab) => tab.name === tabName);
  // console.log("关闭tab: ",tabName)
  tabs.value.splice(index, 1);
  if (tabName === activeTab.value) {
    activeTab.value = tabs.value[0]?.name || '';
  }
};

const refreshPage = () => {
  window.location.reload();
};

const tabClick = (pane, ev) => {
}

// 防抖函数
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

// 重写 ResizeObserver
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
    <el-container style="height: 100vh; overflow: hidden; border-radius: 8px;">
      <el-header class="header">
        <div style="display: flex; cursor: pointer" @click="refreshPage">
          <el-icon size="20">
            <component :is="getIconComponent('HomeIcon')"/>
          </el-icon>
          <span style="font-size:1em;margin-left: 10px">{{ data.smInfo.title }}</span>
        </div>
        <div style="display: flex; align-items: center;">
          <el-switch
              v-model="layout"
              class="ml-2"
              inline-prompt
              style="--el-switch-on-color: #79BBFF; --el-switch-off-color: #95D475;margin-right: 5px"
              active-value="main_left"
              inactive-value="main_top"
              :active-text="langData.mainLayoutUd"
              :inactive-text="langData.mainLayoutLr"
              @change="router.push({path:`/${layout}`})"
          />
          <!--          <TimeComponent/>-->
          <span style="margin-right: 10px;margin-left:0px; padding:0;font-size: 0.6em;">
            {{ data.user.userName }}
          </span>
          <el-icon @click="logout" style="cursor: pointer;color: red;" size="20">
            <SwitchButton/>
          </el-icon>
        </div>
      </el-header>
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside
            :width="isCollapse ? '64px' : '200px'"
            style="overflow: hidden; transition: width 0.3s ease;margin-top: 60px;height: calc(100vh - 60px)"
        >
          <div class="menu-header">
            <el-icon @click="toggleCollapse" style="cursor: pointer; color: #fff;">
              <component :is="isCollapse ? Expand : Fold"/>
            </el-icon>
          </div>
          <el-menu
              :default-active="activeMenu"
              :collapse="isCollapse"
              @select="handleMenuSelect"
              mode="vertical"
              :ellipsis="false"
              popper-effect="dark"
              style="height: calc(100vh - 100px); overflow-y: auto;overflow-x:hidden"
          >
            <template v-for="menu in data.adminMenus" v-if="data.user.roleName=='ADMIN'">
              <el-sub-menu v-if="menu.menus && menu.menus.length > 0" :index="menu.url">
                <template #title>
                  <el-icon>
                    <component :is="getIconComponent(menu.iconName)"/>
                  </el-icon>
                  <span>{{ menu.menuDesc }}</span>
                </template>
                <template v-for="subMenu in menu.menus">
                  <el-menu-item :index="subMenu.url">
                    <el-icon>
                      <component :is="getIconComponent(subMenu.iconName)"/>
                    </el-icon>
                    <template #title>{{ subMenu.menuDesc }}</template>
                  </el-menu-item>
                </template>
              </el-sub-menu>
              <el-menu-item v-else :index="menu.url">
                <el-icon>
                  <component :is="getIconComponent(menu.iconName)"/>
                </el-icon>
                <template #title>{{ menu.menuDesc }}</template>
              </el-menu-item>
            </template>
            <template v-for="menu in data.menus" v-else>
              <el-sub-menu v-if="menu.menus && menu.menus.length > 0" :index="menu.url">
                <template #title>
                  <el-icon>
                    <component :is="getIconComponent(menu.iconName)"/>
                  </el-icon>
                  <span>{{ menu.menuDesc }}</span>
                </template>
                <template v-for="subMenu in menu.menus">
                  <el-menu-item :index="subMenu.url">
                    <el-icon>
                      <component :is="getIconComponent(subMenu.iconName)"/>
                    </el-icon>
                    <template #title>{{ subMenu.menuDesc }}</template>
                  </el-menu-item>
                </template>
              </el-sub-menu>
              <el-menu-item v-else :index="menu.url">
                <el-icon>
                  <component :is="getIconComponent(menu.iconName)"/>
                </el-icon>
                <template #title>{{ menu.menuDesc }}</template>
              </el-menu-item>
            </template>
          </el-menu>
        </el-aside>
        <!-- 右侧内容区域 -->
        <el-container style="height: calc(100vh - 60px); overflow: hidden; background-color: #f5f7fa;margin-top: 60px">
          <div style="width: 100%;">
            <!-- 顶部区域 -->
            <div
                style="height: 39px; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; background-color: #fff; border-bottom: 1px solid #e4e7ed;">
              <!-- 导航面包屑 -->
              <el-breadcrumb separator="/">
                <el-breadcrumb-item>{{ langData.mainFirstPage}}</el-breadcrumb-item>
                <el-breadcrumb-item>{{ langData.mainMainPage }}</el-breadcrumb-item>
              </el-breadcrumb>
            </div>

            <!-- Tab 页区域 -->
            <div style="height: calc(100vh - 100px);">
              <el-tabs
                  v-model="activeTab"
                  type="card"
                  closable
                  @tab-remove="removeTab"
                  class="custom-tabs"
                  :class="{ 'has-tabs': tabs.length > 0 }"
                  @tab-click="tabClick"
              >
                <el-tab-pane
                    v-for="tab in tabs"
                    :key="tab.name"
                    :label="tab.label"
                    :name="tab.name"
                    :class="tab.name.includes('NoOverflow')?'custom-tab-pane-ai':'custom-tab-pane'"
                >
                  <component :is="tab.component"/>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.container {
  flex-grow: 1;
  padding: 0;
  overflow: hidden;
  width: 100%;
  height: 100vh;
}

.header {
  position: fixed;
  top: 0px;
  left: 0px;
  right: 0px;
  z-index: 1000;
  background-color: #ffffff;
  color: #333;
  padding: 10px 20px;
  border-bottom: 1px solid #eaeaea;
  display: flex; /* 启用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  justify-content: space-between; /* 左右分布 */
}

.menu-header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 20px;
  height: 40px;
  line-height: 64px;
  background-color: #1296db; /* 蓝色背景 */
  color: #fff; /* 白色文字 */
}

.custom-tabs {
  height: 100%;
  margin-top: 2px;
  background-color: white;
}

.custom-tabs .el-tabs__item {
  font-size: 14px;
  color: #333;
  padding: 10px 20px;
  transition: all 0.3s;
}

.custom-tabs .el-tabs__item.is-active {
  color: #409EFF;
  border-bottom: 1px solid #409EFF;
}

.custom-tabs .el-tabs__item:hover {
  color: #409EFF;
}

.custom-tab-pane {
  height: calc(100vh - 160px);
  overflow-y: auto;
}

.custom-tab-pane-ai {
  height: calc(100vh - 160px);
  overflow-y: hidden;
}

/* 默认隐藏底部边框线 */
:deep(.el-tabs__header) {
  border-bottom: none;
}

/* 有 Tab 页时显示底部边框线 */
:deep(.el-tabs.has-tabs .el-tabs__header) {
  border-bottom: 1px solid #ddd;
}

:deep(.el-tabs__item) {
  background-color: #fafafa; /* 未选中背景色 */
  position: relative; /* 为伪元素定位 */
  -webkit-transition: all .3s cubic-bezier(.645, .045, .355, 1);
  transition: all .3s cubic-bezier(.645, .045, .355, 1);
}

:deep(.el-tabs__item::after) {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 1px;
  background-color: #ddd; /* 分隔线颜色 */
}

:deep(.el-tabs__item.is-active) {
  background-color: white; /* 选中背景色 */
  color: #1890ff;
  border-color: #e8e8e8;
  border-bottom: 1px solid #fff;
}

:deep(.el-tabs__item.is-active::after) {
  display: none; /* 选中状态隐藏分隔线 */
}
</style>