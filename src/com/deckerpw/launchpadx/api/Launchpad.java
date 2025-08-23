package com.deckerpw.launchpadx.api;

import com.deckerpw.launchpadx.MidiHelper;

import javax.sound.midi.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Launchpad {

    public final Map<Point, PadButtonListener> listenerMap = new HashMap<>();
    private MidiDevice inputDevice;
    private MidiDevice outputDevice;

    public Launchpad(String midiIn, String midiOut) throws MidiUnavailableException, InvalidMidiDataException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            System.out.println(info.getName());
            if (info.getName().equals(midiOut) && inputDevice == null) {
                inputDevice = MidiSystem.getMidiDevice(info);
                continue;
            }
            if (info.getName().equals(midiIn) && outputDevice == null) {
                outputDevice = MidiSystem.getMidiDevice(info);
                continue;
            }
        }
        assert outputDevice != null;
        outputDevice.open();
        assert inputDevice != null;
        inputDevice.open();

        enterProgrammerMode(true);

        receive();
    }

    private void enterProgrammerMode(boolean mode) throws MidiUnavailableException, InvalidMidiDataException {
        MidiHelper.sendMessage(outputDevice, new byte[]{(byte) 0xF0, 0x00, 0x20, 0x29, 0x02, 0x0C, 0x0E, (byte) (mode ? 1 : 0), (byte) 0xF7});
        MidiHelper.sendMessage(outputDevice, new byte[]{(byte) 0xF0, 0x00, 0x20, 0x29, 0x02, 0x0D, 0x0E, (byte) (mode ? 1 : 0), (byte) 0xF7});
    }


    private void receive() throws MidiUnavailableException {
        Transmitter transmitter = inputDevice.getTransmitter();
        transmitter.setReceiver(new Receiver() {
            public void send(MidiMessage message, long timeStamp) {
                boolean pressed = message.getMessage()[0] == -96 || (message.getMessage()[0] == -80 && message.getMessage()[2] == 127);
                int button = message.getMessage()[1];
                Point point = new Point(button % 10 - 1, button / 10 - 1);
                int force = message.getMessage()[2];
                System.out.println(Arrays.toString(message.getMessage()));
                if (listenerMap.containsKey(point)) {
                    PadButtonListener listener = listenerMap.get(point);
                    PadButtonEvent event = new PadButtonEvent(Launchpad.this, point, force);
                    if (pressed)
                        listener.onPressed(event);
                    else
                        listener.onReleased(event);
                }
            }

            public void close() {
            }
        });
    }

    public void addButtonListener(Point buttonPos, PadButtonListener listener) {
        listenerMap.put(buttonPos, listener);
    }

    public void addButtonListener(Point[] buttonPosList, PadButtonListener listener) {
        for (Point pos : buttonPosList) {
            listenerMap.put(pos, listener);
        }
    }

    public void addButtonListener(Rectangle2D rectangle2D, PadButtonListener listener) {
        for (int x = 0; x < rectangle2D.getWidth(); x++) {
            for (int y = 0; y < rectangle2D.getHeight(); y++) {
                listenerMap.put(new Point((int) (x + rectangle2D.getX()), (int) (y + rectangle2D.getY())), listener);
            }
        }
    }

    public void addButtonListener(PadButtonListener listener) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Point pos = new Point(x, y);
                listenerMap.put(pos, listener);
            }
        }
    }

    public void removeButtonListener(PadButtonListener listener) {
        for (Map.Entry<Point, PadButtonListener> listenerEntry : listenerMap.entrySet()) {
            if (listenerEntry.getValue() == listener)
                listenerMap.remove(listenerEntry.getKey());
        }
    }

    public void removeButtonListener(Point pos) {
        listenerMap.remove(pos);
    }

    public void setButtonLight(Point pos, PalleteColor color, LightEffect effect) throws MidiUnavailableException, InvalidMidiDataException {
        switch (effect) {
            case STATIC -> MidiHelper.sendMessage(outputDevice, 0x90, parsePoint(pos), color.color);
            case FLASHING -> MidiHelper.sendMessage(outputDevice, 0x91, parsePoint(pos), color.color);
            case PULSING -> MidiHelper.sendMessage(outputDevice, 0x92, parsePoint(pos), color.color);
        }
    }

    public void setButtonRGB(Point pos, Color color) throws MidiUnavailableException, InvalidMidiDataException {
        MidiHelper.sendMessage(outputDevice, new byte[]{(byte) 0xF0, 0x00, 0x20, 0x29, 0x02, 0x0C, 0x03, 0x03, (byte) parsePoint(pos), (byte) (color.getRed() / 2), (byte) (color.getGreen() / 2), (byte) (color.getBlue() / 2), (byte) 0xF7});
        //MidiHelper.sendMessage(outputDevice,new byte[]{(byte) 0xF0,0x00 ,0x20 ,0x29 ,0x02 ,0x0D ,0x03,0x03, (byte) parsePoint(pos), (byte) (color.getRed()/2), (byte) (color.getGreen()/2), (byte) (color.getBlue()/2), (byte) 0xF7});
    }

    private int parsePoint(Point pos) {
        return pos.x + 1 + (pos.y + 1) * 10;
    }

}
