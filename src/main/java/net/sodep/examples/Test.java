package net.sodep.examples;

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

import net.sodep.SODEPClient;
import net.sodep.SODEPMessage;
import net.sodep.Value;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        SODEPClient sodepClient = new SODEPClient();
        try {
            System.out.print("Connecting...");
            sodepClient.connect("127.0.0.1", 8001, 0);
            System.out.println(" done.");

            System.out.print("Sending message...");
            SODEPMessage message = new SODEPMessage((long) 1, "/", "operation", null, new Value("hello"));
            sodepClient.writeMessage(message);
            System.out.println("done.");

            System.out.print("Receiving message...");
            SODEPMessage received = sodepClient.readMessage();
            System.out.println("done.");

            System.out.println("Received: " + received.getValue().getContent());
        } finally {
            sodepClient.close();
        }
    }

}
