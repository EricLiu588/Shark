package com.heroliu.beans;

public class Party {
	
	private String title;
	private String article;
	private String date;
	private String location;
	private String author;
	private String image;
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Party(String title, String article, String date, String location, String author,String image) {
		super();
		this.title = title;
		this.article = article;
		this.date = date;
		this.location = location;
		this.author = author;
		this.image = image;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setImage(String image){
		this.image = image;
	}
	public String getImage(){
		return image;
	}

}
