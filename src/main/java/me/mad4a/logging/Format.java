package me.mad4a.logging;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: chsc4698@gmail.com
 * Date: 14-1-13
 * all rights reserved
 */
public class Format {
	public class Entry {
		public String name;
		public String format;
		public Entry(String n, String f) {
			name = n;
			format = f;
		}
	}

	private List<Entry> parameters;

	public Format(List<Entry> parameters) {
		this.parameters = parameters;
	}

	public Format() {
		parameters = new LinkedList<Entry>();
		addParameter("LEVEL", "%s");
		addParameter("", ":");
		addParameter("TIME", "%tD");
		addParameter("", ":");
		addParameter("NAME", "%s");
		addParameter("", ": ");
		addParameter("MSG", "%s");
	}

	public void addParameter(String name, String format) {
		parameters.add(new Entry(name, format));
	}

	public String format(Map<String, Object> map) {
		StringBuilder builder = new StringBuilder();
		for (Entry entry: parameters) {
			String key = entry.name;
			if (key.equals("")) {
				builder.append(entry.format);
			} else if (key.equals("LEVEL") ||
					   key.equals("NAME")  ||
					   key.equals("MSG")) {
				String content = (String) map.get(key);
				builder.append(String.format(entry.format, content));
			} else if (key.equals("TIME")) {
				Long time = (Long) map.get(key);
				builder.append(String.format(entry.format, time));
			}
		}

		return builder.toString();
	}

	public static void main(String[] args) {
		Format format = new Format();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LEVEL", "info");
		map.put("TIME", System.currentTimeMillis());
		map.put("NAME", "root");
		map.put("MSG", "test");

		System.out.println(format.format(map));
	}
}
