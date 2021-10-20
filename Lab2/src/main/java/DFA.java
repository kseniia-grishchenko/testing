import java.io.FileNotFoundException;
import java.util.*;

public class DFA {
    Set<Character> alphabet;
    Set<Integer> states;
    Integer startState;
    Set<Integer> finalStates;
    Map<Integer, Map<Character, Set<Integer>>> transitionFunction;

    private DFA(Scanner fileScanner) {
        String preAlphabet = "abcdefghijklmnopqrstuvwxyz";
        int alphabetSize = fileScanner.nextInt();
        alphabet = new HashSet<>();
        for (int i = 0; i < alphabetSize; ++i) {
            alphabet.add(preAlphabet.charAt(i));
        }
        int numberOfStates = fileScanner.nextInt();
        states = new HashSet<>(numberOfStates);
        for (int i = 0; i < numberOfStates; ++i) {
            states.add(i);
        }
        startState = fileScanner.nextInt();
        int numberOfFinalStates = fileScanner.nextInt();
        finalStates = new HashSet<>(numberOfFinalStates);
        for (int i = 0; i < numberOfFinalStates; ++i) {
            finalStates.add(fileScanner.nextInt());
        }
        transitionFunction = new HashMap<>(numberOfStates);
        for (Integer state : states) {
            transitionFunction.put(state, new HashMap<>());
        }
        while (fileScanner.hasNext()) {
            Integer fromState = fileScanner.nextInt();
            Character viaLetter = fileScanner.next().charAt(0);
            Integer toState = fileScanner.nextInt();
            if (!transitionFunction.get(fromState).containsKey(viaLetter)) {
                transitionFunction.get(fromState).put(viaLetter, new HashSet<>());
            }
            transitionFunction.get(fromState).get(viaLetter).add(toState);
        }
    }

    DFA(String pathname) throws FileNotFoundException {
        this(Main.getScanner(pathname));
    }

    Set<Integer> ComSt(Integer steps) throws Main.CompleteStepNotPossibleException {
        Set<Integer> fromStates = new HashSet<>();
        fromStates.add(startState);
        for (int step = 0; step < steps; ++step) {
            Set<Integer> toStates = new HashSet<>();
            for (Integer fromState : fromStates) {
                Set<Integer> toStates_ = new HashSet<>();
                for (Character viaLetter : alphabet) {
                    if (!transitionFunction.get(fromState).containsKey(viaLetter)) {
                        throw new Main.CompleteStepNotPossibleException("NO");
                    }
                    toStates_.addAll(transitionFunction.get(fromState).get(viaLetter));
                }
                toStates = toStates_;
            }
            fromStates =  toStates;
        }
        return fromStates;
    }

}