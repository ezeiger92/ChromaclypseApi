package com.chromaclypse.api.json;

import java.util.HashMap;

import com.chromaclypse.api.messages.Text;

public class JsonBlob {
	private final StringBuilder builder = new StringBuilder();

	public JsonBlob() {
	}

	public JsonBlob(String text, Prop... props) {
		add(text, props);
	}

	public JsonBlob add(String text, Prop... props) {
		HashMap<String, String> properties = new HashMap<>(props.length + 1);
		properties.put("\"text\"", '"' + text.replace("\\", "\\\\").replace("\"", "\\\"") + '"');
		for (Prop p : props)
			p.apply(properties);

		if (builder.length() > 0)
			builder.append(',');

		char prefix = '{';
		for (HashMap.Entry<String, String> entry : properties.entrySet()) {
			builder.append(prefix).append(entry.getKey()).append(':').append(entry.getValue());
			prefix = ',';
		}

		builder.append('}');

		return this;
	}
	
	public JsonBlob add(JsonBlob blob) {
		if (builder.length() > 0 && blob.builder.length() > 0)
			builder.append(',').append(blob.builder);
		
		return this;
	}

	@Override
	public String toString() {
		return '[' + builder.toString() + ']';
	}

	private static Prop ofChar(char in, Prop normal) {
		switch (in) {
			case '0':
				return Prop.BLACK;
			case '1':
				return Prop.DARK_BLUE;
			case '2':
				return Prop.DARK_GREEN;
			case '3':
				return Prop.DARK_AQUA;
			case '4':
				return Prop.DARK_RED;
			case '5':
				return Prop.DARK_PURPLE;
			case '6':
				return Prop.GOLD;
			case '7':
				return Prop.GRAY;
			case '8':
				return Prop.DARK_GRAY;
			case '9':
				return Prop.BLUE;
			case 'a':
				return Prop.GREEN;
			case 'b':
				return Prop.AQUA;
			case 'c':
				return Prop.RED;
			case 'd':
				return Prop.LIGHT_PURPLE;
			case 'e':
				return Prop.YELLOW;
			case 'f':
				return Prop.WHITE;
			case 'r':
				return normal;

			case 'o':
				return Prop.ITALIC;
			case 'l':
				return Prop.BOLD;
			case 'm':
				return Prop.STRIKE;
			case 'n':
				return Prop.UNDERLINE;
			case 'k':
				return Prop.MAGIC;

			default:
				return Prop.NONE;
		}
	}

	public JsonBlob addLegacy(String legacy, Prop... defaults) {
		if (builder.length() > 0)
			builder.append(',');

		builder.append(fromLegacy(legacy, defaults).builder);
		return this;
	}

	public static JsonBlob fromLegacy(String legacy, Prop... defaults) {
		JsonBlob result = new JsonBlob();

		Prop normal = Prop.FUSE(defaults);

		Prop color = Prop.NONE;
		Prop style = Prop.NONE;
		int start = 0, end = 0;

		for (int i = 0; i < legacy.length() - 1; ++i) {
			if (Text.format().isColor(legacy, i)) {
				char c = Character.toLowerCase(legacy.charAt(i + 1));
				boolean valid = false;

				Prop nextColor = color;
				Prop nextStyle = style;
				
				Prop fromChar = ofChar(c, normal);
				
				if(fromChar != Prop.NONE) {
					if(c <= 'f' || c == 'r') {
						nextColor = fromChar;
						nextStyle = Prop.NONE;
						valid = true;
					}
					else {
						nextStyle = fromChar;
						valid = true;
					}
				}

				if (valid) {
					if (start != end)
						result.add(legacy.substring(start, end), normal, color, style);

					color = nextColor;
					style = nextStyle;
					start = end = i + 2;
					++i;
					continue;
				}
			}

			++end;
		}

		return result.add(legacy.substring(start, legacy.length()), normal, color, style);
	}
}
