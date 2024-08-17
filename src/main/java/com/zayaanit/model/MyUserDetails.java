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
	private Integer zid;
	private boolean admin;
	private Integer xstaff;
	private Zbusiness zbusiness;
	private String xprofile;
//	private Integer xwh;
	private Integer xarea;
	private Integer xcusview;
	private String employeeName;
	private boolean switchBusiness;
	private Integer purchaseOrg;
	private Integer salesOrg;
	private Integer inventoryOrg;
	

	private boolean accountExpired;
	private boolean credentialExpired;
	private boolean accountLocked;
	private boolean enabled;
	private String roles;
	private List<GrantedAuthority> authorities;

	public MyUserDetails(Xusers user, Zbusiness zbusiness) {
		this.zid = zbusiness.getZid();
		this.zemail = user.getZemail();
		this.xpassword = user.getXpassword();
		this.zbusiness = zbusiness;
		this.admin = Boolean.TRUE.equals(user.getZadmin());
		this.xstaff = user.getXstaff();
		this.xprofile = user.getXprofile();
//		this.xwh = user.getXwh();
		this.xarea = user.getXarea();
		this.xcusview = user.getXcusview();
		this.employeeName = user.getEmployee();
		if(user.getXswbusiness() == null) {
			this.switchBusiness = false;
		} else {
			this.switchBusiness = user.getXswbusiness();
		}
		this.purchaseOrg = user.getXorgpo();
		this.salesOrg = user.getXorgop();
		this.inventoryOrg = user.getXorgim();

		this.accountExpired = false;
		this.credentialExpired = false;
		this.accountLocked = !Boolean.TRUE.equals(user.getZactive());
		this.enabled = Boolean.TRUE.equals(user.getZactive());
		this.roles = StringUtils.isBlank(user.getRoles()) ? com.zayaanit.enums.UserRole.SUBSCRIBER.getCode() : user.getRoles();
		this.authorities = Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public Integer getDays() {
		return this.xcusview;
	}

	public Integer getSalesArea() {
		return this.xarea;
	}

//	public Integer getStore() {
//		return this.xwh;
//	}

	public void setZbusiness(Zbusiness zbusiness) {
		this.zbusiness = zbusiness;
	}

	public String getXprofile() {
		return this.xprofile;
	}
	
	public Integer getXstaff() {
		return this.xstaff;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getRoles() {
		return roles;
	}

	@Override
	public String getPassword() {
		return this.xpassword;
	}

	@Override
	public String getUsername() {
		return zemail;
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

	public Integer getBusinessId() {
		return zid;
	}

	public Zbusiness getZbusiness() {
		return zbusiness;
	}

	public boolean isAdmin() {
		return admin;
	}

	public boolean isSwitchBusiness() {
		return switchBusiness;
	}

	public Integer getPurchaseOrg() {
		return purchaseOrg;
	}

	public Integer getSalesOrg() {
		return salesOrg;
	}

	public Integer getInventoryOrg() {
		return inventoryOrg;
	}
}
