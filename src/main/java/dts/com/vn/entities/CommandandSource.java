package dts.com.vn.entities;

import lombok.Data;

@Data
public class CommandandSource implements Comparable {

	private String source;

	private String command;

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
