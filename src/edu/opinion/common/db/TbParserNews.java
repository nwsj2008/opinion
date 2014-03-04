package edu.opinion.common.db;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TbParserNews entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TbParserNews implements java.io.Serializable {

	// Fields

	private String id;
	private String url;
	private String idkey;
	private Integer hashCode;
	private String title;
	private String author;
	private String source;
	private Date releaseTime;
	private String content;
	private Integer viewNum;
	private Integer reNum;
	private String tag;
	private String spTitle;
	private String spContent;
	private Date storageTime;
	private Set tbClusterTopics = new HashSet(0);
	private Set tbReNewses = new HashSet(0);
	private Set tbSensitiveTopics = new HashSet(0);

	// Constructors

	/** default constructor */
	public TbParserNews() {
	}

	/** minimal constructor */
	public TbParserNews(String tag) {
		this.tag = tag;
	}

	/** full constructor */
	public TbParserNews(String url, String idkey, Integer hashCode,
			String title, String author, String source, Date releaseTime,
			String content, Integer viewNum, Integer reNum, String tag,
			String spTitle, String spContent, Date storageTime,
			Set tbClusterTopics, Set tbReNewses, Set tbSensitiveTopics) {
		this.url = url;
		this.idkey = idkey;
		this.hashCode = hashCode;
		this.title = title;
		this.author = author;
		this.source = source;
		this.releaseTime = releaseTime;
		this.content = content;
		this.viewNum = viewNum;
		this.reNum = reNum;
		this.tag = tag;
		this.spTitle = spTitle;
		this.spContent = spContent;
		this.storageTime = storageTime;
		this.tbClusterTopics = tbClusterTopics;
		this.tbReNewses = tbReNewses;
		this.tbSensitiveTopics = tbSensitiveTopics;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getReleaseTime() {
		return this.releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getViewNum() {
		return this.viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getReNum() {
		return this.reNum;
	}

	public void setReNum(Integer reNum) {
		this.reNum = reNum;
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

	public Set getTbClusterTopics() {
		return this.tbClusterTopics;
	}

	public void setTbClusterTopics(Set tbClusterTopics) {
		this.tbClusterTopics = tbClusterTopics;
	}

	public Set getTbReNewses() {
		return this.tbReNewses;
	}

	public void setTbReNewses(Set tbReNewses) {
		this.tbReNewses = tbReNewses;
	}

	public Set getTbSensitiveTopics() {
		return this.tbSensitiveTopics;
	}

	public void setTbSensitiveTopics(Set tbSensitiveTopics) {
		this.tbSensitiveTopics = tbSensitiveTopics;
	}

}