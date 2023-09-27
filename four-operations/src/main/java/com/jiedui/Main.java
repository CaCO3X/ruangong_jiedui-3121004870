package com.jiedui;

import com.jiedui.utils.CheckUtils;
import com.jiedui.utils.GenerateUtils;
import com.jiedui.utils.SaveUtils;

/**
 * @author lql 2023/09/25
 */
public class Main {
    public static void main(String[] args) {
        // 检查命令行参数
        if (args.length == 4 && args[0].equals("-n")&&args[2].equals("-r")) {//用来生成题目和相应的参考答案，并且用户须同时写入自己的答案
            int num = Integer.parseInt(args[1]);
            int range = Integer.parseInt(args[3]);

            // 初始化工具类
            GenerateUtils generateUtils = new GenerateUtils(range);

            generateUtils.generateExercises(num);
            SaveUtils.answerExercises(num);
        } else if (args.length == 4 && args[0].equals("-e") && args[2].equals("-a")) {//用来检查用户的答题情况
            String exerciseFile = args[1];
            String myanswerFile = args[3];

            CheckUtils.checkAnswers(exerciseFile, myanswerFile);
        } else {//输入错误的命令时
            System.out.println("命令行参数错误，请输入正确的参数。");
            System.out.println("生成题目示例：-n <生成题目数量> -r <题目中的数值范围>");
            System.out.println("检查答案示例：-e <题目文件名> -a <我的答案的文件名>");
        }
    }
}
