package com.lyle.socket.Monitor.wasu;

import java.util.Date;

/**
 * @ClassName: Warning
 * @Description: 封装事件的value
 * @author: Lyle
 * @date: 2017年4月25日 下午8:16:48
 */
public class Warning {

	/**
	 * 事件key
	 */
	private String eventKey;

	/**
	 * 站点名称
	 */
	private String stationName;

	/**
	 * 事件来源
	 */
	private String eventSource;

	/**
	 * 事件内容
	 */
	private String eventContent;

	/**
	 * 事件级别，0~10级，越高越重要，一般五级以上比较重要
	 */
	private String eventLevel;

	/**
	 * 事件类型
	 */
	private String eventType;

	/**
	 * 发生时间
	 */
	private Date eventHappen;

	/**
	 * 事件ID
	 */
	private String eventId;

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getEventSource() {
		return eventSource;
	}

	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getEventLevel() {
		return eventLevel;
	}

	public void setEventLevel(String eventLevel) {
		this.eventLevel = eventLevel;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Date getEventHappen() {
		return eventHappen;
	}

	public void setEventHappen(Date eventHappen) {
		this.eventHappen = eventHappen;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	@Override
	public String toString() {
		return "Warning [eventKey=" + eventKey + ", stationName=" + stationName + ", eventSource=" + eventSource
				+ ", eventContent=" + eventContent + ", eventLevel=" + eventLevel + ", eventType=" + eventType
				+ ", eventHappen=" + eventHappen + ", eventId=" + eventId + "]";
	}
}
