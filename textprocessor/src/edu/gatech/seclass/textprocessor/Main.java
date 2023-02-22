package edu.gatech.seclass.textprocessor;

import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;


public class Main {


    private Path createFile(String contents) throws IOException {
        return createFile(contents, "input.txt");
    }

    private Path createFile(String contents, String fileName) throws IOException {
        Path file = Paths.get(fileName);
        Files.write(file, contents.getBytes(StandardCharsets.UTF_8));

        return file;
    }

    private static String getFileContent(Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            usage();
            return null;
        }
    }

    private static void check_input(String content) throws Exception {
        if (content.equals("")) {
        } else {
            String regex = "(?<=" + System.lineSeparator() + ")";
            String[] arrOfStr = content.split(regex);
            if (!arrOfStr[arrOfStr.length - 1].contains(System.lineSeparator())) {
                usage();
                throw new Exception("input content is not end with new line");
            }
        }
    }


    private static void o_method(String content, File output_file) throws Exception {

        if (output_file.exists()) {
            usage();
            throw new Exception("output file alreay exist");

        } else {
            Path outputFile = Paths.get(String.valueOf(output_file));
            Files.write(outputFile, content.getBytes(StandardCharsets.UTF_8));
        }

    }

    private static String k_method(String content, String substring, Boolean caseSensitive) throws IOException {

        String result = "";
        String regex = "(?<=" + System.lineSeparator() + ")";
        String[] arrOfStr = content.split(regex);
        for (int i = 0; i < arrOfStr.length; i++) {
            Boolean check;
            if (!caseSensitive) {
                check = Pattern.compile(Pattern.quote(substring), Pattern.CASE_INSENSITIVE).matcher(arrOfStr[i]).find();
            } else {
                check = arrOfStr[i].contains(substring);
            }
            if (check) {
                result = result + arrOfStr[i];
            }

        }
        return result;
    }

    private static String r_method(String content, String old_s, String new_s, Boolean caseSensitive) throws Exception {

        if (Objects.equals(old_s, "")) {
            usage();
            throw new Exception("r argument replaced string can not be empty");
        }

        String result = "";
        String regex = "(?<=" + System.lineSeparator() + ")";
        String[] arrOfStr = content.split(regex);
        if(!StringUtils.isBlank(new_s)){
        if (new_s.substring(0, 1).equals("\\")) {
            String double_backslash = new_s.replace("\\", "\\\\");
            new_s=double_backslash;
        }}

        for (int i = 0; i < arrOfStr.length; i++) {
            if (!caseSensitive) {
                String r_p=arrOfStr[i].replaceFirst("(?i)" + Pattern.quote(old_s), new_s);
                arrOfStr[i] = r_p;
            } else {
                String r_p = arrOfStr[i].replaceFirst(Pattern.quote(old_s), new_s);
                arrOfStr[i] = r_p;
            }
            result = result + arrOfStr[i];

        }
        return result;
    }


    private static String s_method(String content, String suffix) throws Exception {
        if (Objects.equals(suffix, "")) {
            usage();
            throw new Exception("suffix can not be empty");
        }
        String result = "";
        String regex = "(?<=" + System.lineSeparator() + ")";
        String[] arrOfStr = content.split(regex);
        for (int i = 0; i < arrOfStr.length; i++) {
            arrOfStr[i] = arrOfStr[i].replace(System.lineSeparator(), suffix + System.lineSeparator());
            result = result + arrOfStr[i];

        }
        return result;
    }


    private static String n_method(String content, String origin_content, int n, ArrayList<String> r_value, Boolean caseSensitive) {

        String result = "";
        String regex = "(?<=" + System.lineSeparator() + ")";
        String[] arrOfStr = content.split(regex);
        String[] arrOfStr_origin = origin_content.split(regex);
        for (int i = 0; i < arrOfStr.length; i++) {
            if (!StringUtils.isBlank(arrOfStr[i]) || arrOfStr[i].contains(System.lineSeparator())) {

                String line_start;
                int origin_i;
                Boolean check_line;
                if (r_value != null) {
                    if (!caseSensitive) {
                        String upper_ori_line = arrOfStr_origin[i].toUpperCase();
                        check_line = upper_ori_line.contains(r_value.get(0).toUpperCase(Locale.ROOT));
                    } else {
                        check_line = arrOfStr_origin[i].contains(r_value.get(0));
                    }
                    if (check_line) {
                        origin_i = Arrays.asList(arrOfStr_origin).indexOf(arrOfStr_origin[i]);
                    } else {
                        origin_i = Arrays.asList(arrOfStr_origin).indexOf(arrOfStr[i]);
                    }
                } else {
                    origin_i = Arrays.asList(arrOfStr_origin).indexOf(arrOfStr[i]);
                }
                int length = String.valueOf(i + 1).length();
                if (length < n) {

                    String zeros = "0".repeat(n - length);
                    line_start = zeros + (origin_i + 1);
                } else {
                    line_start = String.valueOf((origin_i + 1));
                }

                arrOfStr[i] = line_start + " " + arrOfStr[i];
            }
            result = result + arrOfStr[i];

        }
        return result;
    }


    private static String w_method(String content) {

        String result = "";
        String regex = "(?<=" + System.lineSeparator() + ")";
        String[] arrOfStr = content.split(regex);
        for (int i = 0; i < arrOfStr.length; i++) {
            if (arrOfStr[i].contains(" ")) {
                arrOfStr[i] = arrOfStr[i].replace(" ", "");
                arrOfStr[i] = arrOfStr[i].replace("\t", "");
            }
            result = result + arrOfStr[i];

        }
        return result;
    }

    private static Map<String, Object> handle_argument(String[] args) throws Exception {

        Map<String, Object> arg_all = new HashMap<>();

        String[] p_flags = {"-o", "-k", "-s", "-n"};
        String[] r_flags = {"-r"};
        String[] position_flag = {"FILE"};
        String[] n_p = {"-i", "-w"};

        for (String text : p_flags) {
            arg_all.put(text, null);
        }
        for (String text : r_flags) {
            arg_all.put(text, null);
        }
        for (String text : n_p) {
            arg_all.put(text, false);
        }
        for (String text : position_flag) {
            arg_all.put(text, null);
        }
        // deep copy for tracking
        List<String> copy = new ArrayList<String>();
        for (String text : args) {
            copy.add(text);
        }

        // assign value
        int i = 0;
        while (copy.size() > 0) {
            String f = copy.get(0);
            Boolean check = Arrays.asList(p_flags).contains(f);
            Boolean check_r = Arrays.asList(r_flags).contains(f);
            Boolean check_n_p = Arrays.asList(n_p).contains(f);
            if (f.substring(0, 1).equals("-") && check) {
                if (copy.size() < 2) {
                    usage();
                    throw new Exception(" option miss argument");
                }
                String f_next = copy.get(1);
                arg_all.put(f, f_next);
                copy.remove(f);
                copy.remove(f_next);

            } else if (f.substring(0, 1).equals("-") && check_r) {
                if (copy.size() < 3) {
                    usage();
                    throw new Exception("r option miss argument");
                }
                String f_n_1 = copy.get(1);
                String f_n_2 = copy.get(2);
                ArrayList<String> f_n = new ArrayList<String>();
                f_n.add(f_n_1);
                f_n.add(f_n_2);
                arg_all.put(f, f_n);
                copy.remove(f);
                copy.remove(f_n_1);
                copy.remove(f_n_2);
            } else if (f.substring(0, 1).equals("-") && check_n_p) {
                arg_all.put(f, true);
                copy.remove(f);
            } else if (f.substring(0, 1).equals("-") && !check && !check_r && !check_n_p) {
                usage();
                throw new Exception("invalid option");
            } else {
                if (arg_all.get("FILE") != null) {
                    usage();
                    throw new Exception("invalid option");
                }
                arg_all.put("FILE", f);
                copy.remove(f);
            }

        }
        return arg_all;

    }


    private static Map<String, Object> pass_argument(String[] args) throws Exception {

        if (args.length == 0) {
            usage();
            throw new Exception("no argument");
        }

        Map<String, Object> arg_all = new HashMap<>();


        arg_all = handle_argument(args);

        String file = (String) arg_all.get("FILE");
        if (file == null) {
            usage();
            throw new Exception("input file flag not inputted");
        }

        String o_value = (String) arg_all.get("-o");

        Boolean i_exist = (Boolean) arg_all.get("-i");

        String k_value = (String) arg_all.get("-k");
        Boolean k_exist;
        if (k_value == null) {
            k_exist = false;
        } else {
            k_exist = true;
        }


        ArrayList<String> r_value = (ArrayList<String>) arg_all.get("-r");
        Boolean r_exist;
        if (r_value == null) {
            r_exist = false;
        } else {
            r_exist = true;
        }

        String s_value = (String) arg_all.get("-s");
        String n_value = (String) arg_all.get("-n");
        Boolean n_exist;
        if (n_value == null) {
            n_exist = false;
        } else {
            n_exist = true;
        }

        Boolean w_value = (Boolean) arg_all.get("-w");
        Boolean w_exist;
        if (!w_value) {
            w_exist = false;
        } else {
            w_exist = true;
        }

        if (k_exist || r_exist) {
            if (k_exist && r_exist) {
                usage();
                throw new Exception("k and r option can not be together");
            }
        } else {
            if (i_exist) {
                usage();
                throw new Exception("i need to be with k or r option ");
            }

        }

        if (n_exist || w_exist) {
            if (n_exist && w_exist) {
                usage();
                throw new Exception("n and w options can not be together");
            }
            if (n_exist) {
                // n value
                Boolean is_n = n_value.matches("-?\\d+(\\.\\d+)?");

                if (!is_n) {
                    usage();
                    throw new Exception("n argument is invalid");
                } else {
                    Boolean n_range = Integer.parseInt(n_value) <= 9 && Integer.parseInt(n_value) >= 1;
                    if (!n_range) {
                        usage();
                        throw new Exception("n argument is invalid");
                    }
                }
            }
        }


        return arg_all;
    }


    public static void main(String[] args) throws IOException {

        // get argument list
        Map<String, Object> all_arg_values;
        try {
            all_arg_values = pass_argument(args);
        } catch (Exception e) {
            return;
        }

        // get input content
        String file_name = (String) all_arg_values.get("FILE");
        Path inputFile = Paths.get(file_name);
        String input_content;
        try {
            input_content = getFileContent(inputFile);
        } catch (Exception e) {
            return;
        }


        try {
            check_input(input_content);
        } catch (Exception e) {
            return;
        }


        // --o , --i first
        String o_value = (String) all_arg_values.get("-o");
        Boolean i = (Boolean) all_arg_values.get("-i");


        Boolean caseSensitive = true;
        // case insensitive
        if (i) {
            caseSensitive = false;
        }

        //  -k, -r, -n, -w, and -s
        String k_value = (String) all_arg_values.get("-k");
        ArrayList<String> r_value = (ArrayList<String>) all_arg_values.get("-r");
        String n_value = (String) all_arg_values.get("-n");
        Boolean w_value = (Boolean) all_arg_values.get("-w");
        String s_value = (String) all_arg_values.get("-s");

        String input_processed = null;
        //  --k substring
        if (k_value != null) {
            String k = k_value;

            input_processed = k_method(input_content, k, caseSensitive);
        }


        // --r replace old with new string
        if (r_value != null) {
            String old_s = r_value.get(0);
            String new_s = r_value.get(1);
            String in;
            if (input_processed != null) {
                in = input_processed;
            } else {
                in = input_content;
            }
            try {
                input_processed = r_method(in, old_s, new_s, caseSensitive);
            } catch (Exception e) {
                return;
            }
        }

        // line number padding
        if (n_value != null) {
            int n = Integer.parseInt(n_value);
            String in;
            if (input_processed != null) {
                in = input_processed;
            } else {
                in = input_content;
            }
            input_processed = n_method(in, input_content, n, r_value, caseSensitive);
        }

        // remove whitespace
        if (w_value) {
            String in;
            if (input_processed != null) {
                in = input_processed;
            } else {
                in = input_content;
            }
            input_processed = w_method(in);
        }

        // suffix append at end
        if (s_value != null) {
            String s = s_value;
            String in;
            if (input_processed != null) {
                in = input_processed;
            } else {
                in = input_content;
            }
            try {
                input_processed = s_method(in, s);
            } catch (Exception e) {
                return;
            }
        }


        // output
        String out;
        if (input_processed != null) {
            out = input_processed;
        } else {
            out = input_content;
        }

        if (o_value != null) {
            File output_file = new File(o_value);
            try {
                o_method(out, output_file);
            } catch (Exception e) {
                return;
            }
        } else {
            System.out.print(out);

        }

    }


    private static void usage() {
        System.err.println("Usage: textprocessor [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE");
    }
}
