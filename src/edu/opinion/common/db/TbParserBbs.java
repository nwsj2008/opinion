package edu.opinion.common.db;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TbParserBbs entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TbParserBbs implements java.io.Serializable {

	// Fields

	private String id;
	private String url;
	private String idkey;
	private Integer hashCode;
	private String topic;
	private String author;
	private Date releaseTime;
	private String content;
	private Integer reNum;
	private String tag;
	private String spTopic;
	private String spContent;
	private Date storageTime;
	private Set tbSensitiveTopics = new HashSet(0);
	private Set tbClusterTopics = new HashSet(0);
	private Set tbReBbses = new HashSet(0);

	// Constructors

	/** default constructor */
	public TbParserBbs() {
	}

	/** minimal constructor */
	public TbParserBbs(String topic, String author, Date releaseTime,
			Integer reNum) {
		this.topic = topic;
		this.author = author;
		this.releaseTime = releaseTime;
		this.reNum = reNum;
	}

	/** full constructor */
	public TbParserBbs(String url, String idkey, Integer hashCode,
			String topic, String author, Date releaseTime, String content,
			Integer reNum, String tag, String spTopic, String spContent,
			Date storageTime, Set tbSensitiveTopics, Set tbClusterTopics,
			Set tbReBbses) {
		this.url = url;
		this.idkey = idkey;
		this.hashCode = hashCode;
		this.topic = topic;
		this.author = author;
		this.releaseTime = releaseTime;
		this.content = content;
		this.reNum = reNum;
		this.tag = tag;
		this.spTopic = spTopic;
		this.spContent = spContent;
		this.storageTime = storageTime;
		this.tbSensitiveTopics = tbSensitiveTopics;
		this.tbClusterTopics = tbClusterTopics;
		this.tbReBbses = tbReBbses;
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

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public String getSpTopic() {
		return this.spTopic;
	}

	public void setSpTopic(String spTopic) {
		this.spTopic = spTopic;
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

	public Set getTbClusterTopics() {
		return this.tbClusterTopics;
	}

	public void setTbClusterTopics(Set tbClusterTopics) {
		this.tbClusterTopics = tbClusterTopics;
	}

	public Set getTbReBbses() {
		return this.tbReBbses;
	}

	public void setTbReBbses(Set tbReBbses) {
		this.tbReBbses = tbReBbses;
	}

}