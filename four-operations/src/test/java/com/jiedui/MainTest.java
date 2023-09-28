package com.jiedui;

import org.junit.Test;

/**
 * @author lql 2023/09/27
 */
public class MainTest {
    @Test
    public void mainTest(){
        // 生成10个题目以及范围为6
        System.out.println(">>>生成问题");
        System.out.println();
        String[] args = {"-n","10","-r","6"};
        Main.main(args);
        System.out.println(">>>回答问题");
        System.out.println();
        // 第二个分支
        String[] args2 = {"-e","src/main/java/com/jiedui/test/Exercises.txt","-a","src/main/java/com/jiedui/test/AnswersOfMine.txt"};
        Main.main(args2);

        // 异常分支
        System.out.println(">>>故意进入异常分支");
        Main.main(new String[0]);
    }
}