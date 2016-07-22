package com.heroliu.beans;

import android.graphics.Bitmap;

public class News {

	private String title;
	private String article;
	private String date;
	private String author;
	private String positive;
	private String image;

	public News(String title,String author,String article,String date,String positive,String image){
		this.title = title;
		this.author = author;
		this.article = article;
		this.date = date;
		this.positive = positive;
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor(){return author;}
	public void setAuthor(String author){this.author = author;}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setPositive(String positive){
		this.positive = positive;
	}
	public String getPositive(){
		return positive;
	}
}
