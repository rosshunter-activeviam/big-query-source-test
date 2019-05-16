/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam Ltd. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */

package com.activeviam.training.cells;

import com.quartetfs.fwk.QuartetType;
import com.quartetfs.pivot.mdx.IDefaultCellPropertiesHandler;
import com.quartetfs.pivot.mdx.impl.DefaultCellPropertiesHandler;

@QuartetType(description = "Sets the fore color of negative numeric value to red", intf = IDefaultCellPropertiesHandler.class)
public class CellPropertiesHandler extends DefaultCellPropertiesHandler {
	/**
	 * The fore color is red if the value is negative, unspecified otherwise
	 * (e.g. the GUI can choose its preferred color based on its theme).
	 */
	@Override
	protected Integer getForeColor(Object cellValue) {
		if (cellValue instanceof Number) {
			if (((Number) cellValue).doubleValue() < 0) {
				// You have to reverse the order of the string
				return 0x8787ff;
			}
		}
		return null;
	}
}

