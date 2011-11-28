package com.reindeermobile.hunapknotifier.entities;

import java.util.Date;

public class HunApkInfo {
	private int id;
	private String name;
	private Date date;
	private String link;
	private String author;

	public final int getId() {
		return this.id;
	}

	public final String getName() {
		return this.name;
	}

	public final Date getDate() {
		return this.date;
	}

	public final String getLink() {
		return this.link;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final void setDate(Date date) {
		this.date = date;
	}

	public final void setLink(String link) {
		this.link = link;
	}

	public final String getAuthor() {
		return this.author;
	}

	public final void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "HunApkInfo [id=" + this.id + ", name=" + this.name + ", date=" + this.date + ", link=" + this.link + ", author=" + this.author + "]";
	}

}
