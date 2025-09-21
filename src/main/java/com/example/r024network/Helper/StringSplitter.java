package com.example.r024network.Helper;
import java.util.Arrays;
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
}