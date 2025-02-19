<script setup lang="ts">
import Sidebar from "./Sidebar.vue"
</script>

<template>
  <div class="layout-content layout-wrapper">
    <Sidebar />
    <main class="main">
      <slot></slot>
    </main>
  </div>
</template>

<style scoped>
.layout-content {
  display: flex;
  padding: 3rem 2rem 0;
}
.layout-wrapper {
  background-position: top;
  background-repeat: no-repeat;
  background-size: auto 20rem;
}
</style>
