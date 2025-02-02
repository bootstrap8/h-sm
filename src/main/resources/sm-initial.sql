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