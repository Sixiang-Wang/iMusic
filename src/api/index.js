import {get,post} from "./http"
import songList from "../pages/SongList.vue";
import Axios from "axios";


// ============歌手相关=============
// 查询歌手
export const getAllSinger =() =>get(`singer/allSinger`);

// ============歌曲相关=============
// 根据歌手id查询歌曲
export const songOfSingerId =(id) =>get(`song/singer/detail?singerId=${id}`);
// 根据歌曲id查询歌曲对象
export const songOfSongId =(id) => get(`song/detail?songId=${id}`);
// 根据歌曲名字模糊查询歌曲
export const likeSongOfName =(keywords) => get(`song/likeSongOfName?songName=${keywords}`);
// ============歌单相关==============
// 查询歌曲
export const getAllSongList =() => get(`songList/allSongList`);//获得所有歌单

//根据歌单标题模糊查询歌单
export const likeTitle =(keywords) => get(`songList/likeTitle?title=${keywords}`);

// =============歌单的歌曲相关========
// 根据歌曲id查询歌曲列表
export const listSongDetail = (songListId) => get(`listSong/detail?songListId=${songListId}`);

// ============用户相关==============

// 查询用户
export const getAllUser=()=>get(`consumer/allUser`);

// 注册
export const SignUp = params => post(`/user/add`, params);

// 登录
export const LoginIn = params => post(`/user/login`, params);
// 根据用户id查询该用户的详细信息
export const getUserOfId = (id) => get(`/consumer/selectByPrimaryKey?id=${id}`);
// 根据用户id查询该用户的详细信息
export const updateUserMsg = (params) => post(`/consumer/update`, params);
// 下载音乐
export const download = (url) => Axios({
  method: 'get',
  url: url,
  responseType: 'blob'
});
// =============== 评价 ==================
// =============== 评论 ==================
// 提交评论
export const setComment = (params) => post(`/comment/add`, params);
// 点赞
export const setLike = (params) => post(`/comment/like`, params);
// 返回当前歌单或歌曲的评论区列表

// =============== 收藏 ==================
// 新增收藏
export const setCollect = (params) => post(`/collect/add` ,params);
// 指定用户的收藏列表
export const getCollectOfUserId = (userId) => get(`/collect/collectOfUserId?userId=${userId}`);
