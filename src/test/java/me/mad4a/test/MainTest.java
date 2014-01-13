package me.mad4a.test;

import me.mad4a.core.POP3Client;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * User: chsc4698@gmail.com
 * Date: 14-1-13
 * all rights reserved
 */
public class MainTest {
    POP3Client client;

    public MainTest() throws IOException {
        client = new POP3Client("pop.163.com", 110, 30000);
        // client = new POP3Client("127.0.0.1", 110);
    }

    @Before
    public void testLogin() {
        assertTrue(client.login("1", "2"));
    }

    @Test
    public void testStat() {
        client.status();
    }

    @Test
    public void testList() throws IOException {
        client.list();
    }

    @Test
    public void testRetrieve() throws MessagingException {
        client.showMessage(client.retrieve("1"));
    }
}
