package com.deckerpw.launchpadx;

import javax.sound.midi.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class MidiHelper {

    public static byte[] assembleSysEx(byte[] msg, boolean useD) {
        byte[] rv = new byte[6 + msg.length];
        System.arraycopy(new byte[]{(byte) 0xF0, 0x00, 0x20, 0x29, 0x02, (byte) (useD ? 0x0D : 0x0C)}, 0, rv, 0, 5);
        System.arraycopy(msg, 0, rv, 5, msg.length);
        rv[rv.length - 1] = (byte) 0xF7;
        return rv;
    }

    public static void sendMessage(MidiDevice device, int... message) throws MidiUnavailableException, InvalidMidiDataException {
        sendMessage(device, toByteArray(message));
    }

    public static void sendMessage(MidiDevice device, byte... message) throws MidiUnavailableException, InvalidMidiDataException {
        Receiver receiver = device.getReceiver();
        SysexMessage sysMsg = new SysexMessage();
        sysMsg.setMessage(message, message.length);
        receiver.send(sysMsg, -1);
    }

    public static void sendShortMessage(MidiDevice device, int data1, int data2, int data3) throws InvalidMidiDataException, MidiUnavailableException {
        ShortMessage message = new ShortMessage();
        message.setMessage(data1,data2,data3);
        sendShortMessage(device,message);
    }

    public static void sendShortMessage(MidiDevice device, ShortMessage message) throws MidiUnavailableException {
        Receiver receiver = device.getReceiver();
        receiver.send(message, -1);
    }

    public static void sendScrollText(MidiDevice device, boolean loop, byte speed, Color color, String text) throws MidiUnavailableException, InvalidMidiDataException {
        byte[] msg = new byte[15 + text.length()];
        System.arraycopy(new byte[]{(byte) 0xF0, 0x00, 0x20, 0x29, 0x02, 0x0C, 0x07, (byte) (loop ? 1 : 0), speed, 0x01, (byte) (color.getRed() / 2 - 1), (byte) (color.getGreen() / 2 - 1), (byte) (color.getBlue() / 2 - 1)}, 0, msg, 0, 11);
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(textBytes, 0, msg, 13, textBytes.length);
        msg[msg.length - 1] = (byte) 0xF7;
        sendMessage(device, msg);
    }

    private static byte[] toByteArray(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return bytes;
    }

}
