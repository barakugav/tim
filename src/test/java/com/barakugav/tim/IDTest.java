package com.barakugav.tim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.barakugav.tim.ID;

class IDTest {

    @Test
    void newIDTemplate() {
	ID.newID("table1", ID.Type.Template);
    }

    @Test
    void newIDInstance() {
	ID.newID("table1", ID.Type.Instance);
    }

    @Test
    void newIDTypeNull() {
	assertThrows(NullPointerException.class, () -> ID.newID("table1", null));
    }

    @Test
    void newIDTableNameNull() {
	assertThrows(NullPointerException.class, () -> ID.newID(null, ID.Type.Template));
    }

    @Test
    void getTableName() {
	String name = "table1";
	ID id = ID.newID(name, ID.Type.Template);
	assertEquals(name, id.getTableName());
    }

    @Test
    void toStringAndFromStringTableName() throws ParseException {
	String name = "table1";
	ID id1 = ID.newID(name, ID.Type.Template);
	ID id2 = ID.valueOf(id1.toString());
	assertEquals(name, id2.getTableName());
    }

    @Test
    void toStringAndFromStringEquals() throws ParseException {
	ID id1 = ID.newID("table1", ID.Type.Template);
	ID id2 = ID.valueOf(id1.toString());
	assertEquals(id1, id2);
    }

    @Test
    void toStringAndFromStringEqualsHashCode() throws ParseException {
	ID id1 = ID.newID("table1", ID.Type.Template);
	ID id2 = ID.valueOf(id1.toString());
	assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toStringAndFromStringEqualsToString() throws ParseException {
	ID id1 = ID.newID("table1", ID.Type.Template);
	ID id2 = ID.valueOf(id1.toString());
	assertEquals(id1.toString(), id2.toString());
    }

}
