// You can experiment here, it won’t be checked

import java.util.Scanner;

public class Task {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String str = scanner.nextLine();
  }
}

class Util {
  public static String capitalize(String str) {
    System.out.printf("Before: %s\n", str);
    if (str == null || str.isBlank()) {
      return str;
    }

    if (str.length() == 1) {
      return str.toUpperCase();
    }
    System.out.printf("After: %s\n", Character.toUpperCase(str.charAt(0)) + str.substring(1));
    return Character.toUpperCase(str.charAt(0)) + str.substring(1);
  }
}
