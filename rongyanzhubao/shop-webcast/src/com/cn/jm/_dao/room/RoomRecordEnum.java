package com.cn.jm._dao.room;

public enum RoomRecordEnum {
	/**
	 * 直播
	 */
	LIVE(0),
	CLOSE(1);
	private final int value;

	private RoomRecordEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}


}
