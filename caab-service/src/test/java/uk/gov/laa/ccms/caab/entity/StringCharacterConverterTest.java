package uk.gov.laa.ccms.caab.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StringCharacterConverterTest {

    @InjectMocks
    StringCharacterConverter stringCharacterConverter;

    @Test
    public void testConvertDbToEntity_Y() {
        assertEquals("true", stringCharacterConverter.convertToEntityAttribute('Y'));
    }

    @Test
    public void testConvertDbToEntity_N() {
        assertEquals("false", stringCharacterConverter.convertToEntityAttribute('N'));
    }

    @Test
    public void testConvertDbToEntity_InvalidCharacter() {
        assertThrows(IllegalArgumentException.class, () -> stringCharacterConverter.convertToEntityAttribute('Z'));
    }

    @Test
    public void testConvertDbToEntity_NULL() {
        assertNull(stringCharacterConverter.convertToEntityAttribute(null));
    }

    @Test
    public void testEntityToDb_true() {
        assertEquals(Character.valueOf('Y'), stringCharacterConverter.convertToDatabaseColumn("true"));
    }

    @Test
    public void testEntityToDb_false() {
        assertEquals(Character.valueOf('N'), stringCharacterConverter.convertToDatabaseColumn("false"));
    }

    @Test
    public void testEntityToDb_InvalidString() {
        assertThrows(IllegalArgumentException.class, () -> stringCharacterConverter.convertToDatabaseColumn("invalid"));
    }

    @Test
    public void testEntityToDb_NULL() {
        assertNull(stringCharacterConverter.convertToDatabaseColumn(null));
    }

}