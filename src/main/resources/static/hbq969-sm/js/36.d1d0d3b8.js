"use strict";(self["webpackChunkstatic"]=self["webpackChunkstatic"]||[]).push([[36],{8036:function(e,l,t){t.r(l),t.d(l,{default:function(){return L}});t(7658);var n=t(3396),a=t(7139),u=t(4870),i=t(2748),s=t(1728),o=t(4834),r=t(3824),d=t(2321);const m=e=>((0,n.dD)("data-v-29e91699"),e=e(),(0,n.Cn)(),e),c={class:"container"},w={style:{display:"flex"}},f=m((()=>(0,n._)("span",{style:{"font-size":"1.2em","margin-left":"10px"}},"H-SM",-1))),h={style:{display:"flex","align-items":"center"}},p={style:{"margin-right":"10px","margin-left":"0px",padding:"0","font-size":"0.6em"}},g={class:"menu-header"},v={style:{width:"100%"}},y={style:{height:"39px",display:"flex","align-items":"center","justify-content":"space-between",padding:"0 20px","background-color":"#fff","border-bottom":"1px solid #e4e7ed"}},_={style:{height:"calc(100vh - 100px)"}};var b=(0,n.aZ)({__name:"left",setup(e){const l=(0,u.qj)({currentPage:"主页",user:{},adminMenus:[],menus:[]}),m=(0,u.qj)({}),b=()=>{(0,s.Z)({url:"/system/logout",method:"post"}).then((e=>{"OK"==e.data.state?r.Z.push({path:"/login"}):(0,o.WI)(e.data.errorMessage,"warning")})).catch((e=>{(0,o.WI)("请求异常","error")}))};(0,n.bv)((()=>{(0,s.Z)({url:"/system/user",method:"get"}).then((e=>{if("OK"==e.data.state){let t=e.data.body.user;l.user.userName=t.userName,l.user.isAdmin=t.admin,l.menus=t.menus,l.adminMenus=e.data.body.allMenus,l.adminMenus&&l.adminMenus.length>0&&l.adminMenus.forEach((e=>{m[e.url]=e.menuDesc,e.menus&&e.menus.length>0&&e.menus.forEach((e=>{m[e.url]=e.menuDesc}))}))}})).catch((e=>{(0,o.WI)("请求异常","error")}))}));const x=e=>(0,n.up)(e),W=(0,u.iH)(!1),L=(0,u.iH)("1"),k=(0,u.iH)(""),j=(0,u.iH)([]),D=()=>{W.value=!W.value},H=e=>{let a;if(e.startsWith("http:")||e.startsWith("https:"))window.open(e,"_blank");else{L.value=e;let i=m[e];l.currentPage=i;const s=j.value.find((l=>l.name===e));if(!s){if(e.startsWith("inner:")){const l=e.substring(6);console.log("iframeUrl: ",l),a=(0,n.aZ)({setup(){const e=(0,u.iH)(null),t=(0,u.iH)(!0),n=()=>{if(e.value&&e.value.contentWindow?.document.body){const l=e.value.contentWindow.document.body.scrollHeight;e.value.style.height=`${l}px`,console.log("iframe 高度已调整:",l);const n=new MutationObserver((()=>{const l=e.value.contentWindow.document.body.scrollHeight;e.value.style.height=`${l}px`,console.log("iframe 高度动态调整:",l)}));n.observe(e.value.contentWindow.document.body,{childList:!0,subtree:!0}),t.value=!1}};return{iframeUrl:l,onIframeLoad:n,iframe:e,isLoading:t}},render(){return(0,n.h)("div",{style:{width:"100%",height:"100%",position:"relative"}},[this.isLoading?(0,n.h)("div",{style:{position:"absolute",top:"50%",left:"50%",transform:"translate(-50%, -50%)"}},"加载中..."):null,(0,n.h)("iframe",{src:this.iframeUrl,style:{width:"100%",height:"100%",border:"none"},onLoad:this.onIframeLoad,ref:"iframe"})])}})}else a=(0,n.RC)((()=>t(1480)(`./views${e}.vue`).then((e=>(console.log("组件加载成功:",e.default),e))).catch((e=>{throw console.error("组件加载失败:",e),e}))));j.value.push({name:e,label:i,component:(0,u.Xl)(a)})}k.value=e}},z=e=>{const l=j.value.findIndex((l=>l.name===e));j.value.splice(l,1),e===k.value&&(k.value=j.value[0]?.name||"")},U=(e,l)=>{let t;return function(...n){const a=self;t&&clearTimeout(t),t=setTimeout((()=>{e.apply(a,n)}),l)}},M=window.ResizeObserver;return window.ResizeObserver=class extends M{constructor(e){e=U(e,20),super(e)}},(e,t)=>{const s=(0,n.up)("el-icon"),o=(0,n.up)("el-header"),r=(0,n.up)("el-menu-item"),m=(0,n.up)("el-sub-menu"),U=(0,n.up)("el-menu"),M=(0,n.up)("el-aside"),N=(0,n.up)("el-breadcrumb-item"),I=(0,n.up)("el-breadcrumb"),K=(0,n.up)("el-tab-pane"),Y=(0,n.up)("el-tabs"),Z=(0,n.up)("el-container");return(0,n.wg)(),(0,n.iD)("div",c,[(0,n.Wm)(Z,{style:{height:"100vh",overflow:"hidden","border-radius":"8px"}},{default:(0,n.w5)((()=>[(0,n.Wm)(o,{class:"header"},{default:(0,n.w5)((()=>[(0,n._)("div",w,[(0,n.Wm)(s,{size:"24"},{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(x("HomeIcon"))))])),_:1}),f]),(0,n._)("div",h,[(0,n.Wm)(d.Z),(0,n._)("span",p,", "+(0,a.zw)(l.user.userName),1),(0,n.Wm)(s,{onClick:b,style:{cursor:"pointer",color:"red"},size:"20"},{default:(0,n.w5)((()=>[(0,n.Wm)((0,u.SU)(i.U3h))])),_:1})])])),_:1}),(0,n.Wm)(Z,null,{default:(0,n.w5)((()=>[(0,n.Wm)(M,{width:W.value?"64px":"200px",style:{overflow:"hidden",transition:"width 0.3s ease","margin-top":"60px",height:"calc(100vh - 60px)"}},{default:(0,n.w5)((()=>[(0,n._)("div",g,[(0,n.Wm)(s,{onClick:D,style:{cursor:"pointer",color:"#fff"}},{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(W.value?(0,u.SU)(i.M0b):(0,u.SU)(i.qFw))))])),_:1})]),(0,n.Wm)(U,{"default-active":L.value,collapse:W.value,onSelect:H,mode:"vertical",ellipsis:!1,"popper-effect":"dark",style:{height:"calc(100vh - 100px)","overflow-y":"auto","overflow-x":"hidden"}},{default:(0,n.w5)((()=>[l.user.isAdmin?((0,n.wg)(!0),(0,n.iD)(n.HY,{key:0},(0,n.Ko)(l.adminMenus,(e=>((0,n.wg)(),(0,n.iD)(n.HY,null,[e.menus&&e.menus.length>0?((0,n.wg)(),(0,n.j4)(m,{key:0,index:e.url},{title:(0,n.w5)((()=>[(0,n.Wm)(s,null,{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(x(e.iconName))))])),_:2},1024),(0,n._)("span",null,(0,a.zw)(e.menuDesc),1)])),default:(0,n.w5)((()=>[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(e.menus,(e=>((0,n.wg)(),(0,n.j4)(r,{index:e.url},{title:(0,n.w5)((()=>[(0,n.Uk)((0,a.zw)(e.menuDesc),1)])),default:(0,n.w5)((()=>[(0,n.Wm)(s,null,{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(x(e.iconName))))])),_:2},1024)])),_:2},1032,["index"])))),256))])),_:2},1032,["index"])):((0,n.wg)(),(0,n.j4)(r,{key:1,index:e.url},{title:(0,n.w5)((()=>[(0,n.Uk)((0,a.zw)(e.menuDesc),1)])),default:(0,n.w5)((()=>[(0,n.Wm)(s,null,{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(x(e.iconName))))])),_:2},1024)])),_:2},1032,["index"]))],64)))),256)):((0,n.wg)(!0),(0,n.iD)(n.HY,{key:1},(0,n.Ko)(l.menus,(e=>((0,n.wg)(),(0,n.iD)(n.HY,null,[e.menus&&e.menus.length>0?((0,n.wg)(),(0,n.j4)(m,{key:0,index:e.url},{title:(0,n.w5)((()=>[(0,n.Wm)(s,null,{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(x(e.iconName))))])),_:2},1024),(0,n._)("span",null,(0,a.zw)(e.menuDesc),1)])),default:(0,n.w5)((()=>[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(e.menus,(e=>((0,n.wg)(),(0,n.j4)(r,{index:e.url},{title:(0,n.w5)((()=>[(0,n.Uk)((0,a.zw)(e.menuDesc),1)])),default:(0,n.w5)((()=>[(0,n.Wm)(s,null,{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(x(e.iconName))))])),_:2},1024)])),_:2},1032,["index"])))),256))])),_:2},1032,["index"])):((0,n.wg)(),(0,n.j4)(r,{key:1,index:e.url},{title:(0,n.w5)((()=>[(0,n.Uk)((0,a.zw)(e.menuDesc),1)])),default:(0,n.w5)((()=>[(0,n.Wm)(s,null,{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(x(e.iconName))))])),_:2},1024)])),_:2},1032,["index"]))],64)))),256))])),_:1},8,["default-active","collapse"])])),_:1},8,["width"]),(0,n.Wm)(Z,{style:{height:"calc(100vh - 60px)",overflow:"hidden","background-color":"#f5f7fa","margin-top":"60px"}},{default:(0,n.w5)((()=>[(0,n._)("div",v,[(0,n._)("div",y,[(0,n.Wm)(I,{separator:"/"},{default:(0,n.w5)((()=>[(0,n.Wm)(N,null,{default:(0,n.w5)((()=>[(0,n.Uk)("首页")])),_:1}),(0,n.Wm)(N,null,{default:(0,n.w5)((()=>[(0,n.Uk)((0,a.zw)(l.currentPage),1)])),_:1})])),_:1})]),(0,n._)("div",_,[(0,n.Wm)(Y,{modelValue:k.value,"onUpdate:modelValue":t[0]||(t[0]=e=>k.value=e),type:"card",closable:"",onTabRemove:z,style:{height:"100%"}},{default:(0,n.w5)((()=>[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(j.value,(e=>((0,n.wg)(),(0,n.j4)(K,{key:e.name,label:e.label,name:e.name,style:{height:"calc(100vh - 156px)","overflow-y":"auto"}},{default:(0,n.w5)((()=>[((0,n.wg)(),(0,n.j4)((0,n.LL)(e.component)))])),_:2},1032,["label","name"])))),128))])),_:1},8,["modelValue"])])])])),_:1})])),_:1})])),_:1})])}}}),x=t(89);const W=(0,x.Z)(b,[["__scopeId","data-v-29e91699"]]);var L=W}}]);
//# sourceMappingURL=36.d1d0d3b8.js.map