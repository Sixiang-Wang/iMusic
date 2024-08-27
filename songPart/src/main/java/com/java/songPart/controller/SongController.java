package com.java.songPart.controller;

import com.alibaba.fastjson.JSONObject;

import com.java.songPart.domain.Follow;
import com.java.songPart.domain.Message;
import com.java.songPart.domain.User;
import com.java.songPart.utils.Consts;
import com.java.songPart.utils.Port;
import com.java.songPart.config.PathConfig;
import com.java.songPart.dao.SongMapper;
import com.java.songPart.domain.Song;
import com.java.songPart.service.SongService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 歌曲管理controller
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongService songService;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Getter
    private static SongController songController;




    /**
     * 添加歌曲
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addSong(HttpServletRequest request, @RequestParam("file")MultipartFile mpFile){

        JSONObject jsonObject = new JSONObject();
        //获取前端传来的参数
        String id = request.getParameter("id");
        String userId = request.getParameter("userId").trim();  //所属歌手id
        String name = request.getParameter("name").trim();          //歌名
        String introduction = request.getParameter("introduction").trim();          //简介
        String pic = "/img/songPic/default.jpg";                     //默认图片
        String lyric = request.getParameter("lyric").trim();     //歌词
        String style = request.getParameter("style").trim();     //风格
//        String userName = userService.selectByPrimaryKey(Integer.parseInt(userId)).getName();
        String url = Port.url_base+ Port.port_base+"/user/selectByPrimaryKey?id="+Integer.parseInt(userId);
        User user = restTemplate.getForObject(url, User.class);

        //上传歌曲文件
        if(mpFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"歌曲上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis()+mpFile.getOriginalFilename();
        //文件路径
        String filePath = PathConfig.path +System.getProperty("file.separator")+"song";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if(!file1.exists()){
            file1.mkdir();
        }

        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeUrlPath = "/song/"+fileName;

        try {
            mpFile.transferTo(dest);
            Song song = new Song();
            song.setUserId(Integer.parseInt(userId));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setPic(pic);
            song.setLyric(lyric);
            song.setUrl(storeUrlPath);
            song.setStyle(style);

            if (id!=null&&!id.isEmpty()){
                song.setId(Integer.parseInt(id));
            }

            boolean flag = songService.insert(song);

            if(flag){
                List<Follow> followList = restTemplate.exchange(
                        Port.url_base+ Port.port_base+"/follow/getBySingerId?singerId="+Integer.parseInt(userId),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Follow>>() {}
                ).getBody();
//                List<Follow> followList = followService.getBySingerId(Integer.parseInt(userId));
                followList.forEach(follow->{
                    Message message = new Message();
                    message.setTo(follow.getUserId());
                    message.setFrom(-1);
                    assert user != null;
                    message.setText("您关注的歌手 %s 发布了新歌《%s》!" + user.getName() + name);
                    message.setIsRead(0);
                    message.setType(0);
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Content-Type", "application/json");

                    HttpEntity<Message> requestEntity = new HttpEntity<>(message, headers);

                    // 发送 POST 请求
                    restTemplate.postForObject(
                            Port.url_base+ Port.port_base+"/message/add",
                            requestEntity,
                            Void.class
                    );
//                    messageService.insert(message);
                });
                jsonObject.put(Consts.CODE,1);
                jsonObject.put(Consts.MSG,"保存成功");
                jsonObject.put("avator",storeUrlPath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"保存失败");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"保存失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }

    /**
     * 根据歌手id查询歌曲
     */
    @RequestMapping(value = "/songOfUserId",method = RequestMethod.GET)
    public Object songOfUserId(HttpServletRequest request){
        String userId = request.getParameter("userId");
        return songService.songOfUserId(Integer.parseInt(userId));
    }

    @RequestMapping(value = "/songOfUserId/addPrefix",method = RequestMethod.GET)
    public Object songOfUserIdAddPrefix(HttpServletRequest request){
        String userId = request.getParameter("userId");
        return songService.addPrefix(songService.songOfUserId(Integer.parseInt(userId)));
    }

    /**
     * 修改歌曲
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateSong(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();          //主键
        String name = request.getParameter("name").trim();      //歌名
        String introduction = request.getParameter("introduction").trim();//专辑
        String lyric = request.getParameter("lyric").trim();    //歌词
        String style = request.getParameter("style").trim();     //风格

        //保存到歌手的对象中
        Song song = new Song();
        song.setId(Integer.parseInt(id));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setStyle(style);
        song.setLyric(lyric);

        boolean flag = songService.update(song);
        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"修改成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"修改失败");
        return jsonObject;
    }

    /**
     * 删除歌曲
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object deleteSong(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        return songService.delete(Integer.parseInt(id));
    }

    @RequestMapping(value = "/invisible",method = RequestMethod.GET)
    public Object invisibleSong(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        Song song = songService.selectByPrimaryKey(Integer.parseInt(id));
        song.setVisible(0);
        boolean flag = songService.update(song);
        if(flag){
            Message message = new Message();
            message.setTo(song.getUserId());
            message.setFrom(-1);
            message.setText("您的歌曲《"+song.getName()+"》已被下架");
            message.setIsRead(0);
            message.setType(0);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Message> requestEntity = new HttpEntity<>(message, headers);

            // 发送 POST 请求
            restTemplate.postForObject(
                    Port.url_base+ Port.port_base+"/message/add",
                    requestEntity,
                    Void.class
            );
//            messageService.insert(message);
        }
        return flag;
    }

    @RequestMapping(value = "/allInvisible",method = RequestMethod.GET)
    public Object allInvisibleSong(HttpServletRequest request){
        return songMapper.allInvisible();
    }
    @RequestMapping(value = "/allInvisible/addPrefix",method = RequestMethod.GET)
    public Object allInvisibleSongAddPrefix(HttpServletRequest request){
        return songService.addPrefix(songMapper.allInvisible());
    }

    @RequestMapping(value = "/visible",method = RequestMethod.GET)
    public Object visibleSong(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        Song song = songService.selectByPrimaryKey(Integer.parseInt(id));
        song.setVisible(1);
        boolean flag = songService.update(song);
        if(flag){
            Message message = new Message();
            message.setTo(song.getUserId());
            message.setFrom(-1);
            message.setText("您的歌曲《"+song.getName()+"》已恢复");
            message.setIsRead(0);
            message.setType(0);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Message> requestEntity = new HttpEntity<>(message, headers);

            // 发送 POST 请求
            restTemplate.postForObject(
                    Port.url_base+ Port.port_base+"/message/add",
                    requestEntity,
                    JSONObject.class
            );
//            messageService.insert(message);
        }
        return flag;
    }


    /**
     * 更新歌曲图片
     */
    @RequestMapping(value = "/updateSongPic",method = RequestMethod.POST)
    public Object updateSongPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();
        if(avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        //文件路径
        String filePath = PathConfig.path+System.getProperty("file.separator")+"img"
                +System.getProperty("file.separator")+"songPic";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        Song song = songService.selectByPrimaryKey(id);
        String oldPic = song.getPic();
        if(!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/img/songPic/"+fileName;
        try {
            avatorFile.transferTo(dest);
            song.setId(id);
            song.setPic(storeAvatorPath);
            boolean flag = songService.update(song);
            if(!flag){
                jsonObject.put(Consts.CODE,0);
                jsonObject.put(Consts.MSG,"上传失败");
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"上传成功");
            jsonObject.put("pic",storeAvatorPath);

            if(!oldPic.equals("/img/songPic/default.jpg")){
                File oldPicFile = new File(PathConfig.path +System.getProperty("file.separator") + oldPic);
                if(!oldPicFile.delete()) {
                    System.out.println("旧歌曲图片删除失败:SongController-updateSongPic");
                }
            }
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }


    /**
     * 更新歌曲
     */
    @RequestMapping(value = "/updateSongUrl",method = RequestMethod.POST)
    public Object updateSongUrl(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();
        Song song = songService.selectByPrimaryKey(id);
        String oldUrl = song.getUrl();
        if(avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        //文件路径
        String filePath = PathConfig.path+System.getProperty("file.separator")+"song";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeSongPath = "/song/"+fileName;
        try {
            avatorFile.transferTo(dest);
            song.setId(id);
            song.setUrl(storeSongPath);
            boolean flag = songService.update(song);
            if(!flag){
                jsonObject.put(Consts.CODE,0);
                jsonObject.put(Consts.MSG,"上传失败");
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"上传成功");
            jsonObject.put("song",storeSongPath);

            File oldFile = new File(PathConfig.path +System.getProperty("file.separator")+oldUrl);

            if(!oldFile.delete()) {
                System.out.println("旧歌曲删除失败:SongController-updateSongUrl");
            }

            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }

    /**
     * 更新歌曲MV
     */
    @RequestMapping(value = "/updateMVUrl",method = RequestMethod.POST)
    public Object updateMVUrl(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();
        if(avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        //文件路径
        String filePath = PathConfig.path+System.getProperty("file.separator")+"mv";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/mv/"+fileName;
        try {
            avatorFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setMvurl(storeAvatorPath);
            boolean flag = songService.update(song);
            if(flag){
                jsonObject.put(Consts.CODE,1);
                jsonObject.put(Consts.MSG,"上传成功");
                jsonObject.put("avator",storeAvatorPath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }

    /**
     * 根据歌曲id查询歌曲对象
     */
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public Object detail(HttpServletRequest request){
        String songId = request.getParameter("songId");
        return songService.selectByPrimaryKey(Integer.parseInt(songId));
    }

    @RequestMapping(value = "/detail/addPrefix",method = RequestMethod.GET)
    public Object detailAddPrefix(HttpServletRequest request){
        String songId = request.getParameter("songId");
        return songService.addPrefix(songService.selectByPrimaryKey(Integer.parseInt(songId)));
    }

    /**
     * 根据歌曲id增加歌曲播放次数
     */
    @RequestMapping(value = "/addNums",method = RequestMethod.GET)
    public Object addNums(HttpServletRequest request){
        String songId = request.getParameter("songId");
        return songService.addNums(Integer.parseInt(songId));
    }

    /**
     * 根据名字精确查询歌曲
     */
    @RequestMapping(value = "/songOfSongName",method = RequestMethod.GET)
    public Object songOfSongName(HttpServletRequest request){
        String songName = request.getParameter("songName");
        return songService.songOfName(songName);
    }
    @RequestMapping(value = "/songOfSongName/addPrefix",method = RequestMethod.GET)
    public Object songOfSongNameAddPrefix(HttpServletRequest request){
        String songName = request.getParameter("songName");
        return songService.addPrefix(songService.songOfName(songName));
    }

    /**
     * 根据歌手名字模糊查询歌曲
     */
    @RequestMapping(value = "/likeSongOfName",method = RequestMethod.GET)
    public Object likeSongOfName(HttpServletRequest request){
        String songName = request.getParameter("songName");
        return songService.likeSongOfName(songName);
    }
    @RequestMapping(value = "/likeSongOfName/addPrefix",method = RequestMethod.GET)
    public Object likeSongOfNameAddPrefix(HttpServletRequest request){
        String songName = request.getParameter("songName");
        return songService.addPrefix(songService.likeSongOfName(songName));
    }

    /**
     * 查询所有歌曲
     */
    @RequestMapping(value = "/allSong",method = RequestMethod.GET)
    public Object allSong(HttpServletRequest request){
        return songService.allSong();
    }
    @RequestMapping(value = "/allSong/addPrefix",method = RequestMethod.GET)
    public Object allSongAddPrefix(HttpServletRequest request){
        return songService.addPrefix(songService.allSong());
    }

    /**
     * 查询所有歌曲
     */
    @RequestMapping(value = "/topSong",method = RequestMethod.GET)
    public Object topSong(HttpServletRequest request){
        return songService.topSong();
    }
    @RequestMapping(value = "/topSong/addPrefix",method = RequestMethod.GET)
    public Object topSongAddPrefix(HttpServletRequest request){
        return songService.addPrefix(songService.topSong());
    }

    @RequestMapping(value = "/songOfStyle",method = RequestMethod.GET)
    public Object songOfStyle(HttpServletRequest request){
        String style = request.getParameter("style");
        if ("其他".equals(style)|| "其它".equals(style)){
            return songMapper.songOfOtherStyle();
        }
        return songService.songOfStyle("%"+style+"%");
    }
    @RequestMapping(value = "/songOfStyle/addPrefix",method = RequestMethod.GET)
    public Object songOfStyleAddPrefix(HttpServletRequest request){
        String style = request.getParameter("style");
        if ("其他".equals(style)|| "其它".equals(style)){
            return songMapper.songOfOtherStyle();
        }
        return songService.addPrefix(songService.songOfStyle("%"+style+"%"));
    }

    /**
     * 某用户被点赞最多的歌
     * @param request
     * @return
     */
    @RequestMapping(value = "/popularSongOfUser",method = RequestMethod.GET)
    public Object popularSongOfUser(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        List<Song> songs = songMapper.songOfUserId(userId);
        if(songs==null||songs.isEmpty()){
            return -1;
        }
        return songMapper.popularSongOfUser(userId);
    }
    @RequestMapping(value = "/popularSongOfUser/addPrefix",method = RequestMethod.GET)
    public Object popularSongOfUserAddPrefix(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        List<Song> songs = songMapper.songOfUserId(userId);
        if(songs==null||songs.isEmpty()){
            return -1;
        }
        return songService.addPrefix(songMapper.popularSongOfUser(userId));
    }

    /**
     * 某用户被收藏最多的歌
     * @param request
     * @return
     */
    @RequestMapping(value = "/popularCollectedSongOfUser",method = RequestMethod.GET)
    public Object popularCollectedSongOfUser(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        List<Song> songs = songMapper.songOfUserId(userId);
        if(songs==null||songs.isEmpty()){
            return -1;
        }
        return songs.toArray()[0];
    }
    @RequestMapping(value = "/popularCollectedSongOfUser/addPrefix",method = RequestMethod.GET)
    public Object popularCollectedSongOfUserAddPrefix(HttpServletRequest request){
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        List<Song> songs = songMapper.songOfUserId(userId);
        if(songs==null||songs.isEmpty()){
            return -1;
        }
        return songs.toArray()[0];
    }

    /**
     * 判断歌曲是否是该用户的
     * @param request
     * @return
     */
    @RequestMapping(value = "/isSongOfUser",method = RequestMethod.GET)
    public Object isSongOfUser(HttpServletRequest request){
        Integer songId = Integer.parseInt(request.getParameter("songId"));
        Integer userId = Integer.parseInt(request.getParameter("userId"));
        Song song = songService.selectByPrimaryKey(songId);
//        User user = userService.selectByPrimaryKey(song.getUserId());
        String url = Port.url_base+ Port.port_base+"/user/selectByPrimaryKey?id="+userId;
        User user = restTemplate.getForObject(url, User.class);
        assert user != null;
        if(user.getId().equals(userId)){
            return true;
        }else{
            return false;
        }
    }

}




















