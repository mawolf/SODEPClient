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
public class SODEPMessage {

    private Long messageId;
    private String resourcePath;
    private String operationName;

    private Fault fault;
    private Value value;

    public SODEPMessage(Long messageId, String resourcePath,
                        String operationName, Fault fault, Value value) {
        this.messageId = messageId;
        this.resourcePath = resourcePath;
        this.operationName = operationName;
        this.fault = fault;
        this.value = value;
    }

    public Fault getFault() {
        return fault;
    }

    public Value getValue() {
        return value;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public Long getMessageId() {
        return messageId;
    }
}
