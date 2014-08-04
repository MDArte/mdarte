package org.andromda.core;

public class ANSI {
	static char Esc = (char) 27;
    public static void gotoXY( int x, int y ) {
    	System.out.print(Esc + "[");
    	System.out.print( y );
    	System.out.print( ";" );
    	System.out.print( x );
    	System.out.print( "H" );
    }
    public static void moveLineUp(int n) {
    	System.out.print(Esc + "[" + n + "A");
    }
    public static void moveLineDown(int n) {
    	System.out.print(Esc + "[" + n + "B");
    }   
    public static void moveColumnForward(int n) {
    	System.out.print(Esc + "[" + n + "C");
    }
    public static void moveColumnBackward(int n) {
    	System.out.print(Esc + "[" + n + "D");
    }   	       
    public static void clrScr() {
    	System.out.print(Esc + "[2J");
    }
    public static void erase() {
    	System.out.print(Esc + "[K");
    }
}
