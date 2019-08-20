package com.cn.jm._dao.room;

public enum RoomEnum {
	ONLINE(0),
	FZ(1),
	OFFLINE(2);
	
	private final int value;

	private RoomEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public boolean equals(int value) {
		return this.value == value;
	}
}
