package com.zayaanit.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.ZbusinessRepo;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2024
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private XusersRepo xuserRepo;
	private ZbusinessRepo zbusinessRepo;
	private PdmstRepo pdmstRepo;

	public CustomAuthenticationProvider(ZbusinessRepo zbusinessRepo, XusersRepo xuserRepo, PdmstRepo pdmstRepo) {
		this.zbusinessRepo = zbusinessRepo;
		this.xuserRepo = xuserRepo;
		this.pdmstRepo = pdmstRepo;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		String[] token = username.split("\\|");
		if (token.length < 2)
			throw new UsernameNotFoundException("User not found in the system");

		String zemail = token[0];
		Integer zid = Integer.valueOf(token[1]);

		Optional<Xusers> opuser = xuserRepo.findByZemailAndZid(zemail, zid);
		if (!opuser.isPresent()) {
			throw new UsernameNotFoundException("User not found in the system");
		}

		Optional<Zbusiness> opzbusiness = zbusinessRepo.findByZidAndZactive(zid, Boolean.TRUE);
		if (!opzbusiness.isPresent()) {
			throw new UsernameNotFoundException("Your Business is currently inactive");
		}

		Xusers user = opuser.get();
		if(user != null && user.getXstaff() != null) {
			Optional<Pdmst> pdmstOp = pdmstRepo.findById(new PdmstPK(zid, user.getXstaff()));
			if(pdmstOp.isPresent()) {
				user.setEmployee(pdmstOp.get().getXname());
			}
		}

		UserDetails userDetails = new MyUserDetails(user, opzbusiness.get());

		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
