package com.comego.record.common.util;

public class TokenUtil {

    /**
     * 从token中解析用户ID
     * 支持两种格式：
     * 1. 原始格式: token_用户ID_时间戳
     * 2. Bearer格式: Bearer token_用户ID_时间戳
     * @param token token字符串
     * @return 用户ID，解析失败返回null
     */
    public static Long getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        try {
            // 去除 "Bearer " 前缀（如果存在）
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String[] parts = token.split("_");
            if (parts.length >= 3 && "token".equals(parts[0])) {
                return Long.parseLong(parts[1]);
            }
        } catch (Exception e) {
            // 解析失败
        }
        
        return null;
    }
}
