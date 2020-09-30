/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import static de.sg.islands.Util.byteToInt;
import static de.sg.islands.Util.shortToInt;

import static de.sg.ogl.Log.LOGGER;

public class Island5 {

    // original 8 bytes
    private static class OreDeposit {
        public int type;
        public int xPos;
        public int yPos;
        public int explored;
        public int unknow;
    }

    // original 116 bytes
    public int index;
    public int width;
    public int height;
    public int a;
    public int xPos;
    public int yPos;
    public int b;
    public int c;
    public int[] bytes0 = new int[14];
    public int ore;
    public int explored;
    public OreDeposit[] oreDeposits = new OreDeposit[2];
    public int[] bytes1 = new int[48];
    public int productivity;
    public int d;
    public int e;
    public int base;
    public int f;
    public int south;
    public int diff;
    public int [] bytes2 = new int[14];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Island5(Chunk chunk) {
        LOGGER.debug("Creates Island5 object.");

        readIslandData(chunk);

        LOGGER.debug("Island5 width: {}, height: {}, xPos: {}, yPos: {}, prod: {}", width, height, xPos, yPos, productivity);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void readIslandData(Chunk chunk) {
        LOGGER.debug("Start reading the Island5 data...");

        index = byteToInt(chunk.getData().get());
        width = byteToInt(chunk.getData().get());
        height = byteToInt(chunk.getData().get());
        a = byteToInt(chunk.getData().get());
        xPos = shortToInt(chunk.getData().getShort());
        yPos = shortToInt(chunk.getData().getShort());
        b = shortToInt(chunk.getData().getShort());
        c = shortToInt(chunk.getData().getShort());

        byte[] b0 = new byte[14];
        chunk.getData().get(b0);
        for (int i = 0; i < 14; i++) {
            bytes0[i] = byteToInt(b0[i]);
        }

        ore = byteToInt(chunk.getData().get());
        explored = byteToInt(chunk.getData().get());

        oreDeposits[0] = new OreDeposit();
        oreDeposits[0].type = byteToInt(chunk.getData().get());
        oreDeposits[0].xPos = byteToInt(chunk.getData().get());
        oreDeposits[0].yPos = byteToInt(chunk.getData().get());
        oreDeposits[0].explored = byteToInt(chunk.getData().get());
        oreDeposits[0].unknow = chunk.getData().getInt();

        oreDeposits[1] = new OreDeposit();
        oreDeposits[1].type = byteToInt(chunk.getData().get());
        oreDeposits[1].xPos = byteToInt(chunk.getData().get());
        oreDeposits[1].yPos = byteToInt(chunk.getData().get());
        oreDeposits[1].explored = byteToInt(chunk.getData().get());
        oreDeposits[1].unknow = chunk.getData().getInt();

        byte[] b1 = new byte[48];
        chunk.getData().get(b1);
        for (int i = 0; i < 48; i++) {
            bytes1[i] = byteToInt(b1[i]);
        }

        productivity = byteToInt(chunk.getData().get());
        d = byteToInt(chunk.getData().get());
        e = shortToInt(chunk.getData().getShort());
        base = shortToInt(chunk.getData().getShort());
        f = shortToInt(chunk.getData().getShort());
        south = byteToInt(chunk.getData().get());
        diff = byteToInt(chunk.getData().get());

        byte[] b2 = new byte[14];
        chunk.getData().get(b2);
        for (int i = 0; i < 14; i++) {
            bytes2[i] = byteToInt(b2[i]);
        }

        LOGGER.debug("Island5 data read successfully.");
    }
}
