<template>
  <div class="swiper">
      <el-carousel :interval="200000" type="card" height="280px">
          <el-carousel-item v-for="(item, index) in swiperList" :key="index" @click.native="playMusic(item)">
              <img :src="item.picImg" alt="Carousel Image"/>
          </el-carousel-item>
      </el-carousel>
      <audio ref="audioPlayer" controls style="display:none;">
          Your browser does not support the audio element.
      </audio>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';

export default {
name: 'SwiperComponent',
data() {
  return {
    swiperList: [
    { picImg: require('@/assets/img/swiper/3.jpg'), musicUrl: require('@/assets/music/FIESTAR - 짠해.mp3'), songInfo: { title: '짠해', artist: 'FIESTAR', picUrl: require('@/assets/img/swiper/3.jpg') } },
    { picImg: require('@/assets/img/swiper/2.jpg'), musicUrl: require('@/assets/music/等你等到我心痛-张学友#93kH.mp3'), songInfo: { title: '等你等到我心痛', artist: '张学友', picUrl: require('@/assets/img/swiper/2.jpg') } },
      { picImg: require('@/assets/img/swiper/1.jpg'), musicUrl: require('@/assets/music/我在 .I AM NOT HERE. .伴奏.-王一博#2FICZQ.mp3'), songInfo: { title: '我在 .I AM NOT HERE', artist: '王一博', picUrl: require('@/assets/img/swiper/1.jpg') } },
      { picImg: require('@/assets/img/swiper/5.jpg'), musicUrl: require('@/assets/music/写不完的温柔-G.E.M.邓紫棋#f50bN.mp3'), songInfo: { title: '写不完的温柔', artist: '邓紫棋', picUrl: require('@/assets/img/swiper/5.jpg') } },
      { picImg: require('@/assets/img/swiper/4.jpg'), musicUrl: require('@/assets/music/周深+-+总有美好在路上.mp3'), songInfo: { title: '总有美好在路上', artist: '周深', picUrl: require('@/assets/img/swiper/4.jpg') } }
      // 添加其他项...
    ]
  };
},
computed: {
  ...mapGetters(['title', 'artist', 'picUrl']),
  currentSong() {
    return {
      title: this.title,
      artist: this.artist,
      picUrl: this.picUrl
    };
  }
},
methods: {
  ...mapMutations(['setTitle', 'setArtist', 'setPicUrl']),
  playMusic(item) {
    const audio = this.$refs.audioPlayer;
    audio.src = item.musicUrl;
    audio.play();
    
    // 更新Vuex store中的歌曲信息
    this.setTitle(item.songInfo.title);
    this.setArtist(item.songInfo.artist);
    this.setPicUrl(item.songInfo.picUrl);
  }
}
};
</script>

<style scoped>
.current-song-info img {
width: 100px; /* 设置图片大小 */
height: auto;
}
</style>