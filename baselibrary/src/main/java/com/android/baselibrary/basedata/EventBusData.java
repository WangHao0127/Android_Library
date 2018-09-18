package com.android.baselibrary.basedata;

/**
 * Author: WangHao
 * Created On: 2018/9/18 0018 17:17
 * Description:
 */
public final class EventBusData {

    private Action action;
    private Object data;

    public Action getAction() {
        return this.action;
    }

    public Object getData() {
        return this.data;
    }

    public EventBusData(EventBusData.Action action, Object data) {
        this.action = action;
        this.data = data;
    }

    public enum Action {
        DELETE_ALL_MESSAGE_IN_SESSION;

        public EventBusData createEventBusData() {
            return this.createEventBusData(null);
        }

        public EventBusData createEventBusData(Object data) {
            return new EventBusData(this, data);
        }
    }
}

