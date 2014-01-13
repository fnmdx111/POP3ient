package me.mad4a.cli;

import me.mad4a.core.POP3Client;
import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.Scanner;

/**
 * User: chsc4698@gmail.com
 * Date: 14-1-14
 * all rights reserved
 */
public class Main {
    static String info =
            "POP3Client by 2010302530073\r\n" +
            "help -> print help\r\n" +
            "login usr pwd -> login\r\n" +
            "stat -> show inbox status\r\n" +
            "list -> show mail list\r\n" +
            "retr id -> retrieve mail\r\n" +
            "del id -> delete mail\r\n" +
            "reset -> undo modification\r\n" +
            "apply -> apply changes\r\n" +
            "reconn -> reconnect\r\n" +
            "quit -> quit\r\n";

    public static Commands validate(String[] args) throws ParseException {
        if (args[0].equals("login")) {
            if (args.length != 3) {
                throw new ParseException("err: invalid login command", 0);
            }
            return Commands.LOGIN;
        } else if (args[0].equals("stat")) {
            if (args.length != 1) {
                throw new ParseException("err: invalid stat command", 0);
            }
            return Commands.STAT;
        } else if (args[0].equals("list")) {
            if (args.length != 1) {
                throw new ParseException("err: invalid list command", 0);
            }
            return Commands.LIST;
        } else if (args[0].equals("retr")) {
            if (args.length != 2) {
                throw new ParseException("err: invalid retr command", 0);
            }
            return Commands.RETR;
        } else if (args[0].equals("del")) {
            if (args.length != 2) {
                throw new ParseException("err: invalid del command", 0);
            }
            return Commands.DEL;
        } else if (args[0].equals("reset")) {
            if (args.length != 1) {
                throw new ParseException("err: invalid reset command", 0);
            }
            return Commands.RESET;
        } else if (args[0].equals("apply")) {
            if (args.length != 1) {
                throw new ParseException("err: invalid apply command", 0);
            }
            return Commands.APPLY;
        } else if (args[0].equals("quit")) {
            if (args.length != 1) {
                throw new ParseException("err: invalid quit command", 0);
            }
            return Commands.QUIT;
        } else if (args[0].equals("reconn")) {
            if (args.length != 1) {
                throw new ParseException("err: invalid reconnect command", 0);
            }
            return Commands.RECONN;
        } else {
            throw new ParseException("err: unknown command", 0);
        }
    }

    public static void main(String[] args) throws IOException, MessagingException {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;

        out.println(info);

        out.print("server address? ");
        String serverIP = in.nextLine();
        out.print("port? ");
        String port = in.nextLine();
        POP3Client client = new POP3Client(serverIP, Integer.valueOf(port), 10000);

        do {
            out.print("? ");
            String[] _ = in.nextLine().split("\\s");
            try {
                Commands cmd = validate(_);
                switch (cmd) {
                    case LOGIN:
                        client.login(_[1], _[2]);
                        break;
                    case STAT:
                        client.status();
                        break;
                    case LIST:
                        client.list();
                        break;
                    case RETR:
                        MimeMessageParser parser = client.retrieve(_[1]);
                        if (parser != null) {
                            client.showMessage(parser);
                        } else {
                            out.println("err: null message");
                        }
                        break;
                    case DEL:
                        client.delete(_[1]);
                        break;
                    case RESET:
                        client.reset();
                        break;
                    case APPLY:
                        client.apply();
                        break;
                    case RECONN:
                        client.resetConnection();
                        break;
                    case QUIT:
                        client.quit();
                        System.exit(0);
                }
            } catch (ParseException e) {
                out.println(e.getMessage());
            } catch (SocketTimeoutException e) {
                out.println(e.getMessage());
            } catch (IOException e) {
                out.println(e.getMessage());
            }
        } while (true);
    }
}
