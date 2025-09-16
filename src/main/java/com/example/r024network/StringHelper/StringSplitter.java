package com.example.r024network.StringHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringSplitter {

    /**
     * 将分号分隔的数字字符串转换为整数数组
     * @param input 分号分隔的数字字符串，如 "1;3;6"
     * @return 整数数组，如果输入无效则返回空数组
     */
    public int[] splitToIntArray(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new int[0];
        }

        // 使用分号分割字符串
        String[] parts = input.split(";");
        int[] result = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            try {
                result[i] = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                // 处理无效数字，可以记录日志或抛出异常
                System.err.println("无效的数字: " + parts[i]);
                result[i] = 0; // 或者可以选择跳过无效值
            }
        }

        return result;
    }

    /**
     * 将分号分隔的数字字符串转换为整数列表
     * @param input 分号分隔的数字字符串，如 "1;3;6"
     * @return 整数列表，如果输入无效则返回空列表
     */
    public List<Integer> splitToIntList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // 使用分号分割字符串并转换为整数列表
        return Arrays.stream(input.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        // 处理无效数字
                        System.err.println("无效的数字: " + s);
                        return null; // 返回null，后面会过滤掉
                    }
                })
                .filter(n -> n != null)
                .collect(Collectors.toList());
    }

    /**
     * 将分号分隔的数字字符串转换为长整数列表
     * @param input 分号分隔的数字字符串，如 "1;3;6"
     * @return 长整数列表，如果输入无效则返回空列表
     */
    public List<Long> splitToLongList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // 使用分号分割字符串并转换为长整数列表
        return Arrays.stream(input.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try {
                        return Long.parseLong(s);
                    } catch (NumberFormatException e) {
                        // 处理无效数字
                        System.err.println("无效的数字: " + s);
                        return null; // 返回null，后面会过滤掉
                    }
                })
                .filter(n -> n != null)
                .collect(Collectors.toList());
    }

    /**
     * 将数字数组转换回分号分隔的字符串
     * @param array 整数数组
     * @return 分号分隔的字符串
     */
    public String joinToString(int[] array) {
        if (array == null || array.length == 0) {
            return "";
        }

        return Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(";"));
    }

    /**
     * 将数字列表转换回分号分隔的字符串
     * @param list 数字列表
     * @return 分号分隔的字符串
     */
    public String joinToString(List<? extends Number> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }

    /**
     * 验证字符串是否是有效的分号分隔数字字符串
     * @param input 输入字符串
     * @return 是否有效
     */
    public boolean isValidNumberList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return true; // 空字符串视为有效
        }

        String[] parts = input.split(";");
        for (String part : parts) {
            try {
                Integer.parseInt(part.trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}