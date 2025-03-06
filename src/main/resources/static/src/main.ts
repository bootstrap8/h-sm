import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import store from './store'
import './assets/css/base.css'
import DashboardIcon from "@/components/icon/DashboardIcon.vue";
import FolderIcon from "@/components/icon/FolderIcon.vue";
import MenuIcon from "@/components/icon/MenuIcon.vue";
import LogIcon from "@/components/icon/LogIcon.vue";
import PermissionIcon from "@/components/icon/PermissionIcon.vue";
import RoleIcon from "@/components/icon/RoleIcon.vue";
import SettingsIcon from "@/components/icon/SettingsIcon.vue";
import UserIcon from "@/components/icon/UserIcon.vue";
import BaiduIcon from "@/components/icon/BaiduIcon.vue";
import GoogleIcon from "@/components/icon/GoogleIcon.vue";
import BingIcon from "@/components/icon/BingIcon.vue";
import TenantIcon from "@/components/icon/TenantIcon.vue";
import ClusterIcon from "@/components/icon/ClusterIcon.vue";
import APIIcon from "@/components/icon/APIIcon.vue";
import MiddlewareIcon from "@/components/icon/MiddlewareIcon.vue";
import K8sIcon from "@/components/icon/K8sIcon.vue";
import CloudIcon from "@/components/icon/CloudIcon.vue";
import ContainerIcon from "@/components/icon/ContainerIcon.vue";
import GatewayIcon from "@/components/icon/GatewayIcon.vue";
import ReportIcon from "@/components/icon/ReportIcon.vue";
import ViewIcon from "@/components/icon/ViewIcon.vue";
import DictionaryIcon from "@/components/icon/DictionaryIcon.vue";
import TableManagementIcon from "@/components/icon/TableManagementIcon.vue";
import MySQLIcon from "@/components/icon/MySQLIcon.vue";
import OracleIcon from "@/components/icon/OracleIcon.vue";
import MongoDBIcon from "@/components/icon/MongoDBIcon.vue";
import RedisIcon from "@/components/icon/RedisIcon.vue";
import KafkaIcon from "@/components/icon/KafkaIcon.vue";
import NginxIcon from "@/components/icon/NginxIcon.vue";
import HAProxyIcon from "@/components/icon/HAProxyIcon.vue";
import ElasticsearchIcon from "@/components/icon/ElasticsearchIcon.vue";
import HomeIcon from "@/components/icon/HomeIcon.vue";
import API2Icon from "@/components/icon/API2Icon.vue";
import API3Icon from "@/components/icon/API3Icon.vue";
import Data1 from "@/components/icon/Data1.vue";
import Document1 from "@/components/icon/Document1.vue";
import Document2 from "@/components/icon/Document2.vue";
import Download from "@/components/icon/Download.vue";
import Job1 from "@/components/icon/Job1.vue";
import Job2 from "@/components/icon/Job2.vue";
import Job3 from "@/components/icon/Job3.vue";
import Network1 from "@/components/icon/Network1.vue";
import Report2Icon from "@/components/icon/Report2Icon.vue";
import Report3Icon from "@/components/icon/Report3Icon.vue";
import Save from "@/components/icon/Save.vue";
import Server1 from "@/components/icon/Server1.vue";
import Settings2Icon from "@/components/icon/Settings2Icon.vue";
import Settings3Icon from "@/components/icon/Settings3Icon.vue";
import Upload from "@/components/icon/Upload.vue";
import AI1 from "@/components/icon/AI1.vue";
import AI2 from "@/components/icon/AI2.vue";
import alarm from "@/components/icon/alarm.vue";
import centos from "@/components/icon/centos.vue";
import chatgpt from "@/components/icon/chatgpt.vue";
import conffile from "@/components/icon/conffile.vue";
import Console from "@/components/icon/Console.vue";
import consul from "@/components/icon/consul.vue";
import debian from "@/components/icon/debian.vue";
import devops from "@/components/icon/devops.vue";
import docker from "@/components/icon/docker.vue";
import ecloud from "@/components/icon/ecloud.vue";
import ecs from "@/components/icon/ecs.vue";
import exception from "@/components/icon/exception.vue";
import github from "@/components/icon/github.vue";
import gitlab from "@/components/icon/gitlab.vue";
import gray from "@/components/icon/gray.vue";
import guide from "@/components/icon/guide.vue";
import handbook from "@/components/icon/handbook.vue";
import harbor from "@/components/icon/harbor.vue";
import istio from "@/components/icon/istio.vue";
import java from "@/components/icon/java.vue";
import jenkins from "@/components/icon/jenkins.vue";
import jira from "@/components/icon/jira.vue";
import linux from "@/components/icon/linux.vue";
import macos from "@/components/icon/macos.vue";
import monitor from "@/components/icon/monitor.vue";
import MQ from "@/components/icon/MQ.vue";
import nacos from "@/components/icon/nacos.vue";
import order from "@/components/icon/order.vue";
import pool from "@/components/icon/pool.vue";
import product from "@/components/icon/product.vue";
import python from "@/components/icon/python.vue";
import redhat from "@/components/icon/redhat.vue";
import slb from "@/components/icon/slb.vue";
import solaris from "@/components/icon/solaris.vue";
import tomcat from "@/components/icon/tomcat.vue";
import ubuntu from "@/components/icon/ubuntu.vue";
import version from "@/components/icon/version.vue";
import windows from "@/components/icon/windows.vue";
import zhongtai from "@/components/icon/zhongtai.vue";
import zookeeper from "@/components/icon/zookeeper.vue";
import update from "@/components/icon/update.vue";
import encrypt1 from "@/components/icon/encrypt1.vue";
import encrypt2 from "@/components/icon/encrypt2.vue";
import encrypt3 from "@/components/icon/encrypt3.vue";
import encrypt4 from "@/components/icon/encrypt4.vue";
import backup1 from "@/components/icon/backup1.vue";
import backup2 from "@/components/icon/backup2.vue";
import profile1 from "@/components/icon/profile1.vue";
import recovery1 from "@/components/icon/recovery1.vue";
import recovery2 from "@/components/icon/recovery2.vue";
import service1 from "@/components/icon/service1.vue";
import service2 from "@/components/icon/service2.vue";
import timer1 from "@/components/icon/timer1.vue";
import backup3 from "@/components/icon/backup3.vue";
import backup4 from "@/components/icon/backup4.vue";
import recovery3 from "@/components/icon/recovery3.vue";
import profile2 from "@/components/icon/profile2.vue";
import recovery4 from "@/components/icon/recovery4.vue";
import recovery5 from "@/components/icon/recovery5.vue";
import recovery6 from "@/components/icon/recovery6.vue";
import service3 from "@/components/icon/service3.vue";
import service4 from "@/components/icon/service4.vue";
import service5 from "@/components/icon/service5.vue";
let lang = require('element-plus/dist/locale/zh-cn.min.js')
let language = sessionStorage.getItem('h-sm-lang') || 'zh-CN'
if(language=='en-US'){
    lang = require('element-plus/dist/locale/en.min.js')
}else if(language=='ja-JP'){
    lang = require('element-plus/dist/locale/ja.min.js')
}


