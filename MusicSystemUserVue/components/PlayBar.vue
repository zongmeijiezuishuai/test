<template>
    <div class="play-bar" :class="{ show: !toggle }">
        <div
            @click="toggle = !toggle"
            class="item-up"
            :class="{ turn: toggle }"
        >
            <svg class="icon">
                <use xlink:href="#icon-jiantou-shang-cuxiantiao"></use>
            </svg>
        </div>
        <div class="kongjian">
          <!-- 上一首 -->
            <div class="item" @click="prev">
                <svg class="icon" width="16" height="16" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <path d="M11.89 4.5a.75.75 0 0 0-1.06 0l-5.66 5.66a.75.75 0 0 0 0 1.06l5.66 5.66a.75.75 0 1 0 1.06-1.06L7.75 8l4.14-4.14a.75.75 0 0 0 0-1.06z"/>
                </svg>
            </div>

            <!-- 播放/暂停 -->
            <div class="item" @click="togglePlay">
                <svg class="icon" width="16" height="16" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <!-- 播放 -->
                    <path v-if="!isPlaying" d="M6.25 4.5v7A.75.75 0 0 1 5.5 12H2.5a.75.75 0 0 1-.75-.75V5.25A.75.75 0 0 1 2.5 4.5h3a.75.75 0 0 1 .75.75zm0 7V5.25a.75.75 0 0 0-1.5 0V11.25a.75.75 0 0 0 1.5 0z"/>
                    <!-- 暂停 -->
                    <path v-else d="M3.5 4.5v7a.75.75 0 0 0 1.5 0V4.5a.75.75 0 0 0-1.5 0zm5 0v7a.75.75 0 0 0 1.5 0V4.5a.75.75 0 0 0-1.5 0z"/>
                </svg>
            </div>

            <!-- 下一首 -->
            <div class="item" @click="next">
                <svg class="icon" width="16" height="16" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <path d="M4.11 4.5a.75.75 0 0 1 1.06 0l5.66 5.66a.75.75 0 0 1 0 1.06l-5.66 5.66a.75.75 0 1 1-1.06-1.06L8.25 8 4.11 4.14a.75.75 0 0 1 0-1.06z"/>
                </svg>
            </div>
            <!-- 歌曲图片 -->
            <div class="item-img" @click="toLyric">
                <img :src="picUrl" />
            </div>
            <!-- 播放进度 -->
            <div class="playing-speed">
                <!-- 播放开始时间 -->
                <div class="current-time">{{ nowTime }}</div>
                <div class="progress-box">
                    <div class="item-song-title">
                        <div>{{ this.title }}</div>
                        <div>{{ this.artist }}</div>
                    </div>
                    <div ref="progress" class="progress" @mousemove="mousemove">
                        <!-- 进度条 -->
                        <div ref="bg" class="bg" @click="updatemove">
                            <div
                                ref="curProgress"
                                class="cur-progress"
                                :style="{ width: curLength + '%' }"
                            ></div>
                        </div>
                        <!-- 拖动的点点 -->
                        <div
                            ref="idot"
                            class="idot"
                            :style="{ left: curLength + '%' }"
                            @mousedown="mousedown"
                            @mouseup="mouseup"
                        ></div>
                    </div>
                </div>
                <!-- 播放结束时间 -->
                <div class="left-time">{{ songTime }}</div>
                <!-- 音量 -->
                <div class="item item-volume" style="padding-bottom: -5px">
                    <svg class="icon" width="16" height="16" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                        <path d="M7.5 8a.5.5 0 0 1-.806-.279l-6-6A.5.5 0 0 1 2 2.5v11a.5.5 0 0 1-.806.424l6-6A.5.5 0 0 1 7.5 8zm7.5 3.5a.5.5 0 0 1-1 0V4.464l-2.086 2.086a.5.5 0 0 1-.708-.708l2.828-2.828a.5.5 0 0 1 .708 0l2.828 2.828a.5.5 0 0 1 0 .708L14.036 8V11.5z"/>
                    </svg>
                <el-slider
                    class="volume"
                    v-model="volume"
                    :vertical="true"
                    style="margin-left: 8px;"
                ></el-slider>
