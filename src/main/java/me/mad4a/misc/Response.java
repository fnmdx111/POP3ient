package me.mad4a.misc;

/**
* User: chsc4698@gmail.com
* Date: 14-1-13
* all rights reserved
*/
public class Response {
    public String status;
    public String content;

    public Response(String raw) {
        String[] _ = raw.split(" ", 2);

        status = _[0];
        content = _[1];
    }

    public boolean isSuccessful() {
        return status.equals("+OK");
    }
}
