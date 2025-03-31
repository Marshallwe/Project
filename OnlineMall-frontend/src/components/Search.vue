
<template>
  <el-row>
    <el-col :span="24" style="text-align: center;">
      <div class="search" style="display: inline-block">
        <div style="height: 60px;margin-left: 30px">
          <input @keydown.enter="$emit('search',searchText)" type="text" placeholder="Please enter the item you want to buy" v-model="searchText">
          <button @click="$emit('search',searchText)"></button>
        </div>
      </div>
    </el-col>
  </el-row>
</template>
<script>
export default {
  name: "Search",
  data(){
    return{
      searchText:'',
    }
  }

}
</script>
