package com.inved.realestatemanager.domain;

import java.util.List;

public class JoinCheckBoxInString {

    public String joinMethod(List<String> input) {

        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.size(); i++) {

            sb.append(input.get(i));

            // if not the last item
            if (i != input.size() - 1) {
                sb.append(",");
            }

        }

        return sb.toString();

    }
}
