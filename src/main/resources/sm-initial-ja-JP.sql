delete from h_roles where app='h-sm' and name in ('ADMIN','MANAGE','USER');
delete from h_menus where app='h-sm' and name in ('system','Role','User','Menu','AI','SiliconFlow');
delete from h_dict_base where dict_name='menu,icon' and app='common';
delete from h_dict_pairs where dict_name='menu,icon' and app='common';

insert into h_roles(app,name,description,created_at) values('h-sm','ADMIN','スーパー管理者',1735800456);
insert into h_roles(app,name,description,created_at) values('h-sm','MANAGE','メンテナンス管理者',1735800456);
insert into h_roles(app,name,description,created_at) values('h-sm','USER','通常ユーザー',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','system','システム管理','/system','-',100,1,'SettingsIcon',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','Role','役割管理','/system/Role','system',0,2,'RoleIcon',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','User','ユーザー管理','/system/User','system',1,2,'UserIcon',1735800456);
insert into h_menus(app,name,menu_desc,url,parent_key,order_index,menu_level,icon_name,created_at) values('h-sm','Menu','メニュー管理','/system/Menu','system',2,2,'MenuIcon',1735800456);
insert into h_users(app,username,password,role_name,created_at) values('h-sm','admin','$2a$10$2n7McJFmaxR78hcEU0TELuGGLwpZtqiJDKIolf7SnSETwBye8AYpW','ADMIN',1735800456);
insert into h_users(app,username,password,role_name,created_at) values('h-sm','manage','$2a$10$oHdG4ticIvzG.8BlBjI9HuC3c9RFSD3u.vr8aphNXC4.SckKfIyLq','MANAGE',1735800456);
insert into h_users(app,username,password,role_name,created_at) values('h-sm','user','$2a$10$qRoxm0i0yb1E0MVId.NjL.A/Ac3W4puydYhLeufA5zQ8KJmiAKBIO','USER',1735800456);
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','system');
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','Role');
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','User');
insert into h_role_menus(app,role_name,menu_name) values('h-sm','MANAGE','Menu');

insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column,app) values('i18n,language1','言語略語マッピング',1,'key','value','common');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('i18n,language1','zh','zh-CN');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('i18n,language1','en','en-US');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('i18n,language1','ja','ja-JP');

insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column,app) values('menu,icon','メニューアイコン',1,'key','value','common');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','DashboardIcon','Dashboard');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','FolderIcon','Folder');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','LogIcon','File');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MenuIcon','Menu');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','PermissionIcon','Permission');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','RoleIcon','Role');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','SettingsIcon','Settings');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','UserIcon','User');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','BaiduIcon','Baidu');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','GoogleIcon','Google');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','BingIcon','Bing');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','TenantIcon','Tenant');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ClusterIcon','Cluster');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','APIIcon','api');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MiddlewareIcon','Middleware');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','K8sIcon','kubernetes');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','CloudIcon','Cloud');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ContainerIcon','Container');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','GatewayIcon','Gateway');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ReportIcon','Report');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ViewIcon','View');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','DictionaryIcon','Dictionary');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','TableManagementIcon','TableManagement');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MySQLIcon','mysql');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','OracleIcon','oracle');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MongoDBIcon','mongodb');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','RedisIcon','redis');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','KafkaIcon','kafka');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','NginxIcon','nginx');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','HAProxyIcon','haproxy');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ElasticsearchIcon','elasticsearch');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','HomeIcon','Home');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','API2Icon','api2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','API3Icon','api3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Data1','Data1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Document1','Document1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Document2','Document2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Download','Download');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Job1','Job1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Job2','Job2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Job3','Job3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Network1','Network1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Report2Icon','Report2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Report3Icon','Report3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Save','Save');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Server1','Server1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Settings2Icon','Settings2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Settings3Icon','Settings3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Upload','Upload');

insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','AI1','AI1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','AI2','AI2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','alarm','alarm');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','centos','CentOS');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','chatgpt','ChatGPT');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','conffile','Config File');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','Console','Console');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','consul','Consul');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','debian','Debian');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','devops','DevOps');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','docker','Docker');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ecloud','ECloud');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ecs','ecs');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','exception','exception');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','github','GitHub');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','gitlab','GitLab');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','gray','gray');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','guide','guide');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','handbook','handbook');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','harbor','Harbor');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','istio','Istio');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','java','Java');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','jenkins','Jenkins');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','jira','JIRA');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','linux','Linux');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','macos','MacOS');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','monitor','monitor');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','MQ','MQ');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','nacos','Nacos');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','order','order');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','pool','pool');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','product','product');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','python','Python');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','redhat','redhat');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','slb','slb');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','solaris','Solaris');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','tomcat','Tomcat');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','ubuntu','Ubuntu');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','version','version');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','windows','Windows');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','zhongtai','zhongtai');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','zookeeper','Zookeeper');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','update','update');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt1','encrypt1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt2','encrypt2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt3','encrypt3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','encrypt4','encrypt4');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup1','backup1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup2','backup2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','profile1','profile1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery1','recovery1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery2','recovery2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service1','service1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service2','service2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','timer1','timer1');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup3','backup3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','backup4','backup4');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery3','recovery3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','profile2','profile2');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery4','recovery4');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery5','recovery5');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','recovery6','recovery6');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service3','service3');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service4','service4');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('menu,icon','service5','service5');

delete from h_dict_base where dict_name='lang,ja-JP';
delete from h_dict_pairs where dict_name='lang,ja-JP';

insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column,app) values('lang,ja-JP','语言,日语',1,'key','value','common');

insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','axiosRequestErr','例外を要求する');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','axiosRequestCallKey','電話');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','formValidateNotNull','空にすることはできません');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginAnswerCalc','計算してください');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginAnswerErr','間違った回答です。再度入力してください');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginTitle','アカウントにログイン');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginSpan','当社のアプリ メンテナンス プラットフォームの使用を開始するには、アカウントを作成して、ぜひご利用ください。');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginFormPlaceholderUserName','口座番号を入力してください');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginFormPlaceholderPassword','パスワードを入力してください');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginFormBtnLogin','ログイン');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginFormBtnSpan','ログイン');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginModalTitle','セキュリティ保護');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginModalP','この操作には検証コードが必要であることが検出されました');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginModalPlaceholderCode','認証コードを入力してください');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginModalBtnConfirm','もちろん');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('lang,ja-JP','loginModalBtnCancel','キャンセル');