package com.rongke.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @Channel
 * @(channel)
 * @version : Ver 1.0
 */
@TableName("channel")
public class Channel implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * @备注:主键id
	 * @字段:id BIGINT(19)
	 */
	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * @备注:登录账号
	 * @字段:login_name VARCHAR(255)
	 */
	private String loginName;


	/**
	 * @备注:渠道商名称
	 * @字段:name VARCHAR(255)
	 */
	private String name;


	/**
	 * @备注:推广链接
	 * @字段:link_url VARCHAR(255)
	 */
	private String linkUrl;


	/**
	 * @备注:每单分成利润比例
	 * @字段:proportion VARCHAR(255)
	 */
	private String proportion;


	/**
	 * @备注:登录密码
	 * @字段:password VARCHAR(255)
	 */
	private String password;


	/**
	 * @备注:登录token
	 * @字段:token VARCHAR(255)
	 */
	private String token;


	/**
	 * @备注:总注册会员数
	 * @字段:member_count INT(10)
	 */
	private Integer memberCount;


	/**
	 * @备注:总申请金额
	 * @字段:apply_money VARCHAR(255)
	 */
	private String applyMoney;


	/**
	 * @备注:总放款金额
	 * @字段:out_money VARCHAR(255)
	 */
	private String outMoney;


	/**
	 * @备注:总分成利润
	 * @字段:profit VARCHAR(255)
	 */
	private String profit;


	/**
	 * @备注:状态: 1,正常 2,删除
	 * @字段:status INT(10)
	 */
	private Integer status;

	/**
	 * 放款人数
	 */
	@TableField(exist = false)
	private Integer loanUserCount;

	//扣量比例0-100
	private int decrementRate;

	public Channel(){
	}

	public Channel(
		Long id
	){
		this.id = id;
	}

	public Integer getLoanUserCount() {
		return loanUserCount;
	}

	public void setLoanUserCount(Integer loanUserCount) {
		this.loanUserCount = loanUserCount;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginName() {
		return this.loginName;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}


	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkUrl() {
		return this.linkUrl;
	}


	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	public String getProportion() {
		return this.proportion;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}


	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}


	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	public Integer getMemberCount() {
		return this.memberCount;
	}


	public void setApplyMoney(String applyMoney) {
		this.applyMoney = applyMoney;
	}

	public String getApplyMoney() {
		return this.applyMoney;
	}


	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}

	public String getOutMoney() {
		return this.outMoney;
	}


	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getProfit() {
		return this.profit;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public int getDecrementRate() {
		return decrementRate;
	}

	public void setDecrementRate(int decrementRate) {
		this.decrementRate = decrementRate;
	}
}
