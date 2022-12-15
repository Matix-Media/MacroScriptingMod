"use strict";(self.webpackChunkmacroscriptingmod_docs=self.webpackChunkmacroscriptingmod_docs||[]).push([[226],{3905:(e,t,a)=>{a.d(t,{Zo:()=>u,kt:()=>g});var n=a(7294);function r(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}function l(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,n)}return a}function i(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?l(Object(a),!0).forEach((function(t){r(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):l(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}function o(e,t){if(null==e)return{};var a,n,r=function(e,t){if(null==e)return{};var a,n,r={},l=Object.keys(e);for(n=0;n<l.length;n++)a=l[n],t.indexOf(a)>=0||(r[a]=e[a]);return r}(e,t);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(e);for(n=0;n<l.length;n++)a=l[n],t.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(e,a)&&(r[a]=e[a])}return r}var p=n.createContext({}),c=function(e){var t=n.useContext(p),a=t;return e&&(a="function"==typeof e?e(t):i(i({},t),e)),a},u=function(e){var t=c(e.components);return n.createElement(p.Provider,{value:t},e.children)},m="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},s=n.forwardRef((function(e,t){var a=e.components,r=e.mdxType,l=e.originalType,p=e.parentName,u=o(e,["components","mdxType","originalType","parentName"]),m=c(a),s=r,g=m["".concat(p,".").concat(s)]||m[s]||d[s]||l;return a?n.createElement(g,i(i({ref:t},u),{},{components:a})):n.createElement(g,i({ref:t},u))}));function g(e,t){var a=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var l=a.length,i=new Array(l);i[0]=s;var o={};for(var p in t)hasOwnProperty.call(t,p)&&(o[p]=t[p]);o.originalType=e,o[m]="string"==typeof e?e:r,i[1]=o;for(var c=2;c<l;c++)i[c]=a[c];return n.createElement.apply(null,i)}return n.createElement.apply(null,a)}s.displayName="MDXCreateElement"},4843:(e,t,a)=>{a.r(t),a.d(t,{assets:()=>p,contentTitle:()=>i,default:()=>m,frontMatter:()=>l,metadata:()=>o,toc:()=>c});var n=a(7462),r=(a(7294),a(3905));const l={tags:["Lib"]},i="Player",o={unversionedId:"libraries/player",id:"libraries/player",title:"Player",description:'require("player")',source:"@site/docs/libraries/player.md",sourceDirName:"libraries",slug:"/libraries/player",permalink:"/MacroScriptingMod/docs/libraries/player",draft:!1,editUrl:"https://github.com/Matix-Media/MacroScriptingMod/tree/docs/docs/libraries/player.md",tags:[{label:"Lib",permalink:"/MacroScriptingMod/docs/tags/lib"}],version:"current",frontMatter:{tags:["Lib"]},sidebar:"tutorialSidebar",previous:{title:"Libraries",permalink:"/MacroScriptingMod/docs/libraries/"},next:{title:"Script",permalink:"/MacroScriptingMod/docs/libraries/script"}},p={},c=[{value:"Functions",id:"functions",level:2},{value:"<code>get_info()</code>",id:"get_info",level:3},{value:"<code>get_location()</code>",id:"get_location",level:3},{value:"<code>get_gamemode()</code>",id:"get_gamemode",level:3},{value:"<code>get_health()</code>",id:"get_health",level:3},{value:"<code>get_hunger()</code>",id:"get_hunger",level:3},{value:"<code>get_xp_level()</code>",id:"get_xp_level",level:3},{value:"<code>get_xp()</code>",id:"get_xp",level:3},{value:"<code>get_total_xp()</code>",id:"get_total_xp",level:3}],u={toc:c};function m(e){let{components:t,...a}=e;return(0,r.kt)("wrapper",(0,n.Z)({},u,a,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h1",{id:"player"},"Player"),(0,r.kt)("p",null,(0,r.kt)("inlineCode",{parentName:"p"},'require("player")')),(0,r.kt)("p",null,"The player library contains all kinds of functions related to the player."),(0,r.kt)("h2",{id:"functions"},"Functions"),(0,r.kt)("h3",{id:"get_info"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_info()")),(0,r.kt)("p",null,"Returns general information about the current player."),(0,r.kt)("p",null,"The returned object contains the players ",(0,r.kt)("inlineCode",{parentName:"p"},"UUID")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"name"),"."),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},"local player_info = player.get_info()\n\nprint(player_info.name)\nprint(player_info.UUID)\n")),(0,r.kt)("h3",{id:"get_location"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_location()")),(0,r.kt)("p",null,"Returns general the location of the current player."),(0,r.kt)("p",null,"The returned object contains the players exact ",(0,r.kt)("inlineCode",{parentName:"p"},"x"),", ",(0,r.kt)("inlineCode",{parentName:"p"},"y")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"z")," position."),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},'local player_location = player.get_location()\n\nprint("Player is at: " .. player_location.x .. "/" .. player_location.y .. "/" .. player_location.z)\n')),(0,r.kt)("h3",{id:"get_gamemode"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_gamemode()")),(0,r.kt)("p",null,"Returns the current player's gamemode."),(0,r.kt)("p",null,"The returned object returns the gamemode's ",(0,r.kt)("inlineCode",{parentName:"p"},"id")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"name"),"."),(0,r.kt)("p",null,(0,r.kt)("inlineCode",{parentName:"p"},"id")," and their correlating ",(0,r.kt)("inlineCode",{parentName:"p"},"name")," can be one of the following values",(0,r.kt)("br",{parentName:"p"}),"\n","-",(0,r.kt)("inlineCode",{parentName:"p"},"0"),": ",(0,r.kt)("inlineCode",{parentName:"p"},"survival"),(0,r.kt)("br",{parentName:"p"}),"\n","-",(0,r.kt)("inlineCode",{parentName:"p"},"1"),": ",(0,r.kt)("inlineCode",{parentName:"p"},"creative"),(0,r.kt)("br",{parentName:"p"}),"\n","-",(0,r.kt)("inlineCode",{parentName:"p"},"2"),": ",(0,r.kt)("inlineCode",{parentName:"p"},"adventure"),(0,r.kt)("br",{parentName:"p"}),"\n","-",(0,r.kt)("inlineCode",{parentName:"p"},"3"),": ",(0,r.kt)("inlineCode",{parentName:"p"},"spectator")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},'local player_gamemode = player.get_gamemode()\n\nprint("Player is in gamemode: " .. player_gamemode.name .. " with id: " .. player_gamemode.id)\n')),(0,r.kt)("h3",{id:"get_health"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_health()")),(0,r.kt)("p",null,"Returns the current player's health."),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},'print("Player is at: " .. player.get_health() .. " health.")\n')),(0,r.kt)("h3",{id:"get_hunger"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_hunger()")),(0,r.kt)("p",null,"Returns the current player's hunger."),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},'print("Player is at: " .. player.get_hunger() .. " hunger.")\n')),(0,r.kt)("h3",{id:"get_xp_level"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_xp_level()")),(0,r.kt)("p",null,"Returns the current player's experience level."),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},'print("Player is at level: " .. player.get_xp_level())\n')),(0,r.kt)("h3",{id:"get_xp"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_xp()")),(0,r.kt)("p",null,"Returns the current player's experience.",(0,r.kt)("br",{parentName:"p"}),"\n","Resets after each experience level reached."),(0,r.kt)("admonition",{type:"info"},(0,r.kt)("p",{parentName:"admonition"},"This is not the number you see above your hotbar.")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},'print("Player is at level: " .. player.get_xp_level())\n')),(0,r.kt)("h3",{id:"get_total_xp"},(0,r.kt)("inlineCode",{parentName:"h3"},"get_total_xp()")),(0,r.kt)("p",null,"Returns the current player's total experience."),(0,r.kt)("admonition",{type:"info"},(0,r.kt)("p",{parentName:"admonition"},"This is not the number you see above your hotbar.")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-lua",metastring:'title="example.lua"',title:'"example.lua"'},'print("Player is at level: " .. player.get_xp_level())\n')))}m.isMDXComponent=!0}}]);