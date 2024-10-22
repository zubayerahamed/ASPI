package com.zayaanit.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;

import lombok.ToString;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@ToString
public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = -6787379046214364380L;

	private String zemail;
	private String xpassword;
	private boolean admin;
	private Integer xstaff;
	private String xtheme;
	private Integer xsessiontime;
	private String xsessiontype;

	private boolean switchBusiness;
	private Zbusiness zbusiness;
	private Xprofiles xprofile;

	private boolean accountExpired;
	private boolean credentialExpired;
	private boolean accountLocked;
	private boolean enabled;
	private String roles;
	private List<GrantedAuthority> authorities;
	private Date loginTime;

	public MyUserDetails(Xusers user) {
		this.loginTime = new Date();
		this.zemail = user.getZemail();
		this.xpassword = user.getXpassword();
		this.admin = Boolean.TRUE.equals(user.getZadmin());
		this.xstaff = user.getXstaff();
		this.xtheme = user.getXtheme();
		this.xsessiontime = user.getXsessiontime();
		this.xsessiontype = user.getXsessiontype();
		this.switchBusiness = Boolean.TRUE.equals(user.getZadmin()) ? true : false;
		this.accountExpired = false;
		this.credentialExpired = false;
		this.accountLocked = !Boolean.TRUE.equals(user.getZactive());
		this.enabled = Boolean.TRUE.equals(user.getZactive());
		this.roles = StringUtils.isBlank(user.getRoles()) ? com.zayaanit.enums.UserRole.SUBSCRIBER.getCode()
				: user.getRoles();
		this.authorities = Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public void setUserDetails(Xusers user) {
		this.zemail = user.getZemail();
		this.xpassword = user.getXpassword();
		this.admin = Boolean.TRUE.equals(user.getZadmin());
		this.xstaff = user.getXstaff();
		this.xtheme = user.getXtheme();
		this.xsessiontime = user.getXsessiontime();
		this.xsessiontype = user.getXsessiontype();
		this.switchBusiness = Boolean.TRUE.equals(user.getZadmin()) ? true : false;
		this.accountExpired = false;
		this.credentialExpired = false;
		this.accountLocked = !Boolean.TRUE.equals(user.getZactive());
		this.enabled = Boolean.TRUE.equals(user.getZactive());
		this.roles = StringUtils.isBlank(user.getRoles()) ? com.zayaanit.enums.UserRole.SUBSCRIBER.getCode()
				: user.getRoles();
		this.authorities = Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date date) {
		this.loginTime = date;
	}
	
	public void setZbusiness(Zbusiness zbusiness) {
		this.zbusiness = zbusiness;
	}

	public Zbusiness getZbusiness() {
		return zbusiness;
	}

	public void setXprofile(Xprofiles xprofile) {
		this.xprofile = xprofile;
	}

	public Xprofiles getXprofile() {
		return this.xprofile;
	}

	public Integer getXstaff() {
		return this.xstaff;
	}

	public void setSwitchBusiness(boolean status) {
		this.switchBusiness = this.isAdmin() == true ? true : status;
	}

	public boolean isSwitchBusiness() {
		return this.switchBusiness;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public String getRoles() {
		return this.roles;
	}

	@Override
	public String getPassword() {
		return this.xpassword;
	}

	@Override
	public String getUsername() {
		return this.zemail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !credentialExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public boolean isAdmin() {
		return admin;
	}

	public String getXtheme() {
		return xtheme;
	}

	public void setXtheme(String xtheme) {
		this.xtheme = xtheme;
	}

	public Integer getXsessiontime() {
		if("Default".equalsIgnoreCase(this.xsessiontype)) {
			if(this.zbusiness != null && this.zbusiness.getXsessiontime() != null) {
				this.xsessiontime = zbusiness.getXsessiontime();
			}
		}
		return this.xsessiontime;
	}

	public Date getXsessionexpiry() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, this.xsessiontime);
		return cal.getTime();
	}

	public void setXsessiontime(Integer xsessiontime) {
		this.xsessiontime = xsessiontime;
	}

	public String getXsessiontype() {
		return xsessiontype;
	}

	public void setXsessiontype(String xsessiontype) {
		this.xsessiontype = xsessiontype;
	}
}
