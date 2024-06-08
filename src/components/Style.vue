<template>
  <div class="styles">
    <ul>
      <li class="style-item" :class="{'selected':item===active}" v-for="(item,index) in styles" :key="index">
        <p @click="searchStyle(item)">{{ item }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'style-select',
  data() {
    return {
      styles: ['全部','流行','说唱','摇滚','金属','民谣','电子','古典','ACG','其他'],
      active: '',
    }
  },
  methods: {
    searchStyle(style) {
      if (style === '全部') {
        this.$router.push({path: ''})
      }
      else {
        this.$router.push({path: `?style=${style}`});
      }
      this.active = style;
      this.$forceUpdate()
    },
  }
}
</script>

<style scoped lang = "scss">
@import "../assets/css/styles";
</style>
