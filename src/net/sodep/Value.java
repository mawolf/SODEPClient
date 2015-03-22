package net.sodep;

/*
 * Copyright (C) 2015 Martin Wolf
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of  MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Value {

    protected Object value;
    protected Map<String, List<Value>> children = new HashMap<String, List<Value>>();

    public Value(Object value) {
        this.value = value;
    }

    public Value() {
        this.value = null;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, List<Value>> children() {
        return this.children;
    }

    public void setChildren(String childName, List<Value> children) {
        this.children.put(childName,children);
    }
}
