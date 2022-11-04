import java.util.Scanner;

public class Main {

    private static int parseGridChange(String s){
        // Could do a *simpler* check were the requirements a bit different
        int[] coordinates = new int[2];
        Scanner scanner = new Scanner(s);
        for(int i = 0; i < 2; i++) {
            if (!scanner.hasNextBigInteger())
                return -1; // not a number
            if(!scanner.hasNextInt())
                return -2; // out of range
            coordinates[i] = scanner.nextInt() - 1;
            if(coordinates[i] < 0 || coordinates[i] > 2)
                return -2; // out of range
        }
        return 1 << (coordinates[0] * 3 + coordinates[1]);
    }

    private static boolean isWinner(short subgrid){
        boolean winner = false;
        short[] masks = { 0b000000111, 0b000111000, 0b111000000,      // rows
                0b100100100, 0b010010010, 0b001001001,  // columns
                0b100010001, 0b001010100};           // diagonals
        for (short mask : masks)
            winner |= (subgrid & mask) == mask;
        return winner;
    }

    private static int checkGrid(int grid){
        short xGrid = (short)(grid & 0b111111111);
        short oGrid = (short)((grid >> 9) & 0b111111111);
        int xCount = Integer.bitCount(xGrid), oCount = Integer.bitCount(oGrid);
        boolean xWins, oWins;
        if(xCount - oCount < -1  || xCount - oCount > 1)
            return 4; // impossible
        xWins = isWinner(xGrid);
        oWins = isWinner(oGrid);
        if(xWins && oWins)
            return 4;
        else if(xWins)
            return 1;
        else if(oWins)
            return 2;
        else if(xCount + oCount < 9)
            return 0; // unfinished
        else
            return 3; // draw
    }

    private static int getGridStringCoordinate(int cell){
        int zeroes = Integer.numberOfTrailingZeros(cell);
        return 12 + (zeroes * 2) +  (zeroes / 3) * 4; // a row of a grid string is 10 chars long
    }

    public static void main(String[] args) {
        // write your code here
        String[] gameStates = { "Game not finished", "X wins", "O wins", "Draw", "Impossible"};
        String[] gridChangeHelp = { "You should enter numbers!", "Coordinates should be from 1 to 3!",
                "This cell is occupied! Choose another one!" };
        char[] gridString = "---------\n| _ _ _ |\n| _ _ _ |\n| _ _ _ |\n---------".toCharArray();
        Scanner scanner = new Scanner(System.in);
        int grid = 0;
        int state;
        int turnShift = 0;
        System.out.println(gridString);
        do {
            int cell = -1;
            while (cell < 0) {
                System.out.print("Enter the coordinates: ");
                cell = parseGridChange(scanner.nextLine()); // may return error code
                if (cell > 0 && ((grid | (grid >> 9)) & cell) > 0)
                    cell = -3;
                if (cell < 0)
                    System.out.println(gridChangeHelp[cell * -1 - 1]);
            }

            grid |= cell << turnShift;
            gridString[getGridStringCoordinate(cell)] = turnShift == 0 ? 'X' : 'O';
            state = checkGrid(grid);
            turnShift ^= 9;
            System.out.println(gridString);
        }  while(state == 0);
        System.out.println(gameStates[state]);
    }
}