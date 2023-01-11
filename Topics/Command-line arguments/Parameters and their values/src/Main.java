class Problem {
    public static void main(String[] args) {
        String paramater = null;
        String value;
        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) {
                paramater = args[i];
                continue;
            } else {
                value = args[i];
            }
            System.out.printf("%s=%s\n", paramater, value);
        }
    }
}