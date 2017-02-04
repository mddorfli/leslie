package org.leslie.server.jpa.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;

@Converter
public class ParticipationIntegerConverter implements AttributeConverter<ParticipationLevel, Integer> {

    private static final int VALUE_NONE = 0;
    private static final int VALUE_VIEWER = 10;
    private static final int VALUE_MEMBER = 20;
    private static final int VALUE_MANAGER = 30;

    @Override
    public Integer convertToDatabaseColumn(ParticipationLevel attribute) {
	switch (attribute) {
	case MANAGER:
	    return VALUE_MANAGER;
	case MEMBER:
	    return VALUE_MEMBER;
	case VIEWER:
	    return VALUE_VIEWER;
	case NONE:
	default:
	    return VALUE_NONE;
	}
    }

    @Override
    public ParticipationLevel convertToEntityAttribute(Integer dbData) {
	switch (dbData.intValue()) {
	case VALUE_MANAGER:
	    return ParticipationLevel.MANAGER;
	case VALUE_MEMBER:
	    return ParticipationLevel.MEMBER;
	case VALUE_VIEWER:
	    return ParticipationLevel.VIEWER;
	case VALUE_NONE:
	default:
	    return ParticipationLevel.NONE;
	}
    }

}
