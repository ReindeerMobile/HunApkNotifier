package com.reindeermobile.hunapknotifier.entities;

import com.reindeermobile.reindeerutils.db.BaseDbEntity;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory.Column;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory.NotNull;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory.Table;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "hun_apk_info")
public class HunApkInfo extends BaseDbEntity implements Comparable<HunApkInfo>,
		Parcelable {

	@Column
	@NotNull
	private String name;

	@Column
	private Date date;

	@Column
	@NotNull
	private String link;

	@Column
	private String author;

	@Column
	private boolean readed;

	public static List<HunApkInfo> getDifferences(List<HunApkInfo> local,
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

	public final String getName() {
		return this.name;
	}

	public final Date getDate() {
		return this.date;
	}

	public final String getLink() {
		return this.link;
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

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	@Override
	public int compareTo(HunApkInfo another) {
		if (this.date != null && another.getDate() != null) {
			return this.date.compareTo(another.getDate()) * -1;
		} else {
			if (this.date == null) {
				return -1;
			} else if (another.getDate() == null) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.author == null) ? 0 : this.author.hashCode());
		result = prime * result
				+ ((this.date == null) ? 0 : this.date.hashCode());
		result = prime * result
				+ ((this.link == null) ? 0 : this.link.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HunApkInfo other = (HunApkInfo) obj;
		if (this.author == null) {
			if (other.author != null) {
				return false;
			}
		} else if (!this.author.equals(other.author)) {
			return false;
		}
		if (this.date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!this.date.equals(other.date)) {
			return false;
		}
		if (this.link == null) {
			if (other.link != null) {
				return false;
			}
		} else if (!this.link.equals(other.link)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "HunApkInfo [name=" + this.name + ", date=" + this.date
				+ ", link=" + this.link + ", author=" + this.author + ", id="
				+ this.id + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeLong(this.date.getTime());
		dest.writeString(this.link);
		dest.writeString(this.author);
		dest.writeInt((this.readed) ? 1 : 0);
	}

}
