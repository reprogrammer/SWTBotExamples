/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package com.github.reprogrammer;

public class TestEncapsulateField {

    private Object field;
	
    void m() {
        System.out.println(field);
    }

	Object getField() {
		return field;
	}

	void setField(Object field) {
		this.field = field;
	}

}
