package com.zayaanit.service.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.service.XlogsdtService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Slf4j
@Service
public class XlogsdtServiceImpl implements XlogsdtService {

	@Autowired private JdbcTemplate jdbcTemplate;

	@Transactional
	@Override
	public void save(XlogsdtEvent event) {
		if (event.isAdmin()) return;
		if (!"Advance".equals(event.getXlogtype())) return;

		Date date = new Date();

		Xlogsdt xlogsdt = event.getXlogsdt();
		xlogsdt.setZid(event.getZid());
		xlogsdt.setXsession(event.getXsession());
		xlogsdt.setZemail(event.getZemail());
		xlogsdt.setXstaff(event.getXstaff());
		xlogsdt.setZtime(date);
		xlogsdt.setXdate(date);

		insertAndReturnId(xlogsdt);

		log.debug("Logs Details Saved : " + xlogsdt.toString());
	}

	public long insertAndReturnId(final Xlogsdt xlogsdt) {
		final String sql = "INSERT INTO xlogsdt (" + "xdata, xdate, xfunc, xresult, xscreen, "
				+ "xsession, xsource, xstaff, xstatement, xtable, " + "zemail, zid, ztime"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, xlogsdt.getXdata());
			ps.setDate(2, new java.sql.Date(xlogsdt.getXdate().getTime())); // java.util.Date -> java.sql.Date
			ps.setString(3, xlogsdt.getXfunc());
			ps.setString(4, xlogsdt.getXresult());
			ps.setString(5, xlogsdt.getXscreen());
			ps.setString(6, xlogsdt.getXsession());
			ps.setString(7, xlogsdt.getXsource());
			if (xlogsdt.getXstaff() != null) {
				ps.setInt(8, xlogsdt.getXstaff());
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}
			ps.setString(9, xlogsdt.getXstatement());
			ps.setString(10, xlogsdt.getXtable());
			ps.setString(11, xlogsdt.getZemail());
			ps.setInt(12, xlogsdt.getZid());
			ps.setTimestamp(13, new Timestamp(xlogsdt.getZtime().getTime())); // java.util.Date -> java.sql.Timestamp
			return ps;
		}, keyHolder);

		Number key = keyHolder.getKey();
		return key != null ? key.longValue() : 0L;
	}

}
