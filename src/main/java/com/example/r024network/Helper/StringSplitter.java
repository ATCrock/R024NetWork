package com.example.r024network.Helper;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StringSplitter {

    /**
     * 将分号分隔的数字字符串转换为整数数组
     * @param input 分号分隔的数字字符串
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
                // 如果数字格式有问题
                System.err.println("无效的数字: " + parts[i]);
                result[i] = 0;
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
            return null;
        }
        return Arrays.stream(array)
                .mapToObj(String::valueOf) // 把数字变成字符串
                .collect(Collectors.joining(";"));
    }
}