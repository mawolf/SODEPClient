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

public class Fault {
    private String faultName;
    private Value additionalInfo;

    public Fault(String faultName) {
        this.faultName = faultName;
    }

    public Fault(String faultName, Value additionalInfo) {
        this.faultName = faultName;
        this.additionalInfo = additionalInfo;
    }

    public String getFaultName() {
        return this.faultName;
    }

    public Value getAdditionalInfo() {
        return this.additionalInfo;
    }
}
