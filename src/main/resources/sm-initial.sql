insert into h_roles(app,name,description,created_at) values('h-sm','ADMIN','超级管理员',1735800456);
insert into h_roles(app,name,description,created_at) values('h-sm','MANAGE','维护管理员',1735800456);
insert into h_roles(app,name,description,created_at) values('h-sm','USER','普通用户',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','system','系统管理','/system','-',100,1,'SettingsIcon',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','Role','角色管理','/system/Role','system',0,2,'RoleIcon',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','User','用户管理','/system/User','system',1,2,'UserIcon',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','Menu','菜单管理','/system/Menu','system',2,2,'MenuIcon',1735800456);
insert into h_users(app,username,password,role_name,created_at) values('h-sm','admin','$2a$10$2n7McJFmaxR78hcEU0TELuGGLwpZtqiJDKIolf7SnSETwBye8AYpW','ADMIN',1735800456);
insert into h_users(app,username,password,role_name,created_at) values('h-sm','manage','$2a$10$oHdG4ticIvzG.8BlBjI9HuC3c9RFSD3u.vr8aphNXC4.SckKfIyLq','MANAGE',1735800456);
insert into h_users(app,username,password,role_name,created_at) values('h-sm','user','$2a$10$qRoxm0i0yb1E0MVId.NjL.A/Ac3W4puydYhLeufA5zQ8KJmiAKBIO','USER',1735800456);
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','system');
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','Role');
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','User');
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','Menu');

insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column,app) values('menu,icon','菜单图标',1,'key','value','common');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','DashboardIcon','控制面板');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','FolderIcon','文件夹');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','LogIcon','文件');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MenuIcon','菜单');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','PermissionIcon','权限');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','RoleIcon','角色');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','SettingsIcon','设置');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','UserIcon','用户');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','BaiduIcon','百度');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','GoogleIcon','谷歌');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','BingIcon','必应');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','TenantIcon','租户');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ClusterIcon','集群');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','APIIcon','api');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MiddlewareIcon','中间件');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','K8sIcon','kubernetes');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','CloudIcon','云计算');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ContainerIcon','容器');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','GatewayIcon','网关');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ReportIcon','报表');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ViewIcon','视图');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','DictionaryIcon','字典');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','TableManagementIcon','表管理');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MySQLIcon','mysql');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','OracleIcon','oracle');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MongoDBIcon','mongodb');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','RedisIcon','redis');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','KafkaIcon','kafka');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','NginxIcon','nginx');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','HAProxyIcon','haproxy');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ElasticsearchIcon','elasticsearch');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','HomeIcon','主页');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','API2Icon','api2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','API3Icon','api3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Data1','数据1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Document1','文档1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Document2','文档2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Download','下载');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Job1','工作1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Job2','工作2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Job3','工作3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Network1','网络1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Report2Icon','报表2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Report3Icon','报表3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Save','保存');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Server1','服务器1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Settings2Icon','设置2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Settings3Icon','设置3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Upload','上传');

insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','AI1','人工智能1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','AI2','人工智能2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','alarm','告警');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','centos','CentOS');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','chatgpt','ChatGPT');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','conffile','配置文件');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Console','控制台');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','consul','Consul');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','debian','Debian');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','devops','DevOps');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','docker','Docker');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ecloud','移动云');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ecs','云主机');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','exception','异常');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','github','GitHub');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','gitlab','GitLab');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','gray','灰度');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','guide','引导文档');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','handbook','文档手册');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','harbor','Harbor');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','istio','Istio');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','java','Java');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','jenkins','Jenkins');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','jira','JIRA');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','linux','Linux');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','macos','MacOS');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','monitor','监控');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MQ','MQ消息中间件');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','nacos','Nacos');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','order','订购');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','pool','资源池');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','product','产品');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','python','Python');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','redhat','红帽');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','slb','负载均衡');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','solaris','Solaris');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','tomcat','Tomcat');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ubuntu','Ubuntu');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','version','版本');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','windows','Windows');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','zhongtai','中台');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','zookeeper','Zookeeper');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','update','升级');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt1','加密1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt2','加密2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt3','加密3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt4','网络加密');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup1','备份1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup2','备份2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','profile1','环境1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery1','恢复1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery2','恢复2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service1','服务1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service2','服务2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','timer1','定时1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup3','备份3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup4','备份4');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery3','恢复3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','profile2','环境2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery4','恢复4');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery5','恢复5');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery6','恢复6');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service3','服务3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service4','服务4');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service5','服务5');