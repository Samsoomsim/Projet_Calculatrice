public class Main {
<<<<<<< HEAD
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
=======
    public static void main(String[] args) {
        CalculatorModel m = new CalculatorModel();
        m.addListener((acc, st) -> {
            System.out.print("ACC=" + acc + " | STACK=");
            System.out.println(st.isEmpty() ? "[]" : st.toString());
        });

        m.setAccumulator(12); m.push();   // empile 12
        m.setAccumulator(7);  m.add();    // 12 + 7 -> ACC=19
        m.push();                         // empile 19
        m.setAccumulator(3);  m.mul();    // 19 * 3 -> ACC=57
>>>>>>> origin/yanis
    }
}
