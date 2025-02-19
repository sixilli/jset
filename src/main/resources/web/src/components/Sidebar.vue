<script setup>
import { ref } from "vue";
import { useRouter } from 'vue-router';

const router = useRouter();

const items = ref([
  {
    label: 'Dashboard',
    icon: 'pi pi-server',
    route: '/'
  },
  {
    label: 'Clips',
    icon: 'pi pi-video',
    route: '/clips'
  },
  {
    label: 'Remote Sites',
    icon: 'pi pi-globe',
    route: '/sites'
  }
]);
</script>

<template>
  <aside class="layout-sidebar">
    <nav>
      <div class="card flex justify-center">
        <Menu :model="items">
          <template #item="{ item, props }">
            <router-link v-if="item.route" v-slot="{ href, navigate }" :to="item.route" custom>
              <a v-ripple :href="href" v-bind="props.action" @click="navigate">
                <span :class="item.icon" />
                <span class="ml-2">{{ item.label }}</span>
              </a>
            </router-link>
            <a v-else v-ripple :href="item.url" :target="item.target" v-bind="props.action">
              <span :class="item.icon" />
              <span class="ml-2">{{ item.label }}</span>
            </a>
          </template>
        </Menu>
      </div>
    </nav>
  </aside>
</template>

<style>
.layout-sidebar {
  display: flex;
  flex: 0 0 250px;
  flex-direction: column;
  height: calc(100vh - 9rem);
  inset-inline-start: 0;
  margin-inline-end: 4rem;
  overflow: auto;
  padding: 0;
  position: sticky;
  top: 6rem;
  transition: transform .4s cubic-bezier(.05,.74,.2,.99), opacity .3s;
  -webkit-user-select: none;
  -moz-user-select: none;
  user-select: none;
}

.layout-sidebar .layout-menu {
  list-style: none;
  margin: 0;
  padding: 0;
}
</style>
