package com.example.tears;

public class Music {
    private int id;//歌曲id
    private int duration;//歌曲时长
    private int picture;//歌曲图片
    private long size;//歌曲文件大小
    private String name;//歌曲名称
    private String path;//歌曲文件路径
    private String artist;//歌曲艺术家

    public Music() {
    }

    public Music(int id, int duration, int picture, long size, String name, String path, String artist) {
        this.id = id;
        this.duration = duration;
        this.picture = picture;
        this.size = size;
        this.name = name;
        this.path = path;
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", duration=" + duration +
                ", picture=" + picture +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
