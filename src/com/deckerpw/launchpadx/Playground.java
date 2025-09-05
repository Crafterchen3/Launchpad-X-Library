package com.deckerpw.launchpadx;

import com.deckerpw.launchpadx.api.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.io.IOException;

public class Playground {

    private static boolean mode;
    private static PalleteColor[] colors = new PalleteColor[]{
            new PalleteColor(5),
            new PalleteColor(13),
            new PalleteColor(21),
            new PalleteColor(29),
            new PalleteColor(37),
            new PalleteColor(45),
            new PalleteColor(54),
            new PalleteColor(3)
    };
    private static LightEffect effect = LightEffect.STATIC;
    private static int color = 3;

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        String iName = "MIDIIN2 (LPX MIDI)";
        String oName = "MIDIOUT2 (LPX MIDI)";

        Launchpad launchpad = new Launchpad(oName, iName);
        launchpad.setButtonLight(new Point(8, 7), PalleteColor.BLUE, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 6), PalleteColor.RED, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 5), PalleteColor.RED, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 4), PalleteColor.PURPLE, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 2), new PalleteColor(106), LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 1), PalleteColor.RED, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(8, 0), PalleteColor.GREEN, LightEffect.STATIC);
        launchpad.setButtonRGB(new Point(8, 8), Color.CYAN);
        refreshColorRow(launchpad);

        mode = true;

        launchpad.addButtonListener(new Rectangle(0, 0, 8, 8), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                try {
                    event.launchpad.setButtonLight(event.buttonPos, mode ? colors[color] : PalleteColor.BLACK, effect);
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {
                try {
                    event.launchpad.setButtonLight(event.buttonPos, mode ? colors[color] : PalleteColor.BLACK, effect);
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
                    event.launchpad.setButtonLight(new Rectangle(0, 0, 8, 8), mode ? colors[color] : PalleteColor.BLACK, effect);
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
        launchpad.addButtonListener(new Point(8, 4), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                int i = effect.ordinal() + 1;
                if (i == 3)
                    i = 0;
                effect = LightEffect.values()[i];
                try {
                    launchpad.setButtonLight(new Point(8, 4), PalleteColor.BLACK, LightEffect.STATIC);
                    launchpad.setButtonLight(new Point(8, 4), PalleteColor.PURPLE, effect);
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
        launchpad.addButtonListener(new Point(8, 1), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                try {
                    event.launchpad.stopText();
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
        launchpad.addButtonListener(new Point(8, 0), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                try {
                    event.launchpad.sendText(true, (byte) 0x05, Color.CYAN, "Hello World!");
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
        launchpad.addButtonListener(new Rectangle(0, 8, 8, 1), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                color = event.buttonPos.x;
                try {
                    refreshColorRow(event.launchpad);
                } catch (MidiUnavailableException | InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onReleased(PadButtonEvent event) {

            }
        });
        launchpad.addButtonListener(new Point(8,2), new PadButtonListener() {
            @Override
            public void onPressed(PadButtonEvent event) {
                try {
                    event.launchpad.destroy();
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

    private static void refreshColorRow(Launchpad launchpad) throws MidiUnavailableException, InvalidMidiDataException {
        for (int i = 0; i < 8; i++) {
            launchpad.setButtonLight(new Point(i, 8), colors[i], LightEffect.STATIC);
        }
        launchpad.setButtonLight(new Point(color, 8), PalleteColor.BLACK, LightEffect.STATIC);
        launchpad.setButtonLight(new Point(color, 8), colors[color], LightEffect.FLASHING);
    }

}
