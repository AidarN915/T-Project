package Tproject.security;

import Tproject.enums.ObjectType;
import Tproject.enums.OperationType;

public record TargetIdentifier(ObjectType type, Long id, OperationType opType) {}
