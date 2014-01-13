package me.mad4a.cli;

/**
 * User: chsc4698@gmail.com
 * Date: 14-1-14
 * all rights reserved
 */
public enum Commands {
	LOGIN(1), STAT(2), LIST(3), RETR(4), DEL(5), RESET(6), APPLY(7), QUIT(8), RECONN(9);

	public int index;

	Commands(int i) {
		this.index = i;
	}
}
