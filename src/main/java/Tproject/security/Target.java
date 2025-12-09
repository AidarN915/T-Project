package Tproject.security;

import Tproject.enums.ObjectType;
import Tproject.enums.OperationType;

public class Target {
    public static TargetIdentifier project(Long id, OperationType type) {
        return new TargetIdentifier(ObjectType.PROJECT, id,type);
    }
    public static TargetIdentifier board(Long id, OperationType type) {
        return new TargetIdentifier(ObjectType.BOARD, id,type);
    }
    public static TargetIdentifier taskList(Long id, OperationType type) {
        return new TargetIdentifier(ObjectType.TASK_LIST, id,type);
    }
    public static TargetIdentifier task(Long id, OperationType type) {
        return new TargetIdentifier(ObjectType.TASK, id,type);
    }
    public static TargetIdentifier comment(Long id, OperationType type) {
        return new TargetIdentifier(ObjectType.COMMENT, id,type);
    }
    public static TargetIdentifier chat(Long id, OperationType type) {
        return new TargetIdentifier(ObjectType.CHAT, id,type);
    }
    public static TargetIdentifier taskUpload(Long id, OperationType type) {
        return new TargetIdentifier(ObjectType.TASK_UPLOAD, id,type);
    }

}
