/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

public class IslandTile {

    // original only 8 bytes

    // inselfeld_t

    /*
    uint16_t id; // tile gaphic ID, see haeuser.cod for referene
    uint8_t posx; // position on island
    uint8_t posy; // position on island
    uint32_t orientation : 2; // orientation
    uint32_t animationCount : 4; // animation step for tile
    uint32_t islandNumber : 8; // the island the field is part of
    uint32_t cityNumber : 3; // the city the field is part of
    uint32_t randomNumber : 5; // random number, what for?
    uint32_t playerNumber : 4; // the player that occupies this field
    */

    // original only 8 bytes
    public int developmentId;
    public int xPosOnIsland;
    public int yPosOnIsland;

    public int rot;
    public int ani;
    public int unknow;
    public int state;
    public int random;
    public int player;
    public int empty;

}
