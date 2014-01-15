package me.mad4a.misc;

/**
* User: chsc4698@gmail.com
* Date: 14-1-13
* all rights reserved
*/
public class MailEntry {
    private String id;
    private String size;

    public MailEntry(String raw) {
        String[] _ = raw.split(" ", 2);

        id = _[0];
        size = _[1];
    }

	public String getId() {
		return id;
	}

    public int getSize() {
        return Integer.valueOf(size);
    }
}
