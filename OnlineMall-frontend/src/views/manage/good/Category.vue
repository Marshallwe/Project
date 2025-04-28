
<template>
  <div>

    <div style="width: 100%; margin: 0px auto">

      <el-button @click="addDialogFormVisible = true" round type="success" style="margin: 10px;width: 150px">
        <i class="el-icon-circle-plus"style="padding-right: 6px"></i>Add
      </el-button>

      <el-table :data="icons" stripe border fixed>
        <el-table-column type="expand" label="Subordinate classification" width="100px">

          <template slot-scope="scope">

            <el-table
              :data="scope.row.categories"
              :header-cell-style="{ background: '#cbefea', color: 'black' }"
            >

              <el-table-column label="id of category" prop="id"></el-table-column>
              <el-table-column label="Name of category" prop="name"></el-table-column>

              <el-table-column label="Operation">
                <template slot-scope="scope">
                  <el-button
                    type="primary"
                    size="mini"
                    @click="handleEditCategory(scope.row)"
                    >modify</el-button
                  >

                  <el-popconfirm
                    @confirm="deleteCategory(scope.row)"
                    title="Sure to delete?"
                  >
                    <el-button
                      type="danger"
                      size="mini"
                      slot="reference"
                      >Delete</el-button
                    >
                  </el-popconfirm>
                </template>
              </el-table-column>

            </el-table>
          </template>
        </el-table-column>

        <el-table-column label="id" prop="id" width="60px"></el-table-column>
        <el-table-column label="icon">
          <template slot-scope="scope">
            <i class="iconfont" v-html="scope.row.value"></i>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="Operation" width="200">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              circle
              @click="handleEditIcon(scope.row)"
            ></el-button>
            <el-button
              type="success"
              icon="el-icon-plus"
              circle
              @click="handleAddCategory(scope.row)"
            ></el-button>

            <el-popconfirm
              @confirm="deleteIcon(scope.row.id)"
              title="Sure to delete?"
            >
              <el-button
                type="danger"
                icon="el-icon-delete"
                circle
                slot="reference"
                style="margin-left: 10px"
              ></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>

      </el-table>

    </div>

    <el-dialog title="Modify superior classification" :visible.sync="dialogFormVisible">

      <el-form :model="icon">
        <el-form-item label="icon" label-width="100px">
          <i class="iconfont" v-html="icon.value"></i>
        </el-form-item>

        <el-form-item label="Change the icon" label-width="100px">
          <el-select placeholder="Please select the icon" v-model="icon.value">
            <el-option v-for="item in iconStore" :value="item" :key="item">
              <i class="iconfont" v-html="item"></i>
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="editIcon">Confirm</el-button>
      </div>

    </el-dialog>

    <el-dialog title="Added superior classification" :visible.sync="addDialogFormVisible">

     <el-form :model="addIcon">
        <el-form-item label="icon" label-width="100px">
          <i class="iconfont" v-html="addIcon.value"></i>
        </el-form-item>

        <el-form-item label="Change the icon" label-width="100px">
          <el-select placeholder="Please select the icon" v-model="addIcon.value">
            <el-option v-for="item in iconStore" :value="item" :key="item">
              <i class="iconfont" v-html="item"></i>
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveIcon">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import icons from "@/utils/icons";
export default {
  name: "Category",

  data() {
    return {
      options: [],
      searchText: "",
      user: {},
      iconStore: icons.iconStore,
      icons: [],
      icon: {},
      addIcon: {},
      pageNum: 1,
      pageSize: 5,
      entity: {},
      total: 0,
      dialogFormVisible: false,
      addDialogFormVisible: false,
    };
  },
  created() {
    this.user = localStorage.getItem("user")
      ? JSON.parse(sessionStorage.getItem("user"))
      : {};
    this.load();
  },
  methods: {
    load() {
      this.request.get("/api/icon").then((res) => {
        this.icons = res.data;
      });
    },
    handleEditCategory(category) {
      this.$prompt("Please enter the modified name", "Tip", {
        confirmButtonText: "Confirm",
        cancelButtonText: "Cancel",
      }).then(({ value }) => {
        category.name = value;
        this.request.post("/api/category", category).then((res) => {
          if (res.code === "200") {
            this.$message.success("Modification is successful");
          } else {
            this.$message.error("Modification failed");
          }
        });
      });
    },
    handleAddCategory(icon) {
      this.$prompt("Please enter the new sub-category name", "Tip", {
        confirmButtonText: "Confirm",
        cancelButtonText: "Cancel",
      }).then(({ value }) => {
        this.request
          .post("/api/category/add", { name: value, iconId: icon.id })
          .then((res) => {
            if (res.code === "200") {
              this.$message.success("Add success");
              this.load();
            } else {
              this.$message.error("Add failed");
            }
          });
      });
    },
    handleEditIcon(icon) {
      this.icon = JSON.parse(JSON.stringify(icon));
      this.dialogFormVisible = true;
    },
    editIcon() {
      delete this.icon.categories;
      this.request.post("/api/icon", this.icon).then((res) => {
        if (res.code === "200") {
          this.$message.success("Modification is successful");
          this.dialogFormVisible = false;
        } else {
          this.$message.error("Modification failed");
        }
      });
    },
    saveIcon() {
      if (this.addIcon.value == undefined) {
        this.$message.error("Please select the superior classification icon");
        return;
      }
      this.request.post("/api/icon", this.addIcon).then((res) => {
        if (res.code === "200") {
          this.$message.success("Add success");
          this.addDialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Add failed");
        }
      });
    },
    deleteIcon(iconId) {
      this.request.get("/api/icon/delete?id=" + iconId).then((res) => {
        if (res.code == "200") {
          this.$message.success("Successful deletion");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    deleteCategory(category) {
      this.request.get("/api/category/delete?id=" + category.id).then((res) => {
        if (res.code == "200") {
          this.$message.success("Successful deletion");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
  },
};
</script>

<style scoped>
@import "../../../resource/css/icon.css";
</style>
