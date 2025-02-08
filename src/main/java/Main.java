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
            } else {
                System.out.println(input + ": command not found");
            }
        } while (true);
        
    }
}
