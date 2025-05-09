package com.proyIntUdeA.proyectoIntegradorI.model.enums;
    public enum canceledBy {
        NONE(0),
        STUDENT(1),
        ADMIN(2),
        TUTOR(3);

        private final int value;

        canceledBy(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static canceledBy fromValue(Short value) {
            if (value == null) {
                return null;
            }

            for (canceledBy status : canceledBy.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown canceledBy value: " + value);
        }
    }