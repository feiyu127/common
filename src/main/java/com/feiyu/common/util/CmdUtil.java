package com.feiyu.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author feiyu127@gmail.com
 * @date 2018-09-13 10:00
 */
public class CmdUtil {

    private static final Pattern REG_EXE_PATH_PATTERN = Pattern.compile("(\".*?exe\")");

    public static void main(String[] args) {
        System.out.println(isPalindrome(1410110141));
    }
    public static boolean isPalindrome(int x) {
        if(x < 0){
            return false;
        }
        if(x < 10){
            return true;
        }
        int length = 12;
        int[] num = new int[length];
        int actLength = 0;
        for(int i = 0; x > 0; i++){
           num[i] = x%10;
           x = x/10;
           actLength++;
        }
        for(int i = 0; i < actLength/2; i++){
            if(num[i] != num[actLength-i-1]){
                return false;
            }
        }
        return true;
    }
    /**
     * 执行某个命令，并返回命令执行返回结果
     * @param command 命令字符串
     * @return
     */
    public static String exeCommand(String command) {
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(command);
            exec.getOutputStream().close();
            InputStreamReader inputStream = new InputStreamReader(exec.getInputStream(), "gbk");
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String resultStr = bufferedReader.lines().collect(Collectors.joining("\n"));
            exec.destroy();
            return resultStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据注册表名称获取注册表里的exe文件路径
     * @param name
     * @return
     */
    public static String getRegExeValue(String name) {
        String cmd = "reg query " + name;
        String cmdResult = exeCommand(cmd);
        Matcher matcher = REG_EXE_PATH_PATTERN.matcher(cmdResult);
        boolean exeResult = matcher.find();
        return exeResult ? matcher.group(1) : null;
    }
}
