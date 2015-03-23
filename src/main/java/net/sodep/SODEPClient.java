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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for the SODEP protocol used in Jolie
 * <p/>
 * This implementation is with blocking sockets.
 */
public class SODEPClient {

    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    /**
     * Connect to specified host
     *
     * @param hostname The host name
     * @param port     the port number
     * @param timeout  The timeout value to be used in milliseconds.
     *                 A timeout of zero is interpreted as an infinite timeout.
     * @throws IOException if an error occurs during the connection
     */
    public void connect(String hostname, int port, int timeout) throws IOException {
        this.clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(hostname, port), timeout);
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Read a string
     * <p/>
     * Defined as
     * String ::= int(string length) string(UTF-8 encoded)
     *
     * @return the string read
     * @throws IOException if an error occurs during reading
     */
    private String readString() throws IOException {
        int length = this.dataInputStream.readInt();

        byte[] buffer = new byte[length];
        this.dataInputStream.readFully(buffer);
        return new String(buffer, "UTF-8");
    }

    /**
     * Read a fault if found
     * <p/>
     * Defined as
     * Fault ::= true String(fault name) Value(fault additional data) | false
     *
     * @return if there is a fault, then Fault object returned, if not, then null
     * @throws IOException
     */
    private Fault readFault() throws IOException {
        if (!this.dataInputStream.readBoolean()) {
            return null;
        }

        String faultName = this.readString();
        Value additionalFaultData = this.readValue();
        return new Fault(faultName, additionalFaultData);
    }

    /**
     * Read a Value
     * <p/>
     * Defined as
     * Value ::= ValueContent int(how many ValueChildren) ValueChildren*
     * ValueChildren ::= String(child name) int(how many Value) Value* | epsilon
     *
     * @return a Node object with read values
     * @throws IOException
     */
    private Value readValue() throws IOException {
        //value content
        Value result = new Value(readValueContent());

        //how many ValueChildren
        int childrenCount = this.dataInputStream.readInt();

        //ValueChildren*
        for (int i = 0; i < childrenCount; i++) {
            //child name
            String childName = readString();

            //how many Value
            int valuesCount = this.dataInputStream.readInt();

            //Value*
            List<Value> children = new ArrayList<Value>();
            for (int j = 0; j < valuesCount; j++) {
                children.add(readValue());
            }
            result.setChildren(childName, children);
        }
        return result;
    }

    /**
     * Read content of the value
     * <p/>
     * Defined as
     * ValueContent ::= 0(byte) | 1(byte) String | 2(byte) int | 3(byte) double
     *
     * @return Object with content
     * @throws IOException
     */
    private Object readValueContent() throws IOException {
        switch (this.dataInputStream.readByte()) {
            case 0:
                return null;
            case 1:
                return this.readString();
            case 2:
                return this.dataInputStream.readInt();
            case 3:
                return this.dataInputStream.readDouble();
            default:
                throw new IOException("Type not supported");
        }
    }

    /**
     * Read message
     * <p/>
     * Defined as
     * SODEPMessage ::= long(message id) String(resource path) String(operation name) Fault Value
     *
     * @throws IOException
     */
    public SODEPMessage readMessage() throws IOException {
        Long messageId = this.dataInputStream.readLong();
        String resourcePath = this.readString();
        String operationName = this.readString();

        Fault fault = this.readFault();
        Value value = this.readValue();

        return new SODEPMessage(messageId, resourcePath, operationName, fault, value);
    }

    public void writeMessage(SODEPMessage message) throws IOException {
        this.dataOutputStream.writeLong(message.getMessageId());
        this.writeString(message.getResourcePath());
        this.writeString(message.getOperationName());
        this.writeFault(message.getFault());
        this.writeValue(message.getValue());
    }

    private void writeFault(Fault fault) throws IOException {
        if (fault == null) {
            this.dataOutputStream.writeBoolean(false);
            return;
        }

        this.dataOutputStream.writeBoolean(true);
        this.writeString(fault.getFaultName());
        this.writeValue(fault.getAdditionalInfo());
    }

    /**
     * Write a Value
     * <p/>
     * Defined as
     * Value ::= ValueContent int(how many ValueChildren) ValueChildren*
     * ValueChildren ::= String(child name) int(how many Value) Value* | epsilon
     *
     * @param value Value object to write
     */
    private void writeValue(Value value) throws IOException {
        this.writeValueContent(value.getValue());
        int childrenCount = value.children.size();

        //how many ValueChildren
        this.dataOutputStream.writeInt(childrenCount);

        //for each ValueChild
        for ( String childName : value.children.keySet()) {
            this.writeString(childName);
            List<Value> children = value.children.get(childName);
            this.dataOutputStream.writeInt(children.size());
            for ( Value childValue : children ) {
                this.writeValue(childValue);
            }
        }
    }

    private void writeValueContent(Object content) throws IOException {
        if (content == null) {
            this.dataOutputStream.writeByte(0);
        } else if (content instanceof String) {
            this.dataOutputStream.writeByte(1);
            this.writeString((String) content);
        } else if (content instanceof Integer) {
            this.dataOutputStream.writeByte(2);
            this.dataOutputStream.writeInt((Integer) content);
        } else if (content instanceof Double) {
            this.dataOutputStream.writeByte(3);
            this.dataOutputStream.writeDouble((Double) content);
        } else {
            throw new IOException("Unknown type.");
        }
    }


    private void writeString(String string) throws IOException {
        byte[] stringBytes = string.getBytes("UTF-8");
        this.dataOutputStream.writeInt(stringBytes.length);
        this.dataOutputStream.write(stringBytes);
    }

    /**
     * Close connection
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (this.clientSocket != null)
            this.clientSocket.close();

        this.clientSocket = null;
    }

}