const app = createApp(App)
app.component('DashboardIcon', DashboardIcon);
app.component('FolderIcon', FolderIcon);
app.component('MenuIcon', MenuIcon);
app.component('LogIcon', LogIcon);
app.component('PermissionIcon', PermissionIcon);
app.component('RoleIcon', RoleIcon);
app.component('SettingsIcon', SettingsIcon);
app.component('UserIcon', UserIcon);
app.component('BaiduIcon', BaiduIcon);
app.component('GoogleIcon', GoogleIcon);
app.component('BingIcon', BingIcon);
app.component('TenantIcon', TenantIcon);
app.component('ClusterIcon', ClusterIcon);
app.component('APIIcon', APIIcon);
app.component('MiddlewareIcon', MiddlewareIcon);
app.component('K8sIcon', K8sIcon);
app.component('CloudIcon', CloudIcon);
app.component('ContainerIcon', ContainerIcon);
app.component('GatewayIcon', GatewayIcon);
app.component('ReportIcon', ReportIcon);
app.component('ViewIcon', ViewIcon);
app.component('DictionaryIcon', DictionaryIcon);
app.component('TableManagementIcon', TableManagementIcon);
app.component('MySQLIcon', MySQLIcon);
app.component('OracleIcon', OracleIcon);
app.component('MongoDBIcon', MongoDBIcon);
app.component('RedisIcon', RedisIcon);
app.component('KafkaIcon', KafkaIcon);
app.component('NginxIcon', NginxIcon);
app.component('HAProxyIcon', HAProxyIcon);
app.component('ElasticsearchIcon', ElasticsearchIcon);
app.component('HomeIcon', HomeIcon)
app.component('API2Icon', API2Icon)
app.component('API3Icon', API3Icon)
app.component('Data1', Data1)
app.component('Document1', Document1)
app.component('Document2', Document2)
app.component('Download', Download)
app.component('Job1', Job1)
app.component('Job2', Job2)
app.component('Job3', Job3)
app.component('Network1', Network1)
app.component('Report2Icon', Report2Icon)
app.component('Report3Icon', Report3Icon)
app.component('Save', Save)
app.component('Server1', Server1)
app.component('Settings2Icon', Settings2Icon)
app.component('Settings3Icon', Settings3Icon)
app.component('Upload', Upload)
app.component('AI1', AI1)
app.component('AI2', AI2)
app.component('alarm', alarm)
app.component('centos', centos)
app.component('chatgpt', chatgpt)
app.component('conffile', conffile)
app.component('Console', Console)
app.component('consul', consul)
app.component('debian', debian)
app.component('devops', devops)
app.component('docker', docker)
app.component('ecloud', ecloud)
app.component('ecs', ecs)
app.component('exception', exception)
app.component('github', github)
app.component('gitlab', gitlab)
app.component('gray', gray)
app.component('guide', guide)
app.component('handbook', handbook)
app.component('harbor', harbor)
app.component('istio', istio)
app.component('java', java)
app.component('jenkins', jenkins)
app.component('jira', jira)
app.component('linux', linux)
app.component('macos', macos)
app.component('monitor', monitor)
app.component('MQ', MQ)
app.component('nacos', nacos)
app.component('order', order)
app.component('pool', pool)
app.component('product', product)
app.component('python', python)
app.component('redhat', redhat)
app.component('slb', slb)
app.component('solaris', solaris)
app.component('tomcat', tomcat)
app.component('ubuntu', ubuntu)
app.component('version', version)
app.component('windows', windows)
app.component('zhongtai', zhongtai)
app.component('zookeeper', zookeeper)
app.component('update', update)
app.component('encrypt1',encrypt1)
app.component('encrypt2',encrypt2)
app.component('encrypt3',encrypt3)
app.component('encrypt4',encrypt4)
app.component('backup1',backup1)
app.component('backup2',backup2)
app.component('profile1',profile1)
app.component('recovery1',recovery1)
app.component('recovery2',recovery2)
app.component('service1',service1)
app.component('service2',service2)
app.component('timer1',timer1)
app.component('backup3',backup3)
app.component('backup4',backup4)
app.component('recovery3',recovery3)
app.component('profile2',profile2)
app.component('recovery4',recovery4)
app.component('recovery5',recovery5)
app.component('recovery6',recovery6)
app.component('service3',service3)
app.component('service4',service4)
app.component('service5',service5)

app.use(ElementPlus, {locale: lang})
app.use(store)
app.use(router)
app.mount('#app')

export default app