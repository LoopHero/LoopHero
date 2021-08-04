package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * this class represents a path tile.
 * It holds internally an enumerated value representing a direction.
 */
public class PathTile extends StaticEntity {
    public enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        private int xOffset;
        private int yOffset;

        public int getXOffset() { return xOffset; }
        public int getYOffset() { return yOffset; }

        public Direction invert() {
            switch (this) {
                case UP: return DOWN;
                case DOWN: return UP;
                case LEFT: return RIGHT;
                case RIGHT: return LEFT;
                default: throw new IllegalArgumentException("Invalid direction to invert");
            }
        }

        /**
         * Calculates a direction from the provided points, given the direction goes from x1, y1 -> x2, y2
         */
        public static Direction getDirectionFromOffset(int x1, int y1, int x2, int y2) {
            int xOffset = x2 - x1;
            int yOffset = y2 - y1;
            switch (xOffset) {
                case 1: return RIGHT;
                case -1: return LEFT;
            }
            switch (yOffset) {
                case -1: return UP;
                case 1: return DOWN;
            }
            throw new IllegalArgumentException(String.format("Invalid Direction formed from offset %d and %d", xOffset, yOffset));
        }

        private Direction(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
    }

    public PathTile(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
}
