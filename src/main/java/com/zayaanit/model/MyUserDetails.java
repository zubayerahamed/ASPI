package com.zayaanit.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

	private String xprofile;
	private boolean switchBusiness;
	private Zbusiness zbusiness;

	private boolean accountExpired;
	private boolean credentialExpired;
	private boolean accountLocked;
	private boolean enabled;
	private String roles;
	private List<GrantedAuthority> authorities;

	public MyUserDetails(Xusers user) {
		this.zemail = user.getZemail();
		this.xpassword = user.getXpassword();
		this.admin = Boolean.TRUE.equals(user.getZadmin());
		this.xstaff = user.getXstaff();
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

	public void setZbusiness(Zbusiness zbusiness) {
		this.zbusiness = zbusiness;
	}

	public Zbusiness getZbusiness() {
		return zbusiness;
	}

	public void setXprofile(String xprofile) {
		this.xprofile = xprofile;
	}

	public String getXprofile() {
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

}
