package com.dsa.learning.model;

public enum MistakeCategory {
    LOGIC_ERROR("Logic Error"),
    WRONG_ALGORITHM("Wrong Algorithm"),
    WRONG_DATA_STRUCTURE("Wrong Data Structure"),
    BOUNDARY_CONDITION("Boundary Condition"),
    EDGE_CASE("Edge Case"),
    LOOP_ERROR("Loop Error"),
    RECURSION_BASE_CASE("Recursion Base Case"),
    POINTER_MOVEMENT("Pointer Movement"),
    TIME_COMPLEXITY("Time Complexity"),
    SPACE_COMPLEXITY("Space Complexity");

    private final String displayName;

    MistakeCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MistakeCategory fromString(String text) {
        if (text == null) return LOGIC_ERROR;
        String clean = text.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        for (MistakeCategory cat : MistakeCategory.values()) {
            if (cat.name().equalsIgnoreCase(clean) || cat.getDisplayName().equalsIgnoreCase(text.trim())) {
                return cat;
            }
        }
        return LOGIC_ERROR;
    }
}
