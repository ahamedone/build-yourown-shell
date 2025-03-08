import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) throws Exception {
        String directoryName = System.getProperty("user.dir");
        do {
        	System.out.print("$ ");
        	Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] userCommands = input.split("\\s+");
            switch(userCommands[0]){
                case "exit" -> System.exit(0);
                case "echo" -> System.out.println(input.replace("echo ", ""));
                case "type" -> handleTypeCommand(userCommands[1]);
                case "pwd"  -> System.out.println(directoryName);
                case "cd"   -> {
                    if(userCommands[1].contains("~")){
                        String homeDirectory = System.getenv("HOME");
                        userCommands[1] = userCommands[1].replace("~", homeDirectory);
                    }
                    String path = getAbsolutePath(userCommands[1], directoryName);
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
                        String fullPath = path + input.substring(command.length());
                        Process p = Runtime.getRuntime().exec(input.split(" "));
                        p.getInputStream().transferTo(System.out);
                    }
                }
            }
        } while (true);
        
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

