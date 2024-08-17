package com.zayaanit.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.zayaanit.util.CKTime;

/**
 * @author Zubayer Ahamed
 * @since Apr 24, 2024
 */
@Convert
public class CKTimeConverter implements AttributeConverter<CKTime, String> {

	@Override
	public String convertToDatabaseColumn(CKTime time) {
		return time == null ? null : time.getT5Time();
	}

	@Override
	public CKTime convertToEntityAttribute(String data) {
		return data == null || data.isEmpty() ? null : new CKTime(data);
	}
}
