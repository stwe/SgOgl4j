/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

public class IslandTile {

    /*
    01 02 03 04
    -----------
    C7 C3 DF 01

    = 31441863

                7      15     7         15      1 |  3
    0000000 | 111 | 01111 | 111 | 00001111 | 0001 | 11
          7     3       5     3          8      4    2

    typedef struct
    {
        uint16_t bebauung;
        uint8_t x_pos;
        uint8_t y_pos;

        uint32_t rot : 2;
        uint32_t ani : 4;
        uint32_t unbekannt : 8; // Werte zwischen 0 und ca. 17, pro Insel konstant, mehrere Inseln können den gleichen Wert haben
        uint32_t status : 3; // 7: frei, 0: von spieler besiedelt, 1: von spieler erobert (?)
        uint32_t zufall : 5;
        uint32_t spieler : 3;
        uint32_t leer : 7;
    } inselfeld_t;
    */

    // original 8 bytes
    public int developmentId;
    public int xPosOnIsland;
    public int yPosOnIsland;

    public int rotation;
    public int animationCount;
    public int unknow;
    public int state;
    public int random;
    public int player;
    public int empty;
}
