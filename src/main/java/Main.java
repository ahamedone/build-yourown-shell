import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {
    public static void main(String[] args) throws Exception {
        do {
        	System.out.print("$ ");
        	Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] typeCommands = {"echo", "exit", "type", "pwd"};
            
            if("exit 0".equals(input)) {
            	System.exit(0);
            } else if (null != input && input.startsWith("echo ")) {
            	System.out.println(input.replace("echo ", ""));
            } else if (null != input && (input.startsWith("type"))) {
        		String inputText = input.substring(5);
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
            } else if (null != input && (input.trim().equals("pwd"))){
                String directoryName = System.getProperty("user.dir");
                System.out.println(directoryName);
            } else {
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
        } while (true);
        
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

