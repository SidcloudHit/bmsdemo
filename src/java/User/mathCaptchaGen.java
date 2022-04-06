/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class mathCaptchaGen {

    private static final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final String[] OPERATIONS = {"+", "-", "x"};
    private static final Random RAND = new Random(); // not high entropy, but not a big deal here

    public Map<String, String> generateCaptcha(HttpSession session) {

        Map<String, String> captcha = new HashMap<String, String>();

        int first, second, third, op, pos, solution;

        op = RAND.nextInt(OPERATIONS.length);
        captcha.put("operation", OPERATIONS[op]);
        switch (op) {
            case 2: // multiplication; limit generated args to [1-5]
                first = RAND.nextInt(5) + 1;
                second = RAND.nextInt(5) + 1;
                third = first * second;
                break;
            case 1: // subtraction
                first = RAND.nextInt(NUMBERS.length);
                second = RAND.nextInt(NUMBERS.length);
                if (first < second) {
                    // reorder to ensure positive solution
                    int tmp = first;
                    first = second;
                    second = tmp;
                }
                third = first - second;
                break;
            default: // addition
                first = RAND.nextInt(NUMBERS.length);
                second = RAND.nextInt(NUMBERS.length);
                third = first + second;
                break;
        }

        String rFirst = NUMBERS[first], rSecond = NUMBERS[second], rThird;
        if (third < NUMBERS.length) {
            rThird = NUMBERS[third];
        } else {
            rThird = Integer.toString(third);
        }

        pos = RAND.nextInt(3);
        switch (pos) {
            case 0:
                solution = first;
                captcha.put("second", rSecond);
                captcha.put("third", rThird);
                captcha.put("missing", "1");
                break;
            case 1:
                captcha.put("first", rFirst);
                solution = second;
                captcha.put("third", rThird);
                captcha.put("missing", "2");
                break;
            default:
                captcha.put("first", rFirst);
                captcha.put("second", rSecond);
                solution = third;
                captcha.put("missing", "3");
                break;
        }
        session.setAttribute("captcha_solution", Integer.toString(solution));

        return captcha;
    }

    public Map<String, String> generateCaptcha() {

        Map<String, String> captcha = new HashMap<String, String>();

        int first, second, third, op, pos, solution;

        op = RAND.nextInt(OPERATIONS.length);
        captcha.put("operation", OPERATIONS[op]);
        switch (op) {
            case 2: // multiplication; limit generated args to [1-5]
                first = RAND.nextInt(5) + 1;
                second = RAND.nextInt(5) + 1;
                third = first * second;
                break;
            case 1: // subtraction
                first = RAND.nextInt(NUMBERS.length);
                second = RAND.nextInt(NUMBERS.length);
                if (first < second) {
                    // reorder to ensure positive solution
                    int tmp = first;
                    first = second;
                    second = tmp;
                }
                third = first - second;
                break;
            default: // addition
                first = RAND.nextInt(NUMBERS.length);
                second = RAND.nextInt(NUMBERS.length);
                third = first + second;
                break;
        }

        String rFirst = NUMBERS[first], rSecond = NUMBERS[second], rThird;
        if (third < NUMBERS.length) {
            rThird = NUMBERS[third];
        } else {
            rThird = Integer.toString(third);
        }

//        pos = RAND.nextInt(3);
//        switch (pos) {
//            case 0:
//                solution = first;
//                captcha.put("second", rSecond);
//                captcha.put("third", rThird);
//                captcha.put("missing", "1");
//                break;
//            case 1:
//                captcha.put("first", rFirst);
//                solution = second;
//                captcha.put("third", rThird);
//                captcha.put("missing", "2");
//                break;
//            default:
//                captcha.put("first", rFirst);
//                captcha.put("second", rSecond);
//                solution = third;
//                captcha.put("missing", "3");
//                break;
//        }
        captcha.put("first", rFirst);
        solution = third;
        captcha.put("second", rSecond);
        captcha.put("missing", "1");
        captcha.put("solution", Integer.toString(solution));
        //session.setAttribute("captcha_solution", Integer.toString(solution));

        return captcha;
    }
}
