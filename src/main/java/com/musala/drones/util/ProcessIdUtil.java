package com.musala.drones.util;

import java.util.Objects;
import java.util.UUID;

public class ProcessIdUtil {

    public static final String PROCESS_ID_HEADER_NAME = "processId";

    public static String checkAndGenerateProcessId(String processId) {
        if (Objects.isNull(processId) || processId.isEmpty()) {
            return UUID.randomUUID().toString();
        } else {
            return processId;
        }
    }

}
