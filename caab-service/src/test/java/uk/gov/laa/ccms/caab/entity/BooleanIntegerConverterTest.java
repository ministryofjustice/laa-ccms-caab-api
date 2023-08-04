package uk.gov.laa.ccms.caab.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.laa.ccms.api.entity.BooleanIntegerConverter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BooleanIntegerConverterTest {

    @InjectMocks
    BooleanIntegerConverter booleanIntegerConverter;

    @Test
    public void testConvertDbToEntity_1() {
        assertTrue(booleanIntegerConverter.convertToEntityAttribute(1));
    }

    @Test
    public void testConvertDbToEntity_0() {
        assertFalse(booleanIntegerConverter.convertToEntityAttribute(0));
    }

    @Test
    public void testConvertDbToEntity_AnyOtherInteger() {
        assertFalse(booleanIntegerConverter.convertToEntityAttribute(2));
    }

    @Test
    public void testConvertDbToEntity_NULL() {
        assertNull(booleanIntegerConverter.convertToEntityAttribute(null));
    }

    @Test
    public void testEntityToDb_true() {
        assertEquals(Integer.valueOf(1), booleanIntegerConverter.convertToDatabaseColumn(Boolean.TRUE));
    }

    @Test
    public void testEntityToDb_false() {
        assertEquals(Integer.valueOf(0), booleanIntegerConverter.convertToDatabaseColumn(Boolean.FALSE));
    }

    @Test
    public void testEntityToDb_NULL() {
        assertNull(booleanIntegerConverter.convertToDatabaseColumn(null));
    }

}