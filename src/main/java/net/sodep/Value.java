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

    protected Object content;
    private Map<String, List<Value>> children;

    public Value(Object content) {
        this.content = content;
    }

    public Value() {
        this.content = null;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(Object value) {
        this.content = value;
    }

    public Map<String, List<Value>> getChildren() {
        if (this.children == null)
            return new HashMap<>();

        return new HashMap<>(this.children);
    }

    public int getChildrenCount() {
        if (this.children == null)
            return 0;

        return this.children.size();
    }

    public void setChildren(String childName, List<Value> children) {
        if (this.children == null)
            this.children = new HashMap<>();

        this.children.put(childName, children);
    }

    public void setChild(String childName, Value child) {
        List<Value> values = new ArrayList<>();
        values.add(child);
        this.setChildren(childName, values);
    }

    public List<Value> getChildrenWithName(String name) {
        if (this.children == null)
            return new ArrayList<>();

        return new ArrayList<>(this.children.get(name));
    }

    public Value getFirstChildWithName(String name) {
        return this.children.get(name).get(0);
    }

}
