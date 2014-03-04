package edu.opinion.common.db;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TbReBbs entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TbReBbs implements java.io.Serializable {

	// Fields

	private String id;
	private TbParserBbs tbParserBbs;
	private String url;
	private String idkey;
	private Integer hashCode;
	private String reTitle;
	private String reAuthor;
	private Date reTime;
	private String reContent;
	private String tag;
	private String spTitle;
	private String spContent;
	private Date storageTime;
	private Set tbSensitiveTopics = new HashSet(0);

	// Constructors

	/** default constructor */
	public TbReBbs() {
	}

	/** minimal constructor */
	public TbReBbs(String reAuthor) {
		this.reAuthor = reAuthor;
	}

	/** full constructor */
	public TbReBbs(TbParserBbs tbParserBbs, String url, String idkey,
			Integer hashCode, String reTitle, String reAuthor, Date reTime,
			String reContent, String tag, String spTitle, String spContent,
			Date storageTime, Set tbSensitiveTopics) {
		this.tbParserBbs = tbParserBbs;
		this.url = url;
		this.idkey = idkey;
		this.hashCode = hashCode;
		this.reTitle = reTitle;
		this.reAuthor = reAuthor;
		this.reTime = reTime;
		this.reContent = reContent;
		this.tag = tag;
		this.spTitle = spTitle;
		this.spContent = spContent;
		this.storageTime = storageTime;
		this.tbSensitiveTopics = tbSensitiveTopics;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TbParserBbs getTbParserBbs() {
		return this.tbParserBbs;
	}

	public void setTbParserBbs(TbParserBbs tbParserBbs) {
		this.tbParserBbs = tbParserBbs;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIdkey() {
		return this.idkey;
	}

	public void setIdkey(String idkey) {
		this.idkey = idkey;
	}

	public Integer getHashCode() {
		return this.hashCode;
	}

	public void setHashCode(Integer hashCode) {
		this.hashCode = hashCode;
	}

	public String getReTitle() {
		return this.reTitle;
	}

	public void setReTitle(String reTitle) {
		this.reTitle = reTitle;
	}

	public String getReAuthor() {
		return this.reAuthor;
	}

	public void setReAuthor(String reAuthor) {
		this.reAuthor = reAuthor;
	}

	public Date getReTime() {
		return this.reTime;
	}

	public void setReTime(Date reTime) {
		this.reTime = reTime;
	}

	public String getReContent() {
		return this.reContent;
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSpTitle() {
		return this.spTitle;
	}

	public void setSpTitle(String spTitle) {
		this.spTitle = spTitle;
	}

	public String getSpContent() {
		return this.spContent;
	}

	public void setSpContent(String spContent) {
		this.spContent = spContent;
	}

	public Date getStorageTime() {
		return this.storageTime;
	}

	public void setStorageTime(Date storageTime) {
		this.storageTime = storageTime;
	}

	public Set getTbSensitiveTopics() {
		return this.tbSensitiveTopics;
	}

	public void setTbSensitiveTopics(Set tbSensitiveTopics) {
		this.tbSensitiveTopics = tbSensitiveTopics;
	}

}