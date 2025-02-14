package io.wdsj.leavidence.enums;

import io.wdsj.leavidence.Leavidence;

public enum CheckMode {
    BOT,
    OWNER;

    public static CheckMode fromString(String mode) {
        try {
            return CheckMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException e) {
            Leavidence.logger().warn("Invalid check mode: {}. Defaulting to BOT.", mode);
            return BOT;
        }
    }
}
