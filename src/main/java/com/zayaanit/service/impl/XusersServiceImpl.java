package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.ZbusinessRepo;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Service
public class XusersServiceImpl extends AbstractService implements XusersService, UserDetailsService {
	
	@Autowired private KitSessionManager sessionManager;

	@Autowired
	private XusersRepo xuserRepo;

	@Autowired
	private ZbusinessRepo zbusinessRepo;

	@Autowired
	private PdmstRepo pdmstRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("User not found in the system");
		}

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

		return new MyUserDetails(user, opzbusiness.get());
	}

	@Override
	public List<Xusers> LAD13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xusers u"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xusers> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LAD13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xusers u"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xusers constractListOfXscreens(Map<String, Object> row) {
		Xusers em = new Xusers();
		em.setZemail((String) row.get("zemail"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXprofile((String) row.get("xprofile"));
		em.setXarea((Integer) row.get("xarea"));
		em.setZactive((Boolean) row.get("zactive"));
		em.setZadmin((Boolean) row.get("zadmin"));
		em.setEmployee((String) row.get("employee"));
		em.setArea((String) row.get("area"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT u.*, staff.xname as employee, area.xname as area");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN pdmst staff ON staff.xstaff = u.xstaff AND staff.zid = u.zid ")
				.append(" LEFT JOIN oparea area ON area.xarea = u.xarea AND area.zid = u.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE u.zid="+sessionManager.getBusinessId()+" ");
		if (searchText == null || searchText.isEmpty())
			return sql;
		return sql.append(" AND (zemail LIKE '%" + searchText 
				+ "%' OR staff.xname LIKE '%" + searchText 
				+ "%' OR xprofile LIKE '%" + searchText 
				+ "%' OR area.xname LIKE '%" + searchText 
				+ "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
