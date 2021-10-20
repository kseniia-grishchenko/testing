import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.format("Enter file name ");
            String pathname = scanner.next();
            DFA nfa = new DFA(pathname);
            System.out.format("Enter k: ");
            int k = scanner.nextInt();
            System.out.println(k);

            try {
                Set<Integer> After = nfa.ComSt(k);
                After.removeAll(nfa.finalStates);
                if (After.isEmpty()) {
                    System.out.println("YES");
                } else {
                    System.out.println("NO");
                }
            } catch (CompleteStepNotPossibleException ex) {
                System.out.println("NO");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Invalid file pathname");
        }
    }

    static class CompleteStepNotPossibleException extends Exception {
        CompleteStepNotPossibleException(String message) {
            super(message);
        }
    }

    static Scanner getScanner(String pathname) throws FileNotFoundException {
        File file = new File(pathname);

        if (!file.exists()) {
            System.out.format("File '%s' does not exist.%n", pathname);
        }

        if (!file.canRead()) {
            System.out.format("Cannot read file '%s'.%n", pathname);
        }

        return new Scanner(file);
    }
}