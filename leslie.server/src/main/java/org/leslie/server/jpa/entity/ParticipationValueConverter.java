package org.leslie.server.jpa.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;

@Converter
public class ParticipationValueConverter implements AttributeConverter<ParticipationLevel, Integer> {

	private static final int VALUE_NONE = 0;
	private static final int VALUE_VIEWER = 10;
	private static final int VALUE_MEMBER = 20;
	private static final int VALUE_MANAGER = 30;

	@Override
	public Integer convertToDatabaseColumn(ParticipationLevel attribute) {
		int value;
		switch (attribute) {
		case MANAGER:
			value = VALUE_MANAGER;
			break;
		case MEMBER:
			value = VALUE_MEMBER;
			break;
		case VIEWER:
			value = VALUE_VIEWER;
			break;
		case NONE:
		default:
			value = VALUE_NONE;
			break;
		}
		return Integer.valueOf(value);
	}

	@Override
	public ParticipationLevel convertToEntityAttribute(Integer dbData) {
		ParticipationLevel value;
		switch (dbData.intValue()) {
		case VALUE_MANAGER:
			value = ParticipationLevel.MANAGER;
			break;
		case VALUE_MEMBER:
			value = ParticipationLevel.MEMBER;
			break;
		case VALUE_VIEWER:
			value = ParticipationLevel.VIEWER;
			break;
		case VALUE_NONE:
		default:
			value = ParticipationLevel.NONE;
			break;
		}
		return value;
	}

}
