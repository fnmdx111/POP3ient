package me.mad4a.core;

import me.mad4a.logging.Logger;
import me.mad4a.misc.Response;
import me.mad4a.misc.MailEntry;
import me.mad4a.logging.CommandLineLogger;
import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * User: chsc4698@gmail.com
 * Date: 14-1-13
 * all rights reserved
 */
public class POP3Client {
	private CommandLineLogger log;
	Socket _sock;
	Scanner in;
	PrintWriter out;
	Session session;
	int timeout;

	String cache_username;
	String cache_password;
	String cache_serverIP;
	int cache_serverPort;

	public POP3Client(String serverIP, int serverPort, int timeout) throws IOException {
		cache_serverIP = serverIP;
		cache_serverPort = serverPort;

		this.timeout = timeout;

		session = Session.getInstance(System.getProperties());

		log = new CommandLineLogger("pop3");
		initializeConnection(serverIP, serverPort, timeout);

		_ret();
	}

	public void setLoggerLevel(String level) {
		log.setLevel(Logger.getLevel(level));
	}

	private void initializeConnection(String serverIP, int serverPort,
	                                  int timeout) throws IOException {
		log.d("connecting to %s:%d", serverIP, serverPort);
		_sock = new Socket(serverIP, serverPort);
		_sock.setSoTimeout(timeout);
		log.d("connected");

		in = new Scanner(_sock.getInputStream());
		out = new PrintWriter(_sock.getOutputStream());
	}

	public void resetConnection() throws IOException {
		initializeConnection(cache_serverIP, cache_serverPort, timeout);
	}

	private Response _send(String cmd, boolean readEcho) {
		if (out == null) {
			return new Response("-ERR: not connected");
		}

		out.println(cmd);
		out.flush();

		log.i("cmd: %s", cmd);

		if (readEcho) {
			return _ret();
		} else {
			return new Response("+OK stub");
		}
	}

	private Response _send(String cmd) {
		return _send(cmd, true);
	}

	private Response _ret() {
		String response = "-ERR error reading response";
		if (in.hasNextLine()) {
			response = in.nextLine();
		}
		Response ret = new Response(response);
		if (ret.isSuccessful()) {
			log.i("svr: %s", ret.content);
		} else {
			log.e("svr: %s", ret.content);
		}
		return ret;
	}

	public boolean login(String username, String password) {
		Response ret = _send("USER " + username);
		if (!ret.isSuccessful()) {
			return false;
		}

		ret = _send("PASS " + password);

		if (ret.isSuccessful()) {
			cache_username = username;
			cache_password = password;
			return true;
		}

		return false;
	}

	public List<MailEntry> list() throws IOException {
		List<MailEntry> ret = new ArrayList<MailEntry>();

		Response response = _send("LIST");
		if (!response.isSuccessful()) {
			return ret;
		}

		while (true) {
			if (!in.hasNextLine()) {
				throw new IOException("error reading response");
			}
			String s = in.nextLine();
			if (s.equals(".")) {
				break;
			}

			MailEntry _ = new MailEntry(s);
			ret.add(_);
			log.i("mail %s, size %s", _.id, _.size);
		}

		return ret;
	}

	public int status() {
		Response ret = _send("STAT");

		String[] _ = ret.content.split(" ", 2);
		log.i("total %s of size %s", _[0], _[1]);

		return Integer.valueOf(_[0]);
	}

	public MimeMessageParser retrieve(String id) throws MessagingException {
		StringBuilder builder = new StringBuilder();

		Response response = _send("RETR " + id);
		if (!response.isSuccessful()) {
			return null;
		}

		while (true) {
			String s = in.nextLine();
			if (s.equals(".")) {
				break;
			}

			builder.append(s).append("\r\n");
		}

		MimeMessage message = new MimeMessage(
				session,
				new ByteArrayInputStream(builder.toString().getBytes())
		);

		MimeMessageParser parser = new MimeMessageParser(message);
		try {
			parser.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parser;
	}

	public void showMessage(MimeMessageParser parser) {
		try {
			log.i("From: %s", parser.getFrom());

			List<Address> addresses = parser.getTo();
			for (Address address: addresses) {
				log.i("To: %s", address);
			}

			log.i("Subject: %s", parser.getSubject());
			log.i("Date: %s", parser.getMimeMessage().getReceivedDate());

			String plainContent = parser.getPlainContent();
			if (plainContent != null) {
				log.i("Message:\r\n%s\r\n.", parser.getPlainContent());
			} else {
				log.i("Message: <no plain text content>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean reset() {
		return _send("RSET").isSuccessful();
	}

	public boolean delete(String id) {
		return _send("DELE " + id).isSuccessful();
	}

	public boolean apply() throws IOException {
		log.i("applying modification");
		if (!quit()) {
			return false;
		}

		initializeConnection(cache_serverIP, cache_serverPort, timeout);
		return login(cache_username, cache_password);
	}

	public boolean quit() throws IOException {
		if (_send("QUIT").isSuccessful()) {
			_sock.close();

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		_send("QUIT");

		_sock.close();
	}
}
