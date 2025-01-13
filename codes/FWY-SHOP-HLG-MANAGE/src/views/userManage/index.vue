<!--
 * @Author: your name
 * @Date: 2022-03-11 21:02:43
 * @LastEditTime: 2022-03-26 13:13:03
 * @LastEditors: your name
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wineManage\src\views\userManage\index.vue
-->
<template>
  <keep-alive include="goods/list">
               <router-view></router-view>
            </keep-alive>
</template>