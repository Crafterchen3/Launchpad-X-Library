package com.deckerpw.launchpadx;

import com.deckerpw.launchpadx.api.*;

import javax.sound.midi.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class Playground {

    private static boolean mode;

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        String iName = "MIDIIN2 (LPX MIDI)";
        String oName = "MIDIOUT2 (LPX MIDI)";

        Launchpad launchpad = new Launchpad(oName, iName);
        launchpad.setButtonLight(new Point(8, 7), PalleteColor.BLUE, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 6), PalleteColor.RED, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 5), PalleteColor.RED, LightEffect.STATIC);
        launchpad.setButtonRGB(new Point(8, 8), Color.CYAN);

        mode = true;

        launchpad.addButtonListener(new Rectangle(0, 0, 8, 8), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                try {
                    event.launchpad.setButtonLight(event.buttonPos, new PalleteColor(69), LightEffect.STATIC);
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {
                try {
                    event.launchpad.setButtonLight(event.buttonPos, mode ? PalleteColor.PURPLE : PalleteColor.BLACK, LightEffect.PULSING);
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        launchpad.addButtonListener(new Point(8, 7), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                mode = !mode;
                try {
                    launchpad.setButtonLight(new Point(8, 7), mode ? PalleteColor.BLUE : new PalleteColor(41), LightEffect.STATIC);
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
        launchpad.addButtonListener(new Point(8, 6), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Color color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
                        try {
                            event.launchpad.setButtonRGB(new Point(x, y), color);
                        } catch (MidiUnavailableException | InvalidMidiDataException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
        launchpad.addButtonListener(new Point(8, 5), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                try {
                    event.launchpad.sendText("Hello World!");
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
//
//        //iName = oName = "LPX MIDI";
//        MidiDevice iDev = null, oDev = null;
//        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
//        for (MidiDevice.Info info : infos) {
//            System.out.println(info.getName());
//            if (info.getName().equals(iName) && iDev == null) {
//                iDev = MidiSystem.getMidiDevice(info);
//                continue;
//            }
//            if (info.getName().equals(oName) && oDev == null) {
//                oDev = MidiSystem.getMidiDevice(info);
//                continue;
//            }
//        }
//
//        assert oDev != null;
//        oDev.open();
//        assert iDev != null;
//        iDev.open();
//
//        enterDAWMode(oDev, false);
//
//        receiveDAW(iDev);
//
        //        enterProgrammerMode(oDev,true);
//
//        ShortMessage message = new ShortMessage();
//        message.setMessage(0x90,99, 0x20);
//        MidiHelper.sendMessage(oDev,new byte[]{(byte) 0xF0,0x00 ,0x20 ,0x29 ,0x02 ,0x0C ,0x03,0x02, 12, 0x01, (byte) 0xF7});
//
//        //MidiHelper.sendScrollText(oDev,false, (byte) 0x39,Color.RED,"The Launchpad X can display scrolling text across the pads and the right-side buttons. The top row buttons and the logo is not affected by the scroll, and any lighting covered by the scroll is retained (will return once the scroll is stopped or completed).");
//
//        System.out.println("finish");
    }

}
