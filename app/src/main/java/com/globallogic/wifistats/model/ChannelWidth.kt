package com.globallogic.wifistats.model

enum class ChannelWidth(val value: Int, val title: String) {
    CHANNEL_WIDTH_20MHZ(0, "20 MHZ"),
    CHANNEL_WIDTH_40MHZ(1, "40 MHZ"),
    CHANNEL_WIDTH_80MHZ(2, "80 MHZ"),
    CHANNEL_WIDTH_160MHZ(3, "160 MHZ"),
    CHANNEL_WIDTH_80MHZ_PLUS_MHZ(4, "80MHZ_PLUS_MHZ"),
    CHANNEL_WIDTH_320MHZ(5, "320MHZ");

    companion object {
        fun fromValue(value: Int): ChannelWidth {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return CHANNEL_WIDTH_20MHZ
        }
    }
}