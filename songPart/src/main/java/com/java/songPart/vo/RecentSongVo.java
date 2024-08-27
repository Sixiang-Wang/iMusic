package com.java.songPart.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Data
public class RecentSongVo implements Serializable {

    /**
     * 主键
     */
    @Setter
    @Getter
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer id;

    /**
     * 歌手id
     */
    @Setter
    @Getter
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer singerId;

    /**
     * 歌名
     */
    @Setter
    @Getter
    private String name;

    /**
     * 歌手名
     */
    @Setter
    @Getter
    private String singerName;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 歌曲图片
     */
    private String pic;

    /**
     * 歌词
     */
    private String lyric;

    /**
     * 歌曲地址
     */
    private String url;

    /**
     * 播放量
     */
    private Integer playCount;

    //    @Serial
//    private static final long serialVersionUID = 1L;
}
