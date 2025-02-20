import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        do {
        	System.out.print("$ ");
        	Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if("exit 0".equals(input)) {
            	System.exit(0);
            } else if (null != input && input.startsWith("echo ")) {
            	System.out.println(input.replace("echo ", ""));
            } else if (null != input && (input.startsWith("type"))) {
        		String inputText = input.replaceFirst("type ","");

            	if((input.startsWith("type echo")  || input.startsWith("type exit") || input.startsWith("type type"))) {
                	System.out.println(inputText + " is a shell builtin");
            	} else {
            		 System.out.println(inputText + ": not found");
            	}
            } else {
                System.out.println(input + ": command not found");
            }
        } while (true);
        
    }
}
