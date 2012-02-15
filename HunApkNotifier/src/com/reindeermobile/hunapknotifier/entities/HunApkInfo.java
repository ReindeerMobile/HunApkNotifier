package com.reindeermobile.hunapknotifier.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HunApkInfo implements Comparable<HunApkInfo> {
	private int id;
	private String name;
	private Date date;
	private String link;
	private String author;

	public List<HunApkInfo> getDifferences(List<HunApkInfo> local,
			List<HunApkInfo> online) {
		List<HunApkInfo> differences = new ArrayList<HunApkInfo>();

		for (HunApkInfo hunApkInfo : online) {
			if (!local.contains(hunApkInfo)) {
				differences.add(hunApkInfo);
			}
		}

		return differences;
	}

	public HunApkInfo() {
		super();
	}

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
	public int compareTo(HunApkInfo another) {
		if (this.id < another.getId()) {
			return -1;
		} else if (this.id > another.getId()) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "HunApkInfo [id=" + this.id + ", name=" + this.name + ", date="
				+ this.date + ", link=" + this.link + ", author=" + this.author
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HunApkInfo other = (HunApkInfo) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
