<template>
    <div class="table">
        <div class="crumbs" style="padding: 20px 0 5px 20px">
            <i class="iconfont icon-r-list" style="font-size: 24px">
                收藏信息</i
            >
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input
                    v-model="select_word"
                    placeholder="筛选关键词"
                    class="handle-input"
                ></el-input>
                <br /><br />
                <el-button type="danger" style="font-size: 18px" @click="delAll"
                    >
                    批量删除</el-button
                >
            </div>
        </div>
        <el-table
            ref="multipleTable"
            border
            style="width: 100%"
            height="680px"
            :data="tableData"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column
                prop="name"
                label="歌手-歌名"
                align="center"
            ></el-table-column>
            <el-table-column label="操作" width="150" align="center">
                <template slot-scope="scope">
                    <el-button
                        style="font-size: 18px"
                        type="danger"
                        @click="handleDelete(scope.row)"
                        >
                        删除</el-button
                    >
                </template>
            </el-table-column>
        </el-table>

        <el-dialog
            title="删除歌曲"
            :visible.sync="delVisible"
            width="300px"
            center
        >
            <div align="center">删除不可恢复，是否确定删除？</div>
            <span slot="footer">
                <el-button size="mini" @click="delVisible = false"
                    > 取消</el-button
                >
                <el-button size="mini" @click="deleteRow" type="primary"> 确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { mixin } from "../mixins/index";
import {
    songOfSongId,
    getCollectOfUserId,
    deleteCollection,
} from "../api/index";

export default {
    mixins: [mixin],
    props: ["id"],
    data() {
        return {
            delVisible: false, //删除弹窗是否显示
            tableData: [],
            tempData: [],
            select_word: "",
            idx: -1, //当前选择项
            multipleSelection: [], //哪些项已经打勾
        };
    },
    watch: {
        //搜索框里面的内容发生变化的时候，搜索结果table列表的内容跟着它的内容发生变化
        select_word: function () {
            if (this.select_word == "") {
                this.tableData = this.tempData;
            } else {
                this.tableData = [];
                for (let item of this.tempData) {
                    if (item.name.includes(this.select_word)) {
                        this.tableData.push(item);
                    }
                }
            }
        },
    },
    created() {
        this.getData();
    },
    methods: {
        //查询该用户所有收藏的歌曲
        getData() {
            this.tempData = [];
            this.tableData = [];
            getCollectOfUserId(this.$route.query.id).then((res) => {
                for (let item of res) {
                    this.getSong(item.songId);
                }
            });
        },
        //根据歌曲id查询歌曲对象，放到tempData和tableData里面
        getSong(id) {
            songOfSongId(id)
                .then((res) => {
                    this.tempData.push(res);
                    this.tableData.push(res);
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        //删除一条歌曲
        deleteRow() {
            deleteCollection(this.$route.query.id, this.idx.id)
                .then((res) => {
                    if (res) {
                        this.getData();
                        this.notify("删除成功", "success");
                    } else {
                        this.notify("删除失败", "error");
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
            this.delVisible = false;
        },
        //批量删除已经选择的项
        delAll() {
            for (let item of this.multipleSelection) {
                this.handleDelete(item);
                this.deleteRow();
            }
            this.multipleSelection = [];
        },
    },
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}
.handle-input {
    width: 300px;
    display: inline-block;
}
</style>