</div>
                <!-- 收藏 -->
                <div class="item" @click="collection" style="padding-bottom: -5px">
                    <svg
                        width="16"
                        height="16"
                        xmlns="http://www.w3.org/2000/svg" 
                        xmlns:svg="http://www.w3.org/2000/svg"
                        xmlns:se="http://svg-edit.googlecode.com"
                        class="bi bi-heart-fill"
                        se:nonce="62338"
                    >
                    <g class="layer">
                         <title>Layer 1</title>
                            <path d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/>
                    </g>
                    </svg>    
                </div>
                <!-- 分享 -->
                <div class="item" @click="share" style="padding-bottom: -5px">
                    <svg
                        width="16"
                        height="19"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlns:svg="http://www.w3.org/2000/svg"
                        xmlns:se="http://svg-edit.googlecode.com"
                        class="bi bi-share-fill"
                        se:nonce="62333"
                    >
                        <g class="layer">
                            <title>Layer 1</title>
                            <path
                                d="m11,2.5a2.5,2.5 0 1 1 0.6,1.63l-6.72,3.12a2.5,2.5 0 0 1 0,1.5l6.72,3.12a2.5,2.5 0 1 1 -0.48,0.88l-6.72,-3.12a2.5,2.5 0 1 1 0,-3.26l6.72,-3.12a2.5,2.5 0 0 1 -0.12,-0.75z"
                                id="svg_62333_1"
                            />
                        </g>
                    </svg>
                </div>
                <!-- 下载 -->
                <div class="item" @click="download">
                    <svg class="icon" width="24" height="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
                    </svg>
                </div>
                <!-- 当前播放的歌曲列表 -->
                <div class="item" @click="changeAside">
                    <svg class="icon" width="24" height="24" viewBox="0 0 24 24">
                        <path d="M4 18h16v-2H4v2zm0-5h16v-2H4v2zm0-7v2h16V6H4z"/>
                    </svg>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
    download,
    setCollect,
    getCollectOfUserId,
    songOfSongId,
} from "../api/index";


