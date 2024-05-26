package com.odyssey.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String picture;

    @ManyToOne
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;


    public News(){}

    public News(String title, String description, String picture, User author) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.author = author;
    }

    public News(Integer id, String title, String description, String picture, User author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picturre) {
        this.picture = picturre;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        News news = (News) o;
        return
                Objects.equals(id, news.id) && Objects.equals(title, news.title) &&
                        Objects.equals(description, news.description) && Objects.equals(picture, news.picture) &&
                        Objects.equals(author, news.author) ;

    }


    @Override
    public int hashCode(){
        return Objects.hash(id,title,description,picture,author);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picture=" + picture +
                ", author=" + author +

                '}';
    }
}
