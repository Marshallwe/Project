
<template>
<div class="box" style="position: relative;border-radius: 10px">
  <div>
    <span > {{address.linkUser}}</span>
  </div>
  <hr style="margin: 10px 0;font-size: 1px">
  <span> {{address.linkPhone}}</span>
  <span> {{address.linkAddress}}</span>
  <div style="margin-top: 5px">
    <a @click="$emit('edit')" style="color: #710a0a;cursor: pointer;position:absolute;bottom: 10px">Modify</a>
    <a @click="$emit('delete')" style="color: #710a0a;cursor: pointer;float: right;position:absolute;bottom: 10px;right: 10px">Delete</a>
  </div>
</div>
</template>

<script>
export default {
  name: "AddressBox",
  props:{
    address: Object,
  },
}
</script>

<style scoped>
.box{
  padding: 10px;
  width: 300px;
  height: 120px;
  display: inline-block;
  border: #333333 1px solid;
}

</style>