export default {
    name: "play-bar",
    data() {
        return {
            nowTime: "00:00", //当前播放进度的时间
            songTime: "00:00", //当前歌曲总时间
            curLength: 0, //进度条的位置
            progressLength: 0, //进度条的总长度
            mouseStartX: 0, //拖拽开始位置
            tag: false, //拖拽开始结束的标志，当开始拖拽，它的值才会变成true
            volume: 50, //音量，默认一半
            toggle: true, //显示隐藏播放器页面
        };
    },
    computed: {
        ...mapGetters([
            "id", //歌曲id
            "url", //歌曲地址
            "isPlay", //播放状态
            "playButtonUrl", //播放状态的图标
            "picUrl", //正在播放的音乐的图片
            "title", //歌名
            "artist", //歌手名
            "duration", //音乐时长
            "curTime", //当前音乐的播放位置
            "showAside", //是否显示播放中的歌曲列表
            "listIndex", //当前歌曲在歌单中的位置
            "listOfSongs", //当前歌单列表
            "autoNext", //自动播放下一首
            "loginIn", //用户是否已登录
            "userId", //当前登录用户的id
            "isActive", //当前播放的歌曲是否已收藏
        ]),
    },
    watch: {
        //切换播放状态的图标
        isPlay() {
            if (this.isPlay) {
                this.$store.commit("setPlayButtonUrl", "#icon-zanting");
            } else {
                this.$store.commit("setPlayButtonUrl", "#icon-bofang");
            }
        },
        curTime() {
            this.nowTime = this.formatSeconds(this.curTime);
            this.songTime = this.formatSeconds(this.duration);
            this.curLength = (this.curTime / this.duration) * 100;
        },
        //音量变化
        volume() {
            this.$store.commit("setVolume", this.volume / 100);
        },
        //自动播放下一首
        autoNext() {
            this.next();
        },
    },
    mounted() {
        this.progressLength = this.$refs.progress.getBoundingClientRect().width;
        document.querySelector(".item-volume").addEventListener(
            "click",
            function (e) {
                document.querySelector(".volume").classList.add("show-volume");
                e.stopPropagation();
            },
            false
        );
        document.querySelector(".volume").addEventListener(
            "click",
            function (e) {
                e.stopPropagation();
            },
            false
        );
        document.addEventListener(
            "click",
            function () {
                document
                    .querySelector(".volume")
                    .classList.remove("show-volume");
            },
            false
        );
    },
    methods: {
        share() {
            //分享
            if (this.id && this.id != "") {
                this.copyToClip(
                    "分享一首《" +
                        this.title +
                        "》点击【" +
                        window.location.origin +
                        "/#/lyric?songId=" +
                        this.id +
                        "】与我一起聆听！"
                );
            } else {
                this.notify("当前未选择音乐。", "error");
            }
        },
        copyToClip(content) {
            var aux = document.createElement("input");
            aux.setAttribute("value", content);
            document.body.appendChild(aux);
            aux.select();
            document.execCommand("copy");
            document.body.removeChild(aux);
            this.notify("您已成功获取音乐链接，快去粘贴分享吧。", "success");

            //   navigator.clipboard.writeText(content).then(() => {
            //     this.notify("您已成功获取音乐链接，快去粘贴分享吧。", "success");
            //   });
        },
        //提示信息
        notify(title, type) {
            this.$notify({
                title: title,
                type: type,
            });
        },
        //控制音乐播放、暂停
        togglePlay() {
            if (!this.id || this.id == "") {
                this.notify("当前未选择音乐。", "error");
                return;
            }
            if (this.isPlay) {
                this.$store.commit("setIsPlay", false);
            } else {
                this.$store.commit("setIsPlay", true);
            }
        },
        //解析时间
        formatSeconds(value) {
            let theTime = parseInt(value);
            let result = ""; //返回值
            let hour = parseInt(theTime / 3600); //小时
            let minute = parseInt((theTime / 60) % 60); //分钟
            let second = parseInt(theTime % 60); //秒
            if (hour > 0) {
                if (hour < 10) {
                    result = "0" + hour + ":";
                } else {
                    result = hour + ":";
                }
            }
            if (minute > 0) {
                if (minute < 10) {
                    result += "0" + minute + ":";
                } else {
                    result += minute + ":";
                }
            } else {
                result += "00:";
            }
            if (second > 0) {
                if (second < 10) {
                    result += "0" + second;
                } else {
                    result += second;
                }
            } else {
                result += "00";
            }
            return result;
        },
        //拖拽开始
        mousedown(e) {
            if (!this.id || this.id == "") {
                return;
            }
            this.mouseStartX = e.clientX;
            this.tag = true;
        },
        //拖拽结束
        mouseup() {
            this.tag = false;
        },
        //拖拽中
        mousemove(e) {
            if (!this.duration) {
                return false;
            }
            if (this.tag) {
                let movementX = e.clientX - this.mouseStartX; //点点移动的距离
                let curLength =
                    this.$refs.curProgress.getBoundingClientRect().width;
                let newPercent =
                    ((movementX + curLength) / this.progressLength) * 100;
                if (newPercent > 100) {
                    newPercent = 100;
                }
                this.curLength = newPercent;
                this.mouseStartX = e.clientX;
                this.changeTime(newPercent);
            }
        },
        //更改歌曲进度
        changeTime(percent) {
            let newCurTime = percent * 0.01 * this.duration;
            this.$store.commit("setChangeTime", newCurTime);
        },
        //点击播放条切换播放进度
        updatemove(e) {
            if (!this.id || this.id == "") {
                return;
            }
            if (!this.tag) {
                //进度条的左侧坐标
                let curLength = this.$refs.bg.offsetLeft;
                let newPercent =
                    ((e.clientX - curLength) / this.progressLength) * 100;
                if (newPercent > 100) {
                    newPercent = 100;
                } else if (newPercent < 0) {
                    newPercent = 0;
                }
                this.curLength = newPercent;
                this.changeTime(newPercent);
            }
        },
        //显示播放中的歌曲列表
        changeAside() {
            this.$store.commit("setShowAside", true);
        },
        //上一首
        prev() {
            if (!this.id || this.id == "") {
                this.notify("当前未选择音乐。", "error");
                return;
            }
            if (this.listIndex != -1 && this.listOfSongs.length > 1) {
                //当前处于不可能状态或者只有只有一首音乐的时候不执行）
                if (this.listIndex > 0) {
                    //不是第一首音乐
                    this.$store.commit("setListIndex", this.listIndex - 1); //直接返回上一首
                } else {
                    //当前是第一首音乐
                    this.$store.commit(
                        "setListIndex",
                        this.listOfSongs.length - 1
                    ); //切换到倒数第一首
                }
                this.toplay(this.listOfSongs[this.listIndex].url);
            }
        },
        //下一首
        next() {
            if (!this.id || this.id == "") {
                this.notify("当前未选择音乐。", "error");
                return;
            }
            if (this.listIndex != -1 && this.listOfSongs.length > 1) {
                //当前处于不可能状态或者只有只有一首音乐的时候不执行）
                if (this.listIndex < this.listOfSongs.length - 1) {
                    //不是最后一首音乐
                    this.$store.commit("setListIndex", this.listIndex + 1); //直接返回下一首
                } else {
                    //当前是最后一首音乐
                    this.$store.commit("setListIndex", 0); //切换到第一首
                }
                this.toplay(this.listOfSongs[this.listIndex].url);
            }
        },
        //播放音乐
        toplay: function (url) {
            if (url && url != this.url) {
                this.$store.commit(
                    "setId",
                    this.listOfSongs[this.listIndex].id
                );
                this.$store.commit(
                    "setUrl",
                    this.$store.state.configure.HOST + url
                );
                this.$store.commit(
                    "setPicUrl",
                    this.$store.state.configure.HOST +
                        this.listOfSongs[this.listIndex].pic
                );
                this.$store.commit(
                    "setTitle",
                    this.replaceFName(this.listOfSongs[this.listIndex].name)
                );
                this.$store.commit(
                    "setArtist",
                    this.replaceLName(this.listOfSongs[this.listIndex].name)
                );

                this.$store.commit(
                    "setLyric",
                    this.parseLyric(this.listOfSongs[this.listIndex].lyric)
                );
                this.$store.commit("setIsActive", false);
                if (this.loginIn) {
                    getCollectOfUserId(this.userId).then((res) => {
                  console.log(res)
                        for (let item of res) {
                            if (item.songId == this.id) {
                                this.$store.commit("setIsActive", true);
                                break;
                            }
                        }
                    });
                }
            }
        },
        //获取名字前半部分--歌手名
        replaceLName(str) {
            let arr = str.split("-");
            return arr[0];
        },
        //获取名字后半部分--歌名
        replaceFName(str) {
            let arr = str.split("-");
            return arr[1];
        },
        //解析歌词
        parseLyric(text) {
            let lines = text.split("\n"); //将歌词按行分解成数组
            let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g; //时间格式的正则表达式
            let result = []; //返回值
            //对于歌词格式不对的直接返回
            if (!/\[.+\]/.test(text)) {
                return [[0, text]];
            }
            //去掉前面格式不正确的行
            while (!pattern.test(lines[0])) {
                lines = lines.slice(1);
            }
            //遍历每一行，形成一个每行带着俩元素的数组，第一个元素是以秒为计算单位的时间，第二个元素是歌词
            for (let item of lines) {
                let time = item.match(pattern); //存前面的时间段
                let value = item.replace(pattern, ""); //存后面的歌词
                if (time == undefined || value == undefined) {
                    continue;
                }
                for (let item1 of time) {
                    let t = item1.slice(1, -1).split(":"); //取出时间，换算成数组
                    if (value != "") {
                        result.push([
                            parseInt(t[0], 10) * 60 + parseFloat(t[1]),
                            value,
                        ]);
                    }
                }
            }
            //按照第一个元素--时间--排序
            result.sort(function (a, b) {
                return a[0] - b[0];
            });
            return result;
        },
        //转向歌词页面
        toLyric() {
            this.$router.push({ path: `/lyric` });
        },
        //下载音乐
        download() {
            if (!this.id || this.id == "") {
                this.notify("当前未选择音乐。", "error");
                return;
            }
            download(this.url)
                .then((res) => {
                    let content = res.data;
                    let eleLink = document.createElement("a");
                    eleLink.download = `${this.artist}-${this.title}.mp3`;
                    eleLink.style.display = "none";
                    //把字符内容转换成blob地址
                    let blob = new Blob([content]);
                    eleLink.href = URL.createObjectURL(blob);
                    //把链接地址加到document里
                    document.body.appendChild(eleLink);
                    //触发点击
                    eleLink.click();
                    //然后移除掉这个新加的控件
                    document.body.removeChild(eleLink);
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        //收藏
        collection() {
            if (!this.id || this.id == "") {
                this.notify("当前未选择音乐。", "error");
                return;
            }
            if (this.loginIn) {
                var params = new URLSearchParams();
                params.append("userId", this.userId);
                params.append("type", 0);
                params.append("songId", this.id);
                setCollect(params).then((res) => {
                    if (res.code == 1) {
                        this.$store.commit("setIsActive", true);
                        this.notify("收藏成功", "success");
                    } else if (res.code == 2) {
                        this.$store.commit("setIsActive", false);
                        this.notify("取消收藏成功", "warning");
                    } else {
                        this.notify("收藏失败", "error");
                    }
                });
            } else {
                this.notify("请先登录", "warning");
            }
        },
    },
};
</script>
<style  lang="scss" scoped>
@import "../assets/css/play-bar.scss";
</style>
