import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws Exception {
        String directoryName = System.getProperty("user.dir");
        do {
        	System.out.print("$ ");
        	Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] userCommands = input.split(" ");
            switch(userCommands[0]){
                case "exit", "exit;" -> System.exit(0);
                //case "echo" -> System.out.println(String.join(" ", input.replace("echo ", "").replace("'","")));
                case "echo" -> System.out.println(String.join(" ", handleSingleQuote(input.replace("echo ", ""), "'(.*)'|\\S+")));
                case "type" -> handleTypeCommand(userCommands[1]);
                case "pwd"  -> System.out.println(directoryName);
                case "cd"   -> {
                    String userParam = input.replace("cd ", "");
                    if(userParam.contains("~")){
                        String homeDirectory = System.getenv("HOME");
                        userParam = userParam.replace("~", homeDirectory);
                    }
                    String path = getAbsolutePath(String.join(" ", handleSingleQuote(userParam)), directoryName);
                    if(Files.isDirectory(Path.of(path))){
                        directoryName = Path.of(path).normalize().toString();
                    } else {
                        System.out.println(input.replace("cd ", "") + ": No such file or directory");
                    }
                }
                default -> {
                    String command = input.split(" ")[0];
                    String path = getPath(command);
                    if (path == null) {
                        System.out.println(command + ": command not found");
                    } else {
                        String[] osCommands = getOsCommands(command, input.substring(command.length()));
                        Process p = Runtime.getRuntime().exec(osCommands);
                        p.getInputStream().transferTo(System.out);
                    }
                }
            }
        } while (true);
        
    }

    private static String[] getOsCommands(String command, String params){
        List<String> userCommands = new ArrayList<>();
        userCommands.add(command);
        userCommands.addAll(handleSingleQuote(params));
        return userCommands.toArray(new String[0]);
    }

    private static List<String> handleSingleQuote(String input){
        return handleSingleQuote(input, null);
    }

    private static List<String> handleSingleQuote(String input, String regex){
        List<String> userCommands = new ArrayList<>();
        String aRegex = null == regex ? "'([^']*)'|\\S+" :  regex ;
        Matcher matcher = Pattern.compile(aRegex).matcher(input);
        while(matcher.find()){
            userCommands.add((null == matcher.group(1)) ? matcher.group() : matcher.group(1).replace("'", ""));
        }
        return userCommands;
    }

    private static String getAbsolutePath(String directoryPath, String currentDirectory){
        currentDirectory = (currentDirectory.endsWith("/") ? currentDirectory : currentDirectory + "/");
        if(directoryPath.startsWith("/")){
            return directoryPath;
        } else {
            return currentDirectory + directoryPath;
        }
    }

    private static void handleTypeCommand(String inputText){
        String[] typeCommands = {"echo", "exit", "type", "pwd", "cd"};
        if(Arrays.asList(typeCommands).contains(inputText)) {
            System.out.println(inputText + " is a shell builtin");
        } else {
            String path = getPath(inputText);
            if(null != path){
                System.out.println(inputText + " is " + path);
            } else {
                System.out.println(inputText + ": not found");
            }
        }
    }

    private static String getPath(String parameter) {
        for (String path : System.getenv("PATH").split(":")) {
          Path fullPath = Path.of(path, parameter);
          if (Files.isRegularFile(fullPath)) {
            return fullPath.toString();
          }
        }
        return null;
      }
}

