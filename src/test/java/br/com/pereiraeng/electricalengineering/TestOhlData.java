package br.com.pereiraeng.electricalengineering;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.pereiraeng.electricalengineering.cable.OHLdata;

public class TestOhlData {

	@Test
	void testOhlGetCurrent() {
		// AAC Iris
		int current = OHLdata.getCurrent(0, 3);
		assertEquals(208, current);
	}

	@Test
	void testOhlGetCablingDescrition() {
		String description = OHLdata.getCablingDescription("1;9");
		assertEquals("Penguin-107,22mm\u00B2", description);
	}
}
