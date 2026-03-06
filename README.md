# Simple Virtual World Simulator

A simple console-based simulation project written in Java. It creates a grid world where different animals and plants coexist, move, and interact with each other turn by turn.

## How to play
You control the Human character. Use the keyboard to determine your next move and watch how the world evolves.

## Map Selection
You can toggle the map type in the source code (or config file) using the USE_HEX flag:

Rectangular: Standard grid. Movement is orthogonal (or diagonal depending on implementation).
<img width="1634" height="1690" alt="image" src="https://github.com/user-attachments/assets/ecc9dd86-c5b7-490c-b1d0-d82eefd0a500" />


Hexagonal: Every cell has 6 neighbors. This changes the movement logic and adjacency rules for breeding and combat.
<img width="1512" height="840" alt="image" src="https://github.com/user-attachments/assets/d56dda33-2941-4c67-aa43-e96a82fc14d4" />


### Controls
* **Arrows / W A S D** - Move Human
* **Q / W / E / A / S / D / Z / X / C** - Specific directions (used for Hexagonal navigation).
* **U** - Activate Special Ability
* **Z** - Save game state
* **L** - Load game state
* **Q** - Quit

## Legend (Organisms)

Here is the list of symbols you will see on the board and what they represent:

| Symbol | Organism (English) | Organism (Polish) |
|:---:|:---|:---|
| **H** | Human (Player) | Człowiek |
| **W** | Wolf | Wilk |
| **O** | Sheep | Owca |
| **Z** | Turtle | Żółw |
| **L** | Fox | Lis |
| **A** | Antelope | Antylopa |
| **C** | Cyber Sheep | Cyberowca |
| **T** | Grass | Trawa |
| **M** | Dandelion | Mlecz |
| **G** | Guarana | Guarana |
| **J** | Deadly Nightshade | Wilcze Jagody |
| **B** | Giant Hogweed | Barszcz Sosnowskiego |